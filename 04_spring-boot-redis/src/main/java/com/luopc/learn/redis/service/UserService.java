package com.luopc.learn.redis.service;

import com.luopc.learn.api.user.User;

public interface UserService {

    boolean saveUser();
    boolean saveUser(User user);
    User getUser(String id);

}
