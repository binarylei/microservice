package binarylei.redis.springboot.service.impl;


import binarylei.redis.springboot.entity.User;
import binarylei.redis.springboot.mapper.UserMapper;
import binarylei.redis.springboot.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getUserList(String name, int page, int limit) {
        return userMapper.getUserList(name, page, limit);
    }

    @Override
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }


    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public List<User> getUserByAccount(String account) {
        return userMapper.getUserByAccount(account);
    }

    @Override
    public int add(User user) {
        return userMapper.add(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public int delete(Integer id) {
        return userMapper.delete(id);
    }

    @Override
    public int getUserCount() {
        return userMapper.getUserCount();

    }
}