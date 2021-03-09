package cn.me.service.impl;

import cn.me.mapper.TagsMapper;
import cn.me.model.po.Tags;
import cn.me.service.TagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 标签表 服务实现类
 *
 * @author Giskard
 * @since 2021-03-09
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService
{
}
