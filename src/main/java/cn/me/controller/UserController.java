package cn.me.controller;

import cn.me.model.dto.Result;
import cn.me.model.po.User;
import cn.me.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/index")
	public Result<User> index()
	{
		User user = userService.getById(2);
		return Result.success(user);
	}
}
