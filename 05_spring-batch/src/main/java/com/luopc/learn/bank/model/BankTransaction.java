package com.luopc.learn.bank.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transactionId")
    private String transactionId;
    @Column(name = "bankName")
    private String bankName;
    @Column(name = "accountNumber")
    private String accountNumber;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transactionDate")
    private LocalDate transactionDate;
    @Column(name = "description")
    private String description;

    public BankTransaction(String transactionId, String bankName, String accountNumber,
                           BigDecimal amount, LocalDate transactionDate, String description) {
        this.transactionId = transactionId;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    public enum ReconStatus {
        MATCHED,       // 数据一致
        AMOUNT_DIFF,   // 金额不一致
        STATUS_DIFF,    // 状态不一致
        ONLY_IN_BANK,   // 银行单边账
        ONLY_IN_SYSTEM  // 系统单边账
    }
}
