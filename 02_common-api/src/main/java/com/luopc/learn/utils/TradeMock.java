package com.luopc.learn.utils;

import com.github.javafaker.Faker;
import com.luopc.learn.api.trade.Trade;
import com.luopc.learn.api.user.MarketMaker;
import com.luopc.learn.api.user.Trader;
import com.luopc.learn.api.user.TraderManger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TradeMock {

    public static Trade mock() {
        return mockSpot();
    }

    public static Trade mockSwap() {
        MarketMaker eb = MarketMaker.mock();
        TraderManger fa = TraderManger.mock();
        return mockSwap(fa, eb);
    }

    public static Trade mockSpot() {
        MarketMaker eb = MarketMaker.mock();
        TraderManger fa = TraderManger.mock();
        LocalDate valueDate = randomValueDate(1);
        return mockSpotForward(fa, eb, valueDate);
    }

    public static Trade mockForward() {
        MarketMaker eb = MarketMaker.mock();
        TraderManger fa = TraderManger.mock();
        LocalDate valueDate = randomValueDate(7);
        return mockSpotForward(fa, eb, valueDate);
    }

    public static Trade mockSpotForward(Trader fa, Trader eb, LocalDate valueDate) {
        Faker faker = new Faker(Locale.ENGLISH);
        String ccy1 = faker.currency().code();
        String ccy2 = faker.currency().code();
        BigDecimal strike = new BigDecimal(faker.commerce().price());
        Trade trade = TradeBuilder.createClientCashTrade(fa, eb, isBuy(), ccy1, ccy2, generateAmt(), strike, valueDate);
        mockDownStreamIds(trade);
        mockBaseInfo(trade);
        return trade;
    }

    public static Trade mockSwap(Trader fa, Trader eb) {
        Faker faker = new Faker(Locale.ENGLISH);
        LocalDate valueDate01 = randomValueDate(180);
        LocalDate valueDate02 = randomValueDate(720);

        LocalDate farLegValueDate, nearLegValueDate;
        if (valueDate01.isAfter(valueDate02)) {
            farLegValueDate = valueDate01;
            nearLegValueDate = valueDate02;
        } else {
            farLegValueDate = valueDate02;
            nearLegValueDate = valueDate01;
        }

        String ccy1 = faker.currency().code();
        String ccy2 = faker.currency().code();

        long num = nearLegValueDate.until(farLegValueDate, ChronoUnit.DAYS);
        long rate = (num / 24 / 60 / 60 / 1000) / 365;
        BigDecimal nearLegStrike = new BigDecimal(faker.commerce().price());
        BigDecimal farLegStrike = nearLegStrike.multiply(new BigDecimal(1 + rate));

        Trade trade = TradeBuilder.createClientSwapTrade(fa, eb, isBuy(), ccy1, ccy2, generateAmt(), nearLegStrike, farLegStrike, nearLegValueDate, farLegValueDate);
        mockDownStreamIds(trade);
        mockBaseInfo(trade);
        return trade;
    }

    public static void mockIds(Trade trade) {
        Faker faker = new Faker(Locale.ENGLISH);
        String tradeId = String.valueOf(faker.internet().password());
        String unitId = faker.code().gtin8();
        String matchedId = faker.code().ean8();
        String groupId = faker.code().ean8();

        trade.setTradeId(tradeId);
        trade.setUnitId(unitId);
        trade.setMatchedId(matchedId);
        trade.setGroupId(groupId);
    }

    public static void mockDownStreamIds(Trade trade) {
        Faker faker = new Faker(Locale.ENGLISH);
        Optional.ofNullable(trade.getTradeLegList()).orElse(new ArrayList<>()).forEach(tradeLeg -> {
            String uti = faker.finance().iban();
            String regulatoryKey = faker.code().asin() + faker.code().gtin13();
            String downStreamId = faker.finance().bic() + faker.code().gtin8();

            tradeLeg.setUti(uti);
            tradeLeg.setRegulatoryKey(regulatoryKey);
            tradeLeg.setDownStreamId(downStreamId);
        });
    }

    public static void mockBaseInfo(Trade trade) {
        Faker faker = new Faker(Locale.ENGLISH);
        String userName = faker.name().firstName();
        trade.setCreatedBy(userName);
        trade.setUpdatedBy(userName);
        trade.setPlatform("MOCK");
    }

    public static BigDecimal generateAmt() {
        Faker faker = new Faker(Locale.ENGLISH);
        return BigDecimal.valueOf(faker.number().randomDouble(0, 1000, 1000000));
    }

    public static boolean isBuy() {
        Faker faker = new Faker(Locale.ENGLISH);
        return faker.number().randomDouble(0, 1, 1000) % 2 == 0;
    }


    public static LocalDate randomValueDate(int dayAfterToday) {
        Random random = new Random();
        return LocalDate.now().plusDays(random.nextInt(dayAfterToday));
    }


}
