package com.luopc.learn.bank.processor;


import com.luopc.learn.bank.model.ReconciliationResult;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReconciliationResultWriter implements ItemWriter<ReconciliationResult> {



    @Override
    public void write(Chunk<? extends ReconciliationResult> items) throws Exception {

            ReconciliationResult result = items.getItems().getFirst(); // 只有一个结果

            System.out.println("\n===== 银行交易对账结果 =====");
            System.out.println("匹配成功的交易数量: " + result.getMatchedTransactions().size());
            System.out.println("仅在银行A中的交易数量: " + result.getOnlyInBankA().size());
            System.out.println("仅在银行B中的交易数量: " + result.getOnlyInBankB().size());

            System.out.println("\n匹配成功的交易:");
            result.getMatchedTransactions().forEach(tx ->
                    System.out.printf("交易ID: %s, 金额: %s, 日期: %s%n",
                            tx.getTransactionId(), tx.getAmount(), tx.getTransactionDate()));

            System.out.println("\n仅在银行A中的交易:");
            result.getOnlyInBankA().forEach(tx ->
                    System.out.printf("交易ID: %s, 金额: %s, 日期: %s%n",
                            tx.getTransactionId(), tx.getAmount(), tx.getTransactionDate()));

            System.out.println("\n仅在银行B中的交易:");
            result.getOnlyInBankB().forEach(tx ->
                    System.out.printf("交易ID: %s, 金额: %s, 日期: %s%n",
                            tx.getTransactionId(), tx.getAmount(), tx.getTransactionDate()));
        }
}
