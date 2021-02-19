package cn.me.controller;

import cn.me.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}
