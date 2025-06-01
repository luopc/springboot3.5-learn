package com.luopc.learn.api.user;

import lombok.Data;

@Data
public class Counterparty implements Trader {
    private String traderId;
    private String traderName;
    private String traderCode;
    private String traderFullName;
    private String traderType;
}