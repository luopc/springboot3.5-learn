
package com.luopc.learn.redis.controller;

import com.luopc.learn.redis.service.UserService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RedisControllerTest {

//    @Autowired
//    private UserService userService;
//
//    @Test
//    @Ignore
//    public void save() {
//        userService.saveUser();
//    }
}
