package com.luopc.learn.bank.processor;

import com.luopc.learn.bank.model.ReconTransaction;
import com.luopc.learn.bank.model.ReconTransaction.ReconStatus;
import com.luopc.learn.bank.service.TransactionService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class DataMatchingProcessor implements ItemProcessor<ReconTransaction, ReconTransaction> {

    @Autowired
    private TransactionService transactionService;

    @Override
    public ReconTransaction process(ReconTransaction item) {
        // 双数据源匹配逻辑
        if (item.getBankSerialNo() == null) {
            item.setStatus(ReconStatus.ONLY_IN_SYSTEM);
        } else if (item.getInternalOrderNo() == null) {
            item.setStatus(ReconStatus.ONLY_IN_BANK);
        } else {
            compareAmounts(item);
            compareStatuses(item);
        }
        return item;
    }

    private void compareAmounts(ReconTransaction t) {
        if (t.getBankAmount().compareTo(t.getSystemAmount()) != 0) {
            t.setDiscrepancyType("AMOUNT_MISMATCH");
            t.setStatus(ReconStatus.AMOUNT_DIFF);
            BigDecimal diff = t.getBankAmount().subtract(t.getSystemAmount());
            t.setAmount(diff.abs());
        }
    }

    private void compareStatuses(ReconTransaction t) {
        // 假设从数据库获取内部状态
        String internalStatus = transactionService.getStatus(t.getInternalOrderNo());
        if (!"SETTLED".equals(internalStatus)) {
            t.setDiscrepancyType("STATUS_MISMATCH");
            t.setStatus(ReconStatus.STATUS_DIFF);
        }
    }
}
