package cn.me.mapper;

import cn.me.model.po.TagInfo;
import cn.me.model.po.Tags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 博客表 Mapper 接口
 * 使用MybatisPlus增强接口
 * 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 *
 * @author Giskard
 * @since 2021-03-09
 */
public interface TagsMapper extends BaseMapper<Tags>
{
	List<TagInfo> findAllTagsAndBlogs();
}
