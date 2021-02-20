package cn.me.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.me.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
	 * @param expireMinutes
	 * @return
	 */
	public String generateToken(int userId, int expireMinutes)
	{
		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.claim(JwtConstants.JWT_KEY_ID, userId)
				.setIssuedAt(DateTime.now())
				.setExpiration(DateUtil.offsetMinute(new Date(), expireMinutes))
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
			log.info("token is not valid", e);
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
}
