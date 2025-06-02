package com.luopc.learn.redis;

import com.luopc.learn.RedisApplication;
import com.luopc.learn.api.user.User;
import com.luopc.learn.redis.config.RedisProperties;
import com.luopc.learn.redis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import redis.embedded.RedisServer;

import java.io.IOException;

@Slf4j
@SpringBootTest(classes = RedisApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisApplicationTest {

//    private final RedisServer redisServer;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    private static final String CACHE_USER_ID = "9527";
//
//    public RedisApplicationTest(@Autowired RedisProperties redisProperties) {
//        log.info("Test Redis is running on port: " + redisProperties.getPort());
//        try {
//            RedisServer redisServer = new RedisServer(redisProperties.getPort());
//            redisServer.start();
//            boolean redisUp = redisServer.isActive();
//            if (redisUp) {
//                log.info("Redis is up and running");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @BeforeAll
    static void allInit(@Autowired RedisProperties redisProperties) {
        log.info("allInit()：start before all method");
        log.info("Test Redis is running on port: " + redisProperties.getPort());
        try {
            RedisServer redisServer = new RedisServer(redisProperties.getPort());
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
    public void save() {
        // Arrange
        User mockUser = User.mock();
        mockUser.setId("1001");
        String userId = mockUser.getId();
        log.info("mockUser: " + mockUser);
        userService.saveUser(mockUser);
        log.info("user has been saved into Redis");

        User user = userService.getUser(userId);
        log.info("Query user from redis: {}", user);
    }

    @Test
    @Order(2)
    void getUser() {
        User user = userService.getUser(CACHE_USER_ID);
        log.info("Mock new user: {}", user);
    }


    @Test
    @Order(3)
    void mvcTest() throws Exception {
        User mockUser = User.mock();
        mockUser.setId(CACHE_USER_ID);
        userService.saveUser(mockUser);

        //模拟发送一个请求访问分页查询品牌列表的接口
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/redis/getUser") //设置请求地址
                        .param("id", CACHE_USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk()) //断言返回状态码为200
                .andDo(MockMvcResultHandlers.print()) //在控制台打印日志
                .andReturn(); //返回请求结果
//        log.info("mvc result = {}", mvcResult.getResponse().getContentAsString());
    }


    @AfterEach
    public void runAfterTestMethod() {
        log.info("@After - runAfterTestMethod");
    }

    @AfterAll
    public static void allEnd() {
        log.info("allEnd()：end after all method");
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