package cn.me.service.impl;

import cn.me.model.po.Blog;
import cn.me.mapper.BlogMapper;
import cn.me.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 博客表 服务实现类
 *
 * @author Giskard
 * @since 2021-02-18
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
