package cn.me.service;

import cn.me.model.po.ArchiveInfo;
import cn.me.model.po.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 博客表 服务类
 *
 * @author Giskard
 * @since 2021-02-18
 */
public interface BlogService extends IService<Blog> {
	List<ArchiveInfo> findArchivesByYear();
}
