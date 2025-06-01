package com.luopc.learn.redis.controller;

import com.luopc.learn.api.user.User;
import com.luopc.learn.redis.service.UserService;
import com.luopc.learn.utils.SequentialIDUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Resource
    private UserService userService;

    @RequestMapping("save")
    public String save() {
        boolean r = userService.saveUser();
        return r ? "save success" : "save failed";
    }

    @RequestMapping("getUser")
    public User getUser() {
        return userService.getUser(SequentialIDUtil.shortIdString());
    }
}
