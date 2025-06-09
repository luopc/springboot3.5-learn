package com.luopc.learn.bank.processor;

import com.luopc.learn.bank.model.BankTransaction;
import com.luopc.learn.bank.model.ReconciliationResult;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionMatchingProcessor implements
        ItemProcessor<List<BankTransaction>, ReconciliationResult> {

    @Override
    public ReconciliationResult process(List<BankTransaction> transactions) throws Exception {
        ReconciliationResult result = new ReconciliationResult();
        Map<String, BankTransaction> bankATransactions = new HashMap<>();
        Map<String, BankTransaction> bankBTransactions = new HashMap<>();

        // 分离银行A和银行B的交易
        for (BankTransaction transaction : transactions) {
            if ("BankA".equals(transaction.getBankName())) {
                bankATransactions.put(generateKey(transaction), transaction);
            } else if ("BankB".equals(transaction.getBankName())) {
                bankBTransactions.put(generateKey(transaction), transaction);
            }
        }

        // 匹配交易
        for (Map.Entry<String, BankTransaction> entry : bankATransactions.entrySet()) {
            String key = entry.getKey();
            BankTransaction bankA = entry.getValue();

            if (bankBTransactions.containsKey(key)) {
                result.addMatchedTransaction(bankA);
                bankBTransactions.remove(key);
            } else {
                result.addOnlyInBankA(bankA);
            }
        }

        // 添加只在银行B中的交易
        result.getOnlyInBankB().addAll(bankBTransactions.values());

        return result;
    }

    private String generateKey(BankTransaction transaction) {
        // 使用交易ID、金额和日期作为唯一标识
        return transaction.getTransactionId() + "|" +
                transaction.getAmount() + "|" +
                transaction.getTransactionDate();
    }
}