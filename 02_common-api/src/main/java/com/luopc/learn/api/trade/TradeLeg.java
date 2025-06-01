package com.luopc.learn.api.trade;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Robin
 */

public interface TradeLeg {

    String getLegId();

    void setLegId(String letId);

    String getTradeId();

    void setTradeId(String tradeId);

    int getLegRole();

    void setLegRole(int legRole);

    Integer getVersionId();

    void setVersionId(Integer versionId);

    String getSideType();

    void setSideType(String sideType);

    BigDecimal getPriAmt();

    void setPriAmt(BigDecimal priAmt);

    String getPriCcy();

    void setPriCcy(String priCcy);

    BigDecimal getCntAmt();

    void setCntAmt(BigDecimal cntAmt);

    String getCntCcy();

    String getCcyPair();

    void setCcyPair(String ccyPair);

    void setCntCcy(String cntCcy);

    LocalDate getValueDate();

    void setValueDate(LocalDate valueDate);

    BigDecimal getStrike();

    void setStrike(BigDecimal strike);

    String getUti();

    void setUti(String uti);

    String getRegulatoryKey();

    void setRegulatoryKey(String regulatoryKey);

    String getDownStreamId();

    void setDownStreamId(String downStreamId);
}
