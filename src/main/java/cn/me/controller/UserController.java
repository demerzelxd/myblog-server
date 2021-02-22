package cn.me.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.me.model.common.Result;
import cn.me.model.dto.UserLoginDTO;
import cn.me.model.dto.UserLogonDTO;
import cn.me.model.po.User;
import cn.me.model.vo.UserVO;
import cn.me.service.UserService;
import cn.me.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户表 前端控制器
 *
 * @author Giskard
 * @since 2021-02-18
 */
@RestController
@RequestMapping("/user")
public class UserController
{
	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * 测试接口，@RequiresAuthentication表示需要登录认证已经成功时才能访问
	 * 相当于subject.isAuthenticated()结果为true时
	 * @param id
	 * @return
	 */
	// @RequiresAuthentication
	@GetMapping("/{id}")
	public Result<User> test(@PathVariable("id") Integer id)
	{
		// true
		System.out.println(SecurityUtils.getSubject().isAuthenticated());
		// UserProfile(id=4, username=dada, avatar=https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg, email=1845959992@qq.com)
		System.out.println(SecurityUtils.getSubject().getPrincipals());
		return Result.success(userService.getById(id));
	}

	/**
	 * 用户注册
	 *
	 * @param userLogonDTO
	 * @return
	 */
	@PostMapping("/logon")
	public Result<UserVO> logon(@Validated @RequestBody UserLogonDTO userLogonDTO)
	{
		// 先判断用户名是否重复
		User selected = userService.getOne(new QueryWrapper<User>().eq("username", userLogonDTO.getUsername()));
		// 断言用户名相同的用户不存在，如果用户存在，则发生IllegalArgumentException异常
		Assert.isNull(selected, "用户已存在");
		// 再判断邮箱是否重复
		selected = userService.getOne(new QueryWrapper<User>().eq("email", userLogonDTO.getEmail()));
		// 断言邮箱相同的用户不存在，如果用户存在，则发生IllegalArgumentException异常
		Assert.isNull(selected, "邮箱已被注册");

		User user = new User();
		UserVO userVO = new UserVO();
		// 生成随机盐并保存
		String salt = IdUtil.simpleUUID();
		// 明文密码进行md5 + salt + hash散列
		Md5Hash md5Hash = new Md5Hash(userLogonDTO.getPassword(), salt, 1024);
		user.setUsername(userLogonDTO.getUsername()).setPassword(md5Hash.toHex()).setEmail(userLogonDTO.getEmail()).setSalt(salt);
		// 插入后user的id会自动回显
		userService.save(user);
		// 查找插入后的记录，将User转为UserVO
		BeanUtil.copyProperties(userService.getById(user.getId()), userVO);
		return Result.success(userVO);
	}

	@PostMapping("/login")
	public Result login(@Validated @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response)
	{
		// 先判断用户是否存在
		User selected = userService.getOne(new QueryWrapper<User>().eq("username", userLoginDTO.getUsername()));
		// 断言该用户存在，如果用户不存在，则发生IllegalArgumentException异常
		Assert.notNull(selected, "用户不存在");
		// 对用户输入的明文密码进行md5 + salt + hash散列加密
		Md5Hash md5Hash = new Md5Hash(userLoginDTO.getPassword(), selected.getSalt(), 1024);
		if (!StringUtils.equals(selected.getPassword(), md5Hash.toHex()))
		{
			// 密码不正确
			throw new IncorrectCredentialsException("用户名或密码不正确");
		}
		String jwt = jwtUtils.generateToken(selected.getId(), 1);
		response.setHeader("Auth", jwt);
		response.setHeader("Access-Control-Expose-Headers", "Auth");

		// 给前端登录之后用户信息的回显
		return Result.success(MapUtil.builder()
				.put("id", selected.getId())
				.put("username", selected.getUsername())
				.put("avatar", selected.getAvatar())
				.put("email", selected.getEmail())
				.map()
		);
	}

	// 退出
	@GetMapping("/logout")
	@RequiresAuthentication
	public Result logout()
	{
		SecurityUtils.getSubject().logout();
		return Result.success();
	}
}
