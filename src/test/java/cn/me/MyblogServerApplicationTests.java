package cn.me;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.me.constants.JwtConstants;
import cn.me.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class MyblogServerApplicationTests
{
	@Autowired
	private JwtUtils jwtUtils;

	@Test
	public void testHutool()
	{
		// 测试DateTime
		// System.out.println(DateTime.now().toJdkDate());
		// System.out.println(new Date());

		DateTime dateTime = DateUtil.offsetMinute(new Date(), 1);
		System.out.println(dateTime);


		// System.out.println(jwtUtils.generateToken(1, 1));
		System.out.println(jwtUtils.parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwiaWF0IjoxNjEzNzg4ODYzLCJleHAiOjE2MTM3ODg5MjN9.H2Xrv60UivGvfP18ym7Qa-E2IgCdV7PHcbJ3eyMvX5FnaLzs-IvahvBZbYScON8a0K5bs_1F4He4CiQWN37vPw"));
		// {id=1, iat=1613784697, exp=1613784757}
		System.out.println(jwtUtils.parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwiaWF0IjoxNjEzNzg4ODYzLCJleHAiOjE2MTM3ODg5MjN9.H2Xrv60UivGvfP18ym7Qa-E2IgCdV7PHcbJ3eyMvX5FnaLzs-IvahvBZbYScON8a0K5bs_1F4He4CiQWN37vPw").get(JwtConstants.JWT_KEY_ID));

		// System.out.println(jwtUtils.isTokenExpired(new Date()));
	}

	@Test
	public void testSalt()
	{
		System.out.println(IdUtil.simpleUUID());
	}
}
