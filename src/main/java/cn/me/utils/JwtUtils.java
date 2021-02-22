package cn.me.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.me.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Jwt工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtUtils
{
	/**
	 * 密钥
	 */
	private String secret;

	/**
	 * 请求头名称
	 */
	private String header;

	/**
	 * 生成jwt token
	 *
	 * @param userId
	 * @param expireSeconds
	 * @return
	 */
	public String generateToken(Integer userId, Integer expireSeconds)
	{
		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.claim(JwtConstants.JWT_KEY_ID, userId)
				.setIssuedAt(DateTime.now())
				.setExpiration(DateUtil.offsetSecond(new Date(), expireSeconds))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	/**
	 * 解析token
	 *
	 * @param token
	 * @return
	 */
	public Claims parseToken(String token)
	{
		try
		{
			return Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		}
		catch (Exception e)
		{
			log.info("token解析失败", e);
			return null;
		}
	}

	/**
	 * token是否过期
	 *
	 * @return
	 */
	public boolean isTokenExpired(Date expire)
	{
		return expire.before(new Date());
	}

	/**
	 * 判断token是否有效
	 * 解析失败与过期均为无效
	 * @param token
	 * @return
	 */
	public boolean isTokenValid(String token)
	{
		Claims claim = this.parseToken(token);
		// 如果解析失败或过期返回false
		return !ObjectUtils.isEmpty(claim) && !this.isTokenExpired(claim.getExpiration());
	}
}
