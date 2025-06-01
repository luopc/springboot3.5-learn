package com.luopc.learn.api.user;

import com.github.javafaker.Faker;
import com.luopc.learn.utils.SequentialIDUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraderManger implements Trader {

    private String traderId;
    private String traderName;
    private String traderCode;
    private String traderFullName;
    private String traderType;

    public static TraderManger mock() {
        Faker faker = new Faker(Locale.ENGLISH);
        return TraderManger.builder().traderId(SequentialIDUtil.shortIdString())
                .traderName(faker.rockBand().name())
                .traderFullName(faker.rockBand().name()).build();
    }
}
