/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataHolder;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseRequest {

    public String reference;
    public Long channelId;
    public BigDecimal chargeAmount;
    public String chargeNarration;
    public BigDecimal taxAmount;
    public String taxNarration;
    public String taxLedger;
    public String chargeLedger;
    public String utilityType;
    public String fromAccount;
    public String toAccount;
    public String currency;
    public BigDecimal txnAmt;
    public String accountPmtType;
    public String accessCode;
    public String tranNarration;   

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

   
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getChargeNarration() {
        return chargeNarration;
    }

    public void setChargeNarration(String chargeNarration) {
        this.chargeNarration = chargeNarration;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxNarration() {
        return taxNarration;
    }

    public void setTaxNarration(String taxNarration) {
        this.taxNarration = taxNarration;
    }

    public String getTaxLedger() {
        return taxLedger;
    }

    public void setTaxLedger(String taxLedger) {
        this.taxLedger = taxLedger;
    }

   

    public String getChargeLedger() {
        return chargeLedger;
    }

    public void setChargeLedger(String chargeLedger) {
        this.chargeLedger = chargeLedger;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(BigDecimal txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getUtilityType() {
        return utilityType;
    }

    public void setUtilityType(String utilityType) {
        this.utilityType = utilityType;
    }

    public String getAccountPmtType() {
        return accountPmtType;
    }

    public void setAccountPmtType(String accountPmtType) {
        this.accountPmtType = accountPmtType;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getTranNarration() {
        return tranNarration;
    }

    public void setTranNarration(String tranNarration) {
        this.tranNarration = tranNarration;
    }
    
    
}
