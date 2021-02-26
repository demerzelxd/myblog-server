package cn.me.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.me.model.common.Result;
import cn.me.model.dto.BlogDTO;
import cn.me.model.po.Blog;
import cn.me.model.vo.BlogVO;
import cn.me.service.BlogService;
import cn.me.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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

	/**
	 * 分页查询所有
	 * @param pageNow
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/findByPage")
	public Result findByPage(@RequestParam(defaultValue = "1") Integer pageNow, @RequestParam(defaultValue = "10") Integer pageSize)
	{
		IPage<Blog> page = new Page<>(pageNow, pageSize);
		// 将有效的博客按照创建时间倒序排列
		IPage<Blog> pageResult = blogService.page(page, new QueryWrapper<Blog>().eq("status", "1").orderByDesc("create_time"));
		// 实体转VO
		return Result.success(pageResult.convert(Blog -> BeanUtil.copyProperties(Blog, BlogVO.class)), null);
	}

	/**
	 * 博客详情
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Result<BlogVO> findDetail(@PathVariable("id") Integer id)
	{
		Blog blog = blogService.getById(id);
		Assert.notNull(blog, "该博客不存在或已被删除");
		BlogVO blogVO = new BlogVO();
		BeanUtil.copyProperties(blog, blogVO);
		return Result.success(blogVO, null);
	}

	/**
	 * 新增或编辑博客，需要认证成功才可
	 * @param blogDTO
	 * @return
	 */
	@RequiresAuthentication
	@PostMapping("/addOrUpdate")
	public Result<BlogVO> addOrUpdateBlog(@Validated @RequestBody BlogDTO blogDTO)
	{
		boolean flag = ObjectUtils.isEmpty(blogDTO.getId());
		if(!flag)
		{
			// id不为空，说明是编辑
			// 只能编辑自己的文章
			Assert.isTrue(blogDTO.getUserId().equals(ShiroUtils.getProfile().getId()), "没有权限编辑");
		}
		Blog blog = new Blog();
		BlogVO blogVO = new BlogVO();
		BeanUtil.copyProperties(blogDTO, blog);
		blogService.saveOrUpdate(blog);
		// 如果是新增，blog会回显id；如果是编辑，blog仍是原来的id
		// 获取新增或编辑后的记录，返回
		BeanUtil.copyProperties(blogService.getById(blog.getId()), blogVO);
		String msg = flag ? "新增博客成功" : "编辑博客成功";
		return Result.success(blogVO, msg);
	}
}
