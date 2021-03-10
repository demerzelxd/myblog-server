package cn.me.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.me.model.common.Result;
import cn.me.model.po.Blog;
import cn.me.model.po.Tags;
import cn.me.model.vo.BlogVO;
import cn.me.model.vo.TagsVO;
import cn.me.service.BlogService;
import cn.me.service.TagsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@Autowired
	private BlogService blogService;

	/**
	 * 查询所有标签
	 * @return
	 */
	@GetMapping("/findAll")
	public Result<List<TagsVO>> findAll()
	{
		List<Tags> tagsList = tagsService.list();
		// 不同类型的list相互转换
		return Result.success(tagsList.stream().map(Tags -> BeanUtil.copyProperties(Tags, TagsVO.class)).collect(Collectors.toList()));
	}

	/**
	 * 查询所有的tag以及其对应的blog
	 * @return
	 */
	@GetMapping("/findAllTagsAndBlogs")
	public Result<Map<String, List<BlogVO>>> findAllTagsAndBlogs()
	{
		// HashMap的key没有顺序，所以用LinkedHashMap，这样就能保证老的tag在前
		Map<String, List<BlogVO>> map = new LinkedHashMap<>();
		// tag老的在前
		tagsService.list(new QueryWrapper<Tags>().select("tag_name").orderByAsc("create_time")).forEach(tag -> {
			map.put(tag.getTagName(),
					// blog新的在前
					blogService.list(new QueryWrapper<Blog>().select("id", "title")
							.eq("tag_name", tag.getTagName()).orderByDesc("create_time"))
							.stream().map(Blog -> BeanUtil.copyProperties(Blog, BlogVO.class))
							.collect(Collectors.toList()));
		});
		return Result.success(map);
	}
}
