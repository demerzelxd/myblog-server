package cn.me.service.impl;

import cn.me.model.po.User;
import cn.me.mapper.UserMapper;
import cn.me.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户表 服务实现类
 *
 * @author Giskard
 * @since 2021-02-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
