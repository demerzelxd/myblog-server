package cn.me.controller;

import cn.hutool.core.util.IdUtil;
import cn.me.model.dto.Result;
import cn.me.model.po.User;
import cn.me.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	/**
	 * 用户注册
	 * @return
	 */
	@PostMapping("/logon")
	public Result<User> logon(@RequestBody User user)
	{
		//生成随机盐并保存
		String salt = IdUtil.simpleUUID();
		user.setSalt(salt);
		//明文密码进行md5 + salt + hash散列
		Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
		user.setPassword(md5Hash.toHex());
		// 插入后user的id会自动回显
		userService.save(user);
		return Result.success(userService.getById(user.getId()));
	}
}
