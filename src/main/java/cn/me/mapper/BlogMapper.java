package cn.me.mapper;

import cn.me.model.po.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 博客表 Mapper 接口
 * 使用MybatisPlus增强接口
 * 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 *
 * @author Giskard
 * @since 2021-02-18
 */
public interface BlogMapper extends BaseMapper<Blog> {

}
