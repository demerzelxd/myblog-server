package cn.me.config.shiro.filter;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.me.config.shiro.token.JwtToken;
import cn.me.model.dto.Result;
import cn.me.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends AuthenticatingFilter
{
	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * 获取用户的jwt，并生成shiro能识别的自定义的JwtToken
	 * ctrl O可以重写方法
	 *
	 * @param servletRequest
	 * @param servletResponse
	 * @return
	 * @throws Exception
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception
	{
		// 获取凭证
		String token = this.getToken(servletRequest);
		// 如果用户并没有认证，返回为空
		if (StringUtils.isBlank(token))
		{
			return null;
		}
		// 如果用户已经认证，包装用户凭证
		return new JwtToken(token);
	}

	/**
	 * 校验token为空、过期或不正确的情况并登录认证
	 *
	 * @param servletRequest
	 * @param servletResponse
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception
	{
		// 获取凭证
		String token = this.getToken(servletRequest);
		// 如果用户并没有认证，则下一步去controller的@RequireRoles判断权限，游客只有查看文章等权限
		if (StringUtils.isBlank(token))
		{
			// 放行
			return true;
		}
		// 如果jwt不为空
		// 校验jwt
		Claims claim = jwtUtils.parseToken(token);
		// 如果解析失败或过期
		if (ObjectUtils.isEmpty(claim) || jwtUtils.isTokenExpired(claim.getExpiration()))
		{
			throw new ExpiredCredentialsException("token已失效，请重新登录");
		}
		// 调用AuthenticatingFilter的executeLogin方法，该方法调用了subject.login(token);
		// login方法又会调用自定义realm的doGetAuthenticationInfo方法
		return super.executeLogin(servletRequest, servletResponse);
	}

	/**
	 * 登录认证失败的处理，将认证失败信息返回给前端
	 *
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response)
	{
		// 强转为HttpServletResponse
		HttpServletResponse resp = (HttpServletResponse) response;
		// 获取认证失败信息
		Throwable throwable = ObjectUtils.isEmpty(e.getCause()) ? e : e.getCause();
		// 封装认证失败信息
		Result<Object> result = Result.error(HttpStatus.HTTP_UNAUTHORIZED, throwable.getMessage());
		String json = JSONUtil.toJsonStr(result);
		try
		{
			// 返回给前端
			resp.getWriter().print(json);
		}
		catch (IOException ioException)
		{
			log.info("返回给前端认证失败信息发生异常：【{}】", ioException.getMessage());
		}
		return false;
	}

	/**
	 * 自定义获取token的公共方法
	 *
	 * @param servletRequest
	 * @return
	 */
	protected String getToken(ServletRequest servletRequest)
	{
		// 强转为HttpServerRequest
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		// 从用户的Auth请求头获取凭证
		return request.getHeader("Auth");
	}
}
