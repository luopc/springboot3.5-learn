package com.luopc.learn.api.trade;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Robin
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TradeLegCash implements TradeLeg {

    private String legId;
    private String tradeId;
    private int legRole;
    private Integer versionId;
    private String sideType;
    private BigDecimal priAmt;
    private String priCcy;
    private BigDecimal cntAmt;
    private String cntCcy;
    private String ccyPair;
    private LocalDate valueDate;
    private BigDecimal dealtRate;
    private BigDecimal notionalAmt;
    private BigDecimal ndfFixingAmt;
    private BigDecimal ndfFixingRate;
    private String ndfFixingSource;
    private LocalDate ndfFixingDate;
    private String uti;
    private String regulatoryKey;
    private String downStreamId;

    public TradeLegCash(String tradeId, int legRole, String sideType, BigDecimal priAmt, String priCcy, BigDecimal cntAmt, String cntCcy, LocalDate valueDate, BigDecimal dealtRate) {
        this.tradeId = tradeId;
        this.legRole = legRole;
        this.sideType = sideType;
        this.priAmt = priAmt;
        this.priCcy = priCcy;
        this.cntAmt = cntAmt;
        this.cntCcy = cntCcy;
        this.valueDate = valueDate;
        this.dealtRate = dealtRate;
    }

    @Override
    public BigDecimal getStrike() {
        return dealtRate;
    }

    @Override
    public void setStrike(BigDecimal strike) {
        this.dealtRate = strike;
    }
}
