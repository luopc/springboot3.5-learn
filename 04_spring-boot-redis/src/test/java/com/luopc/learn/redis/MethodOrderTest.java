package com.luopc.learn.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MethodOrderTest {


    @Test
    @Order(1)
    @DisplayName("Method to test low order")
    void lowOrder(){
        log.info("lowOrder method");
    }

    @Test
    @Order(10)
    @DisplayName("Method to test high order")
    void highOrder(){
        log.info("highOrder method");
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void parameterizedTest(int a){
        log.info("parameterizedTest()：a={}",a);
    }
}
