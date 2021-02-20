package cn.me.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.me.model.common.Result;
import cn.me.model.dto.UserLogonDTO;
import cn.me.model.po.User;
import cn.me.model.vo.UserVO;
import cn.me.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
	 * 测试接口，@RequiresAuthentication表示需要登录认证才能访问
	 * @param id
	 * @return
	 */
	// @RequiresAuthentication
	@GetMapping("/{id}")
	public Result<User> test(@PathVariable("id") Integer id)
	{
		return Result.success(userService.getById(id));
	}

	/**
	 * 用户注册
	 * TODO: 用户名邮箱重复校验
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
		//生成随机盐并保存
		String salt = IdUtil.simpleUUID();
		//明文密码进行md5 + salt + hash散列
		Md5Hash md5Hash = new Md5Hash(userLogonDTO.getPassword(), salt, 1024);
		user.setUsername(userLogonDTO.getUsername()).setPassword(md5Hash.toHex()).setEmail(userLogonDTO.getEmail()).setSalt(salt);
		// 插入后user的id会自动回显
		userService.save(user);
		// 查找插入后的记录，将User转为UserVO
		BeanUtil.copyProperties(userService.getById(user.getId()), userVO);
		return Result.success(userVO);
	}
}
