package com.luopc.learn.bank.service;

import org.springframework.stereotype.Component;

@Component
public class TransactionService {
    public String getStatus(String internalOrderNo) {
        return "SUCCESS";
    }
}
