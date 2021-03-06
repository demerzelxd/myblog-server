package cn.me.service;

import cn.me.model.po.TagInfo;
import cn.me.model.po.Tags;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 标签表 服务类
 *
 * @author Giskard
 * @since 2021-03-09
 */
public interface TagsService extends IService<Tags>
{
	List<TagInfo> findAllTagsAndBlogs();
}
