package com.luopc.learn.api.trade;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Robin
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TradeLegOption implements TradeLeg {

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
    private BigDecimal strike;
    private String isExotic;
    private String contractType;
    private String optionStyle;
    private String optionType;
    private String optionEvent;
    private BigDecimal notionalAmt;
    private LocalDate expiryDate;
    private LocalTime expiryTime;
    private String expiryZone;
    private BigDecimal premiumAmt;
    private String premiumCcy;
    private LocalDate premiumDate;
    private BigDecimal fixingAmt;
    private String fixingCcy;
    private LocalDate fixingDate;
    private BigDecimal fixingRate;
    private String fixingSource;
    private BigDecimal fees;
    private String strategy;
    private LocalDate payoutDate;
    private String payoutType;
    private BigDecimal payoutAmt;
    private String payoutCcy;
    private LocalDateTime operationTime;
    private String operationType;
    private BigDecimal operationQuantity;
    private String operationSubType;
    private String operationUser;
    private String barrierType;
    private String barrierStyle;
    private BigDecimal barrier1;
    private BigDecimal barrier2;
    private LocalDate barrierStartDate;
    private LocalDate barrierEndDate;
    private LocalDateTime knockTime;
    private BigDecimal knockRate;
    private String knockEvent;
    private boolean isDiscrete;
    protected String uti;
    protected String regulatoryKey;
    protected String downStreamId;

}
