package com.luopc.learn.redis;

import com.luopc.learn.RedisApplication;
import com.luopc.learn.api.user.User;
import com.luopc.learn.redis.config.RedisProperties;
import com.luopc.learn.redis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.io.IOException;

@Slf4j
@SpringBootTest(classes = RedisApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisApplicationTest {

    private final RedisServer redisServer;

    @Autowired
    private UserService userService;

    public RedisApplicationTest(@Autowired RedisProperties redisProperties) {
        log.info("Test Redis is running on port: " + redisProperties.getPort());
        try {
            redisServer = new RedisServer(redisProperties.getPort());
            redisServer.start();
            boolean redisUp = redisServer.isActive();
            if (redisUp) {
                log.info("Redis is up and running");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void save() {
        // Arrange
        User mockUser = User.mock();
        String userId = mockUser.getId();
        log.info("mockUser: " + mockUser);
        userService.saveUser(mockUser);
        log.info("user has been saved into Redis");

        User user = userService.getUser(userId);
        log.info("Query user from redis: {}", user);
    }


//    @Test
//    @Order(100)
//    public void after() {
//        log.info("Going to shutdown Redis.");
//        List<Integer> ports = new ArrayList<>();
//        try {
//            boolean redisUp = redisServer.isActive();
//            if (redisUp) {
//                ports = redisServer.ports();
//                log.info("Redis is up and running, going to shutdown, port: {}", ports);
//                redisServer.stop();
//            } else {
//                log.info("Unable to find active redis on port: {}", ports);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}