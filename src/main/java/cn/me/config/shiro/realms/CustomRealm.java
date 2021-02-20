package cn.me.config.shiro.realms;

import cn.hutool.core.bean.BeanUtil;
import cn.me.config.shiro.profile.UserProfile;
import cn.me.config.shiro.token.JwtToken;
import cn.me.constants.StateConstants;
import cn.me.model.po.User;
import cn.me.service.UserService;
import cn.me.constants.JwtConstants;
import cn.me.utils.JwtUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomRealm extends AuthorizingRealm
{
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	/**
	 * 使自定义realm支持该自定义的JwtToken作为校验
	 *
	 * @param token
	 * @return
	 */
	@Override
	public boolean supports(AuthenticationToken token)
	{
		// return super.supports(token);
		return token instanceof JwtToken;
	}

	/**
	 * 授权
	 *
	 * @param principalCollection
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
	{
		return null;
	}

	/**
	 * 认证
	 *
	 * @param authenticationToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
	{
		JwtToken jwtToken = (JwtToken) authenticationToken;
		// 获取用户id
		Integer userId = jwtUtils.parseToken((String) jwtToken.getPrincipal()).get(JwtConstants.JWT_KEY_ID, Integer.class);
		// 获取用户
		User user = userService.getById(userId);
		// 如果用户不存在
		if (ObjectUtils.isEmpty(user))
		{
			throw new UnknownAccountException("用户不存在");
		}
		// 如果用户状态为停用
		if (StringUtils.equals(user.getStatus(), StateConstants.OFF))
		{
			throw new LockedAccountException("账户已被锁定");
		}
		// 如果能获取到用户
		UserProfile userProfile = new UserProfile();
		BeanUtil.copyProperties(user, userProfile);
		return new SimpleAuthenticationInfo(userProfile, jwtToken.getCredentials(), this.getName());
	}
}
