package cn.me.service.impl;

import cn.me.mapper.BlogMapper;
import cn.me.model.po.ArchiveInfo;
import cn.me.model.po.Blog;
import cn.me.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 博客表 服务实现类
 *
 * @author Giskard
 * @since 2021-02-18
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

	@Autowired
	private BlogMapper blogMapper;

	@Override
	public List<ArchiveInfo> findArchivesByYear()
	{
		return blogMapper.findArchivesByYear();
	}
}
