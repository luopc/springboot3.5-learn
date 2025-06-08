package com.luopc.learn.user.process;


import com.luopc.learn.user.entity.User;
import com.luopc.learn.user.entity.User1;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class U1Processor implements ItemProcessor<User1, User> {


    @Override
    public User process(User1 u1) {
        //update user
        return null;
    }
}
