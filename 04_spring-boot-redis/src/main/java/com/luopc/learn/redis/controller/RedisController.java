package com.luopc.learn.redis.controller;

import com.luopc.learn.api.user.User;
import com.luopc.learn.redis.service.UserService;
import com.luopc.learn.utils.SequentialIDUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

    @GetMapping("getUser")
    public ResponseEntity<User> getUser(@RequestParam("id") String id) {
        log.info("getUser id:{}",id);
        User user = userService.getUser(id);
        log.info("getUser user:{}",user);
        return ResponseEntity.ok(user);
    }
}
