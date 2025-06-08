package com.luopc.learn.user.process;

import com.luopc.learn.user.entity.User;
import com.luopc.learn.user.entity.User2;
import org.springframework.batch.item.ItemProcessor;

public class U2Processor implements ItemProcessor<User2, User> {


    @Override
    public User process(User2 u2) {
        //update user
        return null;
    }
}