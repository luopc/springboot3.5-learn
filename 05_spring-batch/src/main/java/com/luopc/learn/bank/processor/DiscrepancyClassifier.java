package com.luopc.learn.bank.processor;

import com.luopc.learn.bank.model.AlertLevel;
import com.luopc.learn.bank.model.ReconTransaction;

import com.luopc.learn.bank.model.ReconTransaction.ReconStatus;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class DiscrepancyClassifier implements ItemProcessor<ReconTransaction, ReconTransaction> {
    @Override
    public ReconTransaction process(ReconTransaction item){
        if (item.getStatus() != ReconStatus.MATCHED) {
            // 添加告警标记
            item.setAlertLevel(calculateAlertLevel(item));
        }
        return item;
    }

    private AlertLevel calculateAlertLevel(ReconTransaction t){
        if (t.getAmount().compareTo(new BigDecimal("1000000")) > 0) {
            return AlertLevel.CRITICAL;
        }
        return AlertLevel.WARNING;
    }
}
