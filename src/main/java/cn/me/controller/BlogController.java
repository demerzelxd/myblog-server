package cn.me.controller;

import cn.me.model.common.Result;
import cn.me.model.po.Blog;
import cn.me.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 博客表 前端控制器
 *
 * @author Giskard
 * @since 2021-02-18
 */
@RestController
@RequestMapping("/blog")
public class BlogController
{
	@Autowired
	private BlogService blogService;

	@GetMapping("/findByPage")
	public Result findByPage(@RequestParam(defaultValue = "1") Integer pageNow, @RequestParam(defaultValue = "10") Integer pageSize)
	{
		return Result.success();
	}

	/**
	 * 博客详情
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Result findDetail(@PathVariable("id") Integer id)
	{
		return Result.success();
	}

	@PostMapping("/edit")
	public Result edit(@Validated @RequestBody Blog blog)
	{
		return Result.success();
	}
}
