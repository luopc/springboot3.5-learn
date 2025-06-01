package com.luopc.learn.api.market;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luopc.learn.utils.CurrencyHelper;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Robin
 */
@Getter
public class Currency {

    private final String ccyCode;
    @JsonIgnore
    private static final Map<String, Currency> STRING_TO_CURRENCY = new ConcurrentHashMap<>();

    private Currency(String ccyCode) {
        this.ccyCode = ccyCode;
    }

    public static Currency getInstance(String ccyCodeStr) {
        if (StringUtils.isNotBlank(ccyCodeStr) && ccyCodeStr.length() == 3) {
            String ccyCode = ccyCodeStr.toUpperCase();
            Currency currency = STRING_TO_CURRENCY.get(ccyCode);
            return Objects.requireNonNullElseGet(currency, () -> creatNewCurrency(ccyCode));
        }
        throw new IllegalArgumentException("Illegal to create ccy pair instance from emtpy string");
    }

    private static Currency creatNewCurrency(String ccyCode) {
        Currency currency = new Currency(ccyCode);
        Currency previous = STRING_TO_CURRENCY.putIfAbsent(ccyCode, currency);
        return Objects.nonNull(previous) ? previous : currency;
    }

    public String getCcyCode() {
        if (ccyCode != null) {
            return ccyCode.toUpperCase();
        }
        return ccyCode;
    }

    @JsonIgnore
    public int getAmtDecimal() {
        return "JPY".equalsIgnoreCase(this.ccyCode) ? 0 : 2;
    }

    @JsonIgnore
    public int getSpotDays() {
        return 2;
    }

    @JsonIgnore
    public boolean isUsdFlag() {
        return CurrencyHelper.USD.equals(this);
    }

    @JsonIgnore
    public boolean isCnyFlag() {
        return CurrencyHelper.CNY.equals(this);
    }

    @JsonIgnore
    public boolean isClsFlag() {
        return Arrays.asList(CurrencyHelper.CLS_GROUP).contains(ccyCode);
    }

    @JsonIgnore
    public boolean isG20Flag() {
        return Arrays.asList(CurrencyHelper.G20_GROUP).contains(ccyCode);
    }

    @JsonIgnore
    public boolean isMetalFlag() {
        return Arrays.asList(CurrencyHelper.METAL_GROUP).contains(ccyCode);
    }

    @JsonIgnore
    public boolean isTopToUsd() {
        return Arrays.asList(CurrencyHelper.TOP_TO_USD).contains(ccyCode);
    }

    public boolean isNdfFlag() {
        return Arrays.asList(CurrencyHelper.NDF_GROUP).contains(ccyCode);
    }

    @Override
    public String toString() {
        return ccyCode;
    }
}
