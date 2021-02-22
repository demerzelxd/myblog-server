package cn.me.config.shiro.filter;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.me.config.shiro.token.JwtToken;
import cn.me.model.common.Result;
import cn.me.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

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
		// 如果用户并没有认证，则下一步可以去controller的@RequireRoles判断权限，游客只有查看文章等权限
		// 用户注册和用户登录时token也是null，放行
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
		// 调用一下 Exception的getCause方法找到原始的异常
		Throwable throwable = ObjectUtils.isEmpty(e.getCause()) ? e : e.getCause();
		// 不可以进行如下throw给全局异常捕获，因为要重写该方法要return false，而throw会直接结束，不会return
		// throw new ShiroException(throwable.getMessage());

		// 封装认证失败信息
		Result result = Result.error(HttpStatus.HTTP_UNAUTHORIZED, throwable.getMessage());
		String json = JSONUtil.toJsonStr(result);
		try
		{
			// 返回给前端
			resp.getWriter().print(json);
		}
		catch (IOException ioException)
		{
			log.info("向前端返回认证失败信息时发生异常：--------------- {}", ioException.getMessage());
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

	/**
	 * 对跨域提供支持
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
	{
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name()))
		{
			httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}
}
