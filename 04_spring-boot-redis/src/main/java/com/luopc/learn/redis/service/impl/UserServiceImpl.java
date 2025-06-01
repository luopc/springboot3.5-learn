package com.luopc.learn.redis.service.impl;

import com.luopc.learn.api.user.User;
import com.luopc.learn.redis.service.UserService;
import com.luopc.learn.redis.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean saveUser() {
        User user = User.mock();
        return saveUser(user);
    }

    @Override
    public boolean saveUser(User user) {
        String userKey = "user:key:" + user.getId();
        redisUtils.set(userKey, user);
        return true;
    }

    @Override
    public User getUser(String id) {
        String userKey = "user:key:" + id;
        return (User) redisUtils.get(userKey);
    }
}
