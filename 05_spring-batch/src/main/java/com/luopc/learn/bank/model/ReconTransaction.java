package com.luopc.learn.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReconTransaction {
    // 公共字段
    private String transactionId;
    private LocalDateTime tradeTime;
    private BigDecimal amount;

    // 银行端数据
    private String bankSerialNo;
    private BigDecimal bankAmount;

    // 内部系统数据
    private String internalOrderNo;
    private BigDecimal systemAmount;

    // 对账结果
    private ReconStatus status;
    private String discrepancyType;
    private AlertLevel alertLevel;

    public enum ReconStatus {
        MATCHED,       // 数据一致
        AMOUNT_DIFF,   // 金额不一致
        STATUS_DIFF,    // 状态不一致
        ONLY_IN_BANK,   // 银行单边账
        ONLY_IN_SYSTEM  // 系统单边账
    }
}

