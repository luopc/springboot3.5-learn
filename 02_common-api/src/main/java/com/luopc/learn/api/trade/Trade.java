package com.luopc.learn.api.trade;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

    private String tradeId;
    private String previousTradeId;
    private String linkTradeId;
    private String platform;
    private String platformIdentify;
    private String originatingSystem;

    private Integer revision;
    private String parentId;
    private String unitId;
    private String groupId;
    private String matchedId;
    private String msgId;
    private String downstreamId;
    private String ticketNum;
    private String userMemo;
    private String side;
    private String ccyPair;
    private String productGroup;
    private String productType;
    private String productCode;
    private LocalDate tradeDate;
    private String tradeType;
    private String buySellType;
    private String tradeStatus;
    private String allocationStatus;
    private String matchedStatus;
    private String bookingStatus;
    private String srcDealId;
    private String srcSysName;
    private String srcSysType;
    private String tradePartyCode;
    private String tradePartyName;
    private String tradePartyType;
    private String counterPartyCode;
    private String counterPartyName;
    private String tradeCcy;
    private BigDecimal dealRate;
    private BigDecimal stockPrice;
    private boolean metal;
    private LocalDate valueDate;
    private LocalDateTime creationTime;
    private LocalDateTime bookingTime;
    private LocalDateTime executionTime;
    private LocalDateTime allocationTime;
    private LocalDateTime matchedTime;
    private LocalDateTime cancelledTime;
    private LocalDateTime reversionTime;
    private String createdBy;
    private LocalDateTime createdTime;
    private String updatedBy;
    private LocalDateTime updatedTime;
    private boolean activityStatus;
    private boolean deleteFlag;
    private String info;
    private List<TradeLeg> tradeLegList;

}
