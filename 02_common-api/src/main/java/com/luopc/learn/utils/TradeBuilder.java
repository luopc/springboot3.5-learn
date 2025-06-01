package com.luopc.learn.utils;

import com.luopc.learn.api.market.CcyPair;
import com.luopc.learn.api.trade.Trade;
import com.luopc.learn.api.trade.TradeLegCash;
import com.luopc.learn.api.user.Trader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class TradeBuilder {

    public static final int RATE_SCALE = 6;

    public static Trade createClientCashTrade(Trader fa, Trader eb, boolean isBuy, String ccy1, String ccy2, BigDecimal tradeAmt, BigDecimal strike, LocalDate valueDate) {
        Trade trade = buildClientTradeInfo(fa, eb);
        return createCashTrade(trade, isBuy, ccy1, ccy2, tradeAmt, strike, valueDate);
    }

    public static Trade createClientCashTrade(Trader fa, Trader eb, String ccy1, String ccy2, BigDecimal tradeAmt, BigDecimal makerAmt, LocalDate valueDate) {
        Trade trade = buildClientTradeInfo(fa, eb);
        return createCashTrade(trade, ccy1, ccy2, tradeAmt, makerAmt, valueDate);
    }

    public static Trade createEBCashTrade(Trader fa, Trader eb, boolean isBuy, String ccy1, String ccy2, BigDecimal tradeAmt, BigDecimal strike, LocalDate valueDate) {
        Trade trade = buildEBTradeInfo(fa, eb);
        return createCashTrade(trade, isBuy, ccy1, ccy2, tradeAmt, strike, valueDate);
    }

    private static Trade createCashTrade(Trade trade, boolean isBuy, String ccy1, String ccy2, BigDecimal tradeAmt, BigDecimal strike, LocalDate valueDate) {
        TradeMock.mockIds(trade);

        trade.setProductGroup("FX");
        trade.setProductType(getProductType(valueDate));
        trade.setValueDate(valueDate);

        String ccyPair = CcyPair.getInstance(ccy1 + ccy2).toString();
        trade.setCcyPair(ccyPair);
        BigDecimal priAmt = tradeAmt.multiply(isBuy ? BigDecimal.ONE : BigDecimal.valueOf(-1));
        String buySelFlag = isBuy ? "BUY" : "SELL";
        BigDecimal cntAmt = tradeAmt.multiply(strike).multiply(BigDecimal.valueOf(-1));

        trade.setDealRate(strike);
        trade.setTradeCcy(ccy1);
        trade.setMetal(false);
        trade.setTradeLegList(List.of(new TradeLegCash(trade.getTradeId(), 1, buySelFlag, priAmt, ccy1, cntAmt, ccy2, trade.getValueDate(), strike)));

        return trade;
    }

    private static Trade createCashTrade(Trade trade, String ccy1, String ccy2, BigDecimal ccy1Amt, BigDecimal ccy2Amt, LocalDate valueDate) {
        TradeMock.mockIds(trade);

        trade.setProductGroup("FX");
        trade.setProductType(getProductType(valueDate));
        trade.setValueDate(valueDate);

        String ccyPair = CcyPair.getInstance(ccy1 + ccy2).toString();
        trade.setCcyPair(ccyPair);
        BigDecimal strike = div(ccy1Amt, ccy2Amt);
        trade.setDealRate(strike);
        trade.setTradeCcy(ccy1);
        trade.setMetal(false);
        String buySelFlag = BigDecimal.ZERO.compareTo(ccy1Amt) > 0 ? "BUY" : "SELL";
        trade.setTradeLegList(List.of(new TradeLegCash(trade.getTradeId(), 1, buySelFlag, ccy1Amt, ccy1, ccy2Amt, ccy2, trade.getValueDate(), strike)));

        return trade;
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, RATE_SCALE);
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (BigDecimal.ZERO.compareTo(v2) != 0) {
            //ROUND_HALF_UP:四舍五入
            return v1.divide(v2, scale, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public static Trade createClientSwapTrade(Trader fa, Trader eb, boolean isBuy, String ccy1, String ccy2, BigDecimal tradeAmt, BigDecimal nearLegStrike, BigDecimal farLegStrike, LocalDate nearLegValueDate, LocalDate farLegValueDate) {
        Trade trade = buildClientTradeInfo(fa, eb);
        return createSwapTrade(trade, isBuy, ccy1, ccy2, tradeAmt, nearLegStrike, farLegStrike, nearLegValueDate, farLegValueDate);
    }

    public static Trade createEBSwapTrade(Trader fa, Trader eb, boolean isBuy, String ccy1, String ccy2, BigDecimal tradeAmt, BigDecimal nearLegStrike, BigDecimal farLegStrike, LocalDate nearLegValueDate, LocalDate farLegValueDate) {
        Trade trade = buildEBTradeInfo(fa, eb);
        return createSwapTrade(trade, isBuy, ccy1, ccy2, tradeAmt, nearLegStrike, farLegStrike, nearLegValueDate, farLegValueDate);
    }

    private static Trade createSwapTrade(Trade trade, boolean isBuy, String ccy1, String ccy2, BigDecimal tradeAmt, BigDecimal nearLegStrike, BigDecimal farLegStrike, LocalDate nearLegValueDate, LocalDate farLegValueDate) {

        TradeMock.mockIds(trade);
        trade.setProductGroup("FX");
        trade.setProductType("Swap");
        String ccyPair = CcyPair.getInstance(ccy1 + ccy2).toString();
        trade.setCcyPair(ccyPair);
        BigDecimal nearPriAmt = tradeAmt.multiply(isBuy ? BigDecimal.ONE : BigDecimal.valueOf(-1));
        BigDecimal farPriAmt = tradeAmt.multiply(isBuy ? BigDecimal.valueOf(-1) : BigDecimal.ONE);


        BigDecimal nearCntAmt = nearPriAmt.multiply(nearLegStrike).multiply(BigDecimal.valueOf(-1));
        BigDecimal farCntAmt = farPriAmt.multiply(farLegStrike).multiply(BigDecimal.valueOf(-1));

        trade.setDealRate(nearLegStrike);
        trade.setTradeCcy(ccy1);
        trade.setValueDate(farLegValueDate);
        trade.setMetal(false);
        trade.setTradeLegList(List.of(
                new TradeLegCash(trade.getTradeId(), 1, isBuy ? "BUY" : "SELL", nearPriAmt, ccy1, nearCntAmt, ccy2,
                        nearLegValueDate, nearLegStrike),
                new TradeLegCash(trade.getTradeId(), 2, isBuy ? "SELL" : "BUY", farPriAmt, ccy1, farCntAmt, ccy2,
                        farLegValueDate, farLegStrike)));

        return trade;
    }

    private static String getProductType(LocalDate valueDate) {
        long timeAfterToday = 0L;
        if (Objects.nonNull(valueDate)) {
            timeAfterToday = LocalDate.now().until(valueDate, ChronoUnit.DAYS);
        }
        if ((int) timeAfterToday <= 7) {
            return "Spot";
        } else {
            return "Forward";
        }
    }

    private static Trade buildClientTradeInfo(Trader fa, Trader eb) {
        Trade trade = new Trade();
        trade.setTradeDate(LocalDate.now());
        trade.setCreationTime(LocalDateTime.now());
        trade.setTradePartyType(fa.getTraderType());

        trade.setTradePartyCode(fa.getTraderCode());
        trade.setTradePartyName(fa.getTraderFullName());

        trade.setCounterPartyCode(eb.getTraderCode());
        trade.setCounterPartyName(eb.getTraderFullName());
        return trade;
    }

    private static Trade buildEBTradeInfo(Trader fa, Trader eb) {
        Trade trade = new Trade();
        trade.setTradeDate(LocalDate.now());
        trade.setCreationTime(LocalDateTime.now());
        trade.setTradePartyType(eb.getTraderType());

        trade.setTradePartyCode(eb.getTraderCode());
        trade.setTradePartyName(eb.getTraderFullName());

        trade.setCounterPartyCode(fa.getTraderCode());
        trade.setCounterPartyName(fa.getTraderFullName());
        return trade;
    }

    /*
     * Mock trade
     */

    public static Trade mock() {
        return TradeMock.mockSpot();
    }

    public static Trade mockSwap() {
        return TradeMock.mockSwap();
    }

    public static Trade mockSpot() {
        return TradeMock.mockSpot();
    }

    public static Trade mockForward() {
        return TradeMock.mockForward();
    }
}
