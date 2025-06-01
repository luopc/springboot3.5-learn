package com.luopc.learn.api.user;

import com.github.javafaker.Faker;
import com.luopc.learn.utils.RandomNumberUtil;
import com.luopc.learn.utils.SequentialIDUtil;
import lombok.*;

import java.util.Locale;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    private int age;
    private String email;
    private String password;


    public static User mock() {
        Faker faker = new Faker(Locale.ENGLISH);
        return User.builder()
                .id(SequentialIDUtil.shortIdString())
                .name(faker.name().fullName())
                .age(RandomNumberUtil.randomNumber(18, 60))
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }

}
