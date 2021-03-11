package cn.me.controller;

import cn.me.model.common.Result;
import cn.me.model.po.TagInfo;
import cn.me.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 标签表 前端控制器
 *
 * @author Giskard
 * @since 2021-03-09
 */
@RestController
@RequestMapping("/tags")
public class TagsController
{
	@Autowired
	private TagsService tagsService;

	/**
	 * 查询所有的tag以及其对应的blog
	 * @return
	 */
	@GetMapping("/findAllTagsAndBlogs")
	public Result<List<TagInfo>> findAllTagsAndBlogs()
	{
		List<TagInfo> tagInfoList = tagsService.findAllTagsAndBlogs();
		return Result.success(tagInfoList);
	}
}
