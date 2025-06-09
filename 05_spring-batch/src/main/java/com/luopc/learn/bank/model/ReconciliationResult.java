package com.luopc.learn.bank.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReconciliationResult {
    private List<BankTransaction> matchedTransactions = new ArrayList<>();
    private List<BankTransaction> onlyInBankA = new ArrayList<>();
    private List<BankTransaction> onlyInBankB = new ArrayList<>();

    public void addMatchedTransaction(BankTransaction transaction) {
        matchedTransactions.add(transaction);
    }

    public void addOnlyInBankA(BankTransaction transaction) {
        onlyInBankA.add(transaction);
    }

    public void addOnlyInBankB(BankTransaction transaction) {
        onlyInBankB.add(transaction);
    }
}
