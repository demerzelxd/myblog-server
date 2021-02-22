package cn.me.config.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义jwtToken，类似UsernamePasswordToken
 */
public class JwtToken implements AuthenticationToken
{
	public String token;

	public JwtToken(String token)
	{
		this.token = token;
	}

	/**
	 * 身份信息
	 * @return
	 */
	@Override
	public Object getPrincipal()
	{
		return token;
	}

	/**
	 * 登录凭证
	 * @return
	 */
	@Override
	public Object getCredentials()
	{
		return token;
	}
}
