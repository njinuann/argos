/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 *
 * @author Pecherk
 */
public class TXRequest
{


    private String reference;
    private String accountNumber1;
    private String accountNumber2;
    private String currencyCode;
    private BigDecimal txnAmount;
    private BigDecimal chargeAmount;
    private BigDecimal taxAmount;
    private String txnNarration;
    private String chargeNarration;
    private String chargeDebitAccount;
    private String chargeCreditLedger;
    private String taxCreditLedger;
    private String taxNarration;
    private String channelContraLedger;
    private String toCurrencyCode;
    private Long channelId;

//======group loan ============
    private String DebitAccount;
    private String CreditAccount;
    private String SystemUserID;
    private String AccessCode;
    private String TxnDate;
    private Long groupLoanId;
    private String channelCode;
    private Long CustomerId;
    private Long LoanProductId;
    private String CustomerNo;
    private Long CreditTypeId;
    private String ProductCombination;
    private Long LoanCrncyId;
    private BigDecimal LoanAmount;
    private BigDecimal loanAmtLimit;
    private String LnStartDate;
    private Long TermValue;
    private String TermCode;
    private String MaturityDate;
    private String RateType;
    private Long PurposeOfCredit;
    private Long BankOfficerId;
    private Long BusinessUnitId;
    private String PaymentType;
    private String RepayFreqCd;
    private Long RepayFreqValue;
    private String RepaymentMethod;
    //public  Long RepaymentPoolAcctId;
    private String RepaymentStartDate;
    private String DisbursementDate;
    private String DisbursementMethod;
    private String ReferenceNumber;
    private Long IndexRateId;
    private Long IndustryId;
    private Long PortfolioId;
    private Long ChannelId;
    private String ChannelCode;
    private String UserLoginId;
    private String SupervisorId;
    private Long RepaymentPoolAcctId;
    private String MultipleDisbPerMember;
    private Long TransmissionTime;
    private Long SettlementAcctId;
    private Long MembershipId;
    private String grpLoanReference;
    private Long workListItem;
    private Long queueId;

    //====individual 
    private Long currencyId;
    private Long accountId;
    private Long product_id;
    private String customerName;
    private Long applID;
        /**
     * @return the grouoLoanId
     */
    public Long getGroupLoanId()
    {
        return groupLoanId;
    }

    /**
     * @param aGrouoLoanId the grouoLoanId to set
     */
    public void setGroupLoanId(Long aGroupLoanId)
    {
        groupLoanId = aGroupLoanId;
    }

    /**
     * @return the CustomerId
     */
    public Long getCustomerId()
    {
        return CustomerId;
    }

    /**
     * @param aCustomerId the CustomerId to set
     */
    public void setCustomerId(Long aCustomerId)
    {
        CustomerId = aCustomerId;
    }

    /**
     * @return the LoanProductId
     */
    public Long getLoanProductId()
    {
        return LoanProductId;
    }

    /**
     * @param aLoanProductId the LoanProductId to set
     */
    public void setLoanProductId(Long aLoanProductId)
    {
        LoanProductId = aLoanProductId;
    }

    /**
     * @return the CustomerNo
     */
    public String getCustomerNo()
    {
        return CustomerNo;
    }

    /**
     * @param aCustomerNo the CustomerNo to set
     */
    public void setCustomerNo(String aCustomerNo)
    {
        CustomerNo = aCustomerNo;
    }

    /**
     * @return the CreditTypeId
     */
    public Long getCreditTypeId()
    {
        return CreditTypeId;
    }

    /**
     * @param aCreditTypeId the CreditTypeId to set
     */
    public void setCreditTypeId(Long aCreditTypeId)
    {
        CreditTypeId = aCreditTypeId;
    }

    /**
     * @return the ProductCombination
     */
    public String getProductCombination()
    {
        return ProductCombination;
    }

    /**
     * @param aProductCombination the ProductCombination to set
     */
    public void setProductCombination(String aProductCombination)
    {
        ProductCombination = aProductCombination;
    }

    /**
     * @return the LoanCrncyId
     */
    public Long getLoanCrncyId()
    {
        return LoanCrncyId;
    }

    /**
     * @param aLoanCrncyId the LoanCrncyId to set
     */
    public void setLoanCrncyId(Long aLoanCrncyId)
    {
        LoanCrncyId = aLoanCrncyId;
    }

    /**
     * @return the LoanAmount
     */
    public BigDecimal getLoanAmount()
    {
        return LoanAmount;
    }

    /**
     * @param aLoanAmount the LoanAmount to set
     */
    public void setLoanAmount(BigDecimal aLoanAmount)
    {
        LoanAmount = aLoanAmount;
    }

    /**
     * @return the LnStartDate
     */
    public String getLnStartDate()
    {
        return LnStartDate;
    }

    /**
     * @param aLnStartDate the LnStartDate to set
     */
    public void setLnStartDate(String aLnStartDate)
    {
        LnStartDate = aLnStartDate;
    }

    /**
     * @return the TermValue
     */
    public Long getTermValue()
    {
        return TermValue;
    }

    /**
     * @param aTermValue the TermValue to set
     */
    public void setTermValue(Long aTermValue)
    {
        TermValue = aTermValue;
    }

    /**
     * @return the TermCode
     */
    public String getTermCode()
    {
        return TermCode;
    }

    /**
     * @param aTermCode the TermCode to set
     */
    public void setTermCode(String aTermCode)
    {
        TermCode = aTermCode;
    }

    /**
     * @return the MaturityDate
     */
    public String getMaturityDate()
    {
        return MaturityDate;
    }

    /**
     * @param aMaturityDate the MaturityDate to set
     */
    public void setMaturityDate(String aMaturityDate)
    {
        MaturityDate = aMaturityDate;
    }

    /**
     * @return the RateType
     */
    public String getRateType()
    {
        return RateType;
    }

    /**
     * @param aRateType the RateType to set
     */
    public void setRateType(String aRateType)
    {
        RateType = aRateType;
    }

    /**
     * @return the PurposeOfCredit
     */
    public Long getPurposeOfCredit()
    {
        return PurposeOfCredit;
    }

    /**
     * @param aPurposeOfCredit the PurposeOfCredit to set
     */
    public void setPurposeOfCredit(Long aPurposeOfCredit)
    {
        PurposeOfCredit = aPurposeOfCredit;
    }

    /**
     * @return the BankOfficerId
     */
    public Long getBankOfficerId()
    {
        return BankOfficerId;
    }

    /**
     * @param aBankOfficerId the BankOfficerId to set
     */
    public void setBankOfficerId(Long aBankOfficerId)
    {
        BankOfficerId = aBankOfficerId;
    }

    /**
     * @return the BusinessUnitId
     */
    public Long getBusinessUnitId()
    {
        return BusinessUnitId;
    }

    /**
     * @param aBusinessUnitId the BusinessUnitId to set
     */
    public void setBusinessUnitId(Long aBusinessUnitId)
    {
        BusinessUnitId = aBusinessUnitId;
    }

    /**
     * @return the PaymentType
     */
    public String getPaymentType()
    {
        return PaymentType;
    }

    /**
     * @param aPaymentType the PaymentType to set
     */
    public void setPaymentType(String aPaymentType)
    {
        PaymentType = aPaymentType;
    }

    /**
     * @return the RepayFreqCd
     */
    public String getRepayFreqCd()
    {
        return RepayFreqCd;
    }

    /**
     * @param aRepayFreqCd the RepayFreqCd to set
     */
    public void setRepayFreqCd(String aRepayFreqCd)
    {
        RepayFreqCd = aRepayFreqCd;
    }

    /**
     * @return the RepayFreqValue
     */
    public Long getRepayFreqValue()
    {
        return RepayFreqValue;
    }

    /**
     * @param aRepayFreqValue the RepayFreqValue to set
     */
    public void setRepayFreqValue(Long aRepayFreqValue)
    {
        RepayFreqValue = aRepayFreqValue;
    }

    /**
     * @return the RepaymentMethod
     */
    public String getRepaymentMethod()
    {
        return RepaymentMethod;
    }

    /**
     * @param aRepaymentMethod the RepaymentMethod to set
     */
    public void setRepaymentMethod(String aRepaymentMethod)
    {
        RepaymentMethod = aRepaymentMethod;
    }

    /**
     * @return the RepaymentStartDate
     */
    public String getRepaymentStartDate()
    {
        return RepaymentStartDate;
    }

    /**
     * @param aRepaymentStartDate the RepaymentStartDate to set
     */
    public void setRepaymentStartDate(String aRepaymentStartDate)
    {
        RepaymentStartDate = aRepaymentStartDate;
    }

    /**
     * @return the DisbursementDate
     */
    public String getDisbursementDate()
    {
        return DisbursementDate;
    }

    /**
     * @param aDisbursementDate the DisbursementDate to set
     */
    public void setDisbursementDate(String aDisbursementDate)
    {
        DisbursementDate = aDisbursementDate;
    }

    /**
     * @return the ReferenceNumber
     */
    public String getReferenceNumber()
    {
        return ReferenceNumber;
    }

    /**
     * @param aReferenceNumber the ReferenceNumber to set
     */
    public void setReferenceNumber(String aReferenceNumber)
    {
        ReferenceNumber = aReferenceNumber;
    }

    /**
     * @return the IndexRateId
     */
    public Long getIndexRateId()
    {
        return IndexRateId;
    }

    /**
     * @param aIndexRateId the IndexRateId to set
     */
    public void setIndexRateId(Long aIndexRateId)
    {
        IndexRateId = aIndexRateId;
    }

    /**
     * @return the IndustryId
     */
    public Long getIndustryId()
    {
        return IndustryId;
    }

    /**
     * @param aIndustryId the IndustryId to set
     */
    public void setIndustryId(Long aIndustryId)
    {
        IndustryId = aIndustryId;
    }

    /**
     * @return the PortfolioId
     */
    public Long getPortfolioId()
    {
        return PortfolioId;
    }

    /**
     * @param aPortfolioId the PortfolioId to set
     */
    public void setPortfolioId(Long aPortfolioId)
    {
        PortfolioId = aPortfolioId;
    }

    /**
     * @return the ChannelId
     */
    public Long getChannelId()
    {
        return ChannelId;
    }

    /**
     * @param aChannelId the ChannelId to set
     */
    public void setChannelId(Long aChannelId)
    {
        ChannelId = aChannelId;
    }

    /**
     * @return the ChannelCode
     */
    public String getChannelCode()
    {
        return ChannelCode;
    }

    /**
     * @param aChannelCode the ChannelCode to set
     */
    public void setChannelCode(String aChannelCode)
    {
        ChannelCode = aChannelCode;
    }

    /**
     * @return the UserLoginId
     */
    public String getUserLoginId()
    {
        return UserLoginId;
    }

    /**
     * @param aUserLoginId the UserLoginId to set
     */
    public void setUserLoginId(String aUserLoginId)
    {
        UserLoginId = aUserLoginId;
    }

    /**
     * @return the RepaymentPoolAcctId
     */
    public Long getRepaymentPoolAcctId()
    {
        return RepaymentPoolAcctId;
    }

    /**
     * @param aRepaymentPoolAcctId the RepaymentPoolAcctId to set
     */
    public void setRepaymentPoolAcctId(Long aRepaymentPoolAcctId)
    {
        RepaymentPoolAcctId = aRepaymentPoolAcctId;
    }

    /**
     * @return the MultipleDisbPerMember
     */
    public String getMultipleDisbPerMember()
    {
        return MultipleDisbPerMember;
    }

    /**
     * @param aMultipleDisbPerMember the MultipleDisbPerMember to set
     */
    public void setMultipleDisbPerMember(String aMultipleDisbPerMember)
    {
        MultipleDisbPerMember = aMultipleDisbPerMember;
    }

    /**
     * @return the TransmissionTime
     */
    public Long getTransmissionTime()
    {
        return TransmissionTime;
    }

    /**
     * @param aTransmissionTime the TransmissionTime to set
     */
    public void setTransmissionTime(Long aTransmissionTime)
    {
        TransmissionTime = aTransmissionTime;
    }

    /**
     * @return the DebitAccount
     */
    public String getDebitAccount()
    {
        return DebitAccount;
    }

    /**
     * @param DebitAccount the DebitAccount to set
     */
    public void setDebitAccount(String DebitAccount)
    {
        this.DebitAccount = DebitAccount;
    }

    /**
     * @return the CreditAccount
     */
    public String getCreditAccount()
    {
        return CreditAccount;
    }

    /**
     * @param CreditAccount the CreditAccount to set
     */
    public void setCreditAccount(String CreditAccount)
    {
        this.CreditAccount = CreditAccount;
    }

    /**
     * @return the SystemUserID
     */
    public String getSystemUserID()
    {
        return SystemUserID;
    }

    /**
     * @param SystemUserID the SystemUserID to set
     */
    public void setSystemUserID(String SystemUserID)
    {
        this.SystemUserID = SystemUserID;
    }

    /**
     * @return the AccessCode
     */
    public String getAccessCode()
    {
        return AccessCode;
    }

    /**
     * @param AccessCode the AccessCode to set
     */
    public void setAccessCode(String AccessCode)
    {
        this.AccessCode = AccessCode;
    }

    /**
     * @return the TxnDate
     */
    public String getTxnDate()
    {
        return TxnDate;
    }

    /**
     * @param TxnDate the TxnDate to set
     */
    public void setTxnDate(String TxnDate)
    {
        this.TxnDate = TxnDate;
    }

    /**
     * @return the reference
     */
    public String getReference()
    {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference)
    {
        this.reference = reference;
    }

    /**
     * @return the accountNumber1
     */
    public String getAccountNumber1()
    {
        return accountNumber1;
    }

    /**
     * @param accountNumber1 the accountNumber1 to set
     */
    public void setAccountNumber1(String accountNumber1)
    {
        this.accountNumber1 = accountNumber1;
    }

    /**
     * @return the accountNumber2
     */
    public String getAccountNumber2()
    {
        return accountNumber2;
    }

    /**
     * @param accountNumber2 the accountNumber2 to set
     */
    public void setAccountNumber2(String accountNumber2)
    {
        this.accountNumber2 = accountNumber2;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode()
    {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    /**
     * @return the txnAmount
     */
    public BigDecimal getTxnAmount()
    {
        return txnAmount;
    }

    /**
     * @param txnAmount the txnAmount to set
     */
    public void setTxnAmount(BigDecimal txnAmount)
    {
        this.txnAmount = txnAmount;
    }

    /**
     * @return the chargeAmount
     */
    public BigDecimal getChargeAmount()
    {
        return chargeAmount;
    }

    /**
     * @param chargeAmount the chargeAmount to set
     */
    public void setChargeAmount(BigDecimal chargeAmount)
    {
        this.chargeAmount = chargeAmount;
    }

    /**
     * @return the taxAmount
     */
    public BigDecimal getTaxAmount()
    {
        return taxAmount;
    }

    /**
     * @param taxAmount the taxAmount to set
     */
    public void setTaxAmount(BigDecimal taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    /**
     * @return the txnNarration
     */
    public String getTxnNarration()
    {
        return txnNarration;
    }

    /**
     * @param txnNarration the txnNarration to set
     */
    public void setTxnNarration(String txnNarration)
    {
        this.txnNarration = txnNarration;
    }

    /**
     * @return the chargeNarration
     */
    public String getChargeNarration()
    {
        return chargeNarration;
    }

    /**
     * @param chargeNarration the chargeNarration to set
     */
    public void setChargeNarration(String chargeNarration)
    {
        this.chargeNarration = chargeNarration;
    }

    /**
     * @return the chargeDebitAccount
     */
    public String getChargeDebitAccount()
    {
        return chargeDebitAccount;
    }

    /**
     * @param chargeDebitAccount the chargeDebitAccount to set
     */
    public void setChargeDebitAccount(String chargeDebitAccount)
    {
        this.chargeDebitAccount = chargeDebitAccount;
    }

    /**
     * @return the chargeCreditLedger
     */
    public String getChargeCreditLedger()
    {
        return chargeCreditLedger;
    }

    /**
     * @param chargeCreditLedger the chargeCreditLedger to set
     */
    public void setChargeCreditLedger(String chargeCreditLedger)
    {
        this.chargeCreditLedger = chargeCreditLedger;
    }

    /**
     * @return the taxCreditLedger
     */
    public String getTaxCreditLedger()
    {
        return taxCreditLedger;
    }

    /**
     * @param taxCreditLedger the taxCreditLedger to set
     */
    public void setTaxCreditLedger(String taxCreditLedger)
    {
        this.taxCreditLedger = taxCreditLedger;
    }

    /**
     * @return the taxNarration
     */
    public String getTaxNarration()
    {
        return taxNarration;
    }

    /**
     * @param taxNarration the taxNarration to set
     */
    public void setTaxNarration(String taxNarration)
    {
        this.taxNarration = taxNarration;
    }

    /**
     * @return the channelContraLedger
     */
    public String getChannelContraLedger()
    {
        return channelContraLedger;
    }

    /**
     * @param channelContraLedger the channelContraLedger to set
     */
    public void setChannelContraLedger(String channelContraLedger)
    {
        this.channelContraLedger = channelContraLedger;
    }

    /**
     * @return the toCurrencyCode
     */
    public String getToCurrencyCode()
    {
        return toCurrencyCode;
    }

    /**
     * @param toCurrencyCode the toCurrencyCode to set
     */
    public void setToCurrencyCode(String toCurrencyCode)
    {
        this.toCurrencyCode = toCurrencyCode;
    }

    /**
     * @return the DisbursementMethod
     */
    public String getDisbursementMethod()
    {
        return DisbursementMethod;
    }

    /**
     * @param DisbursementMethod the DisbursementMethod to set
     */
    public void setDisbursementMethod(String DisbursementMethod)
    {
        this.DisbursementMethod = DisbursementMethod;
    }

    /**
     * @return the SettlementAcctId
     */
    public Long getSettlementAcctId()
    {
        return SettlementAcctId;
    }

    /**
     * @param SettlementAcctId the SettlementAcctId to set
     */
    public void setSettlementAcctId(Long SettlementAcctId)
    {
        this.SettlementAcctId = SettlementAcctId;
    }

    /**
     * @return the MembershipId
     */
    public Long getMembershipId()
    {
        return MembershipId;
    }

    /**
     * @param MembershipId the MembershipId to set
     */
    public void setMembershipId(Long MembershipId)
    {
        this.MembershipId = MembershipId;
    }

    public String getGrpLoanReference()
    {
        return grpLoanReference;
    }

    public void setGrpLoanReference(String grpLoanReference)
    {
        this.grpLoanReference = grpLoanReference;
    }

    public Long getWorkListItem()
    {
        return workListItem;
    }

    public void setWorkListItem(Long workListItem)
    {
        this.workListItem = workListItem;
    }

    public Long getQueueId()
    {
        return queueId;
    }

    public void setQueueId(Long queueId)
    {
        this.queueId = queueId;
    }

    public BigDecimal getLoanAmtLimit()
    {
        return loanAmtLimit;
    }

    public void setLoanAmtLimit(BigDecimal loanAmtLimit)
    {
        this.loanAmtLimit = loanAmtLimit;
    }

    public String getSupervisorId()
    {
        return SupervisorId;
    }

    public void setSupervisorId(String SupervisorId)
    {
        this.SupervisorId = SupervisorId;
    }

    /**
     * @return the currencyId
     */
    public Long getCurrencyId()
    {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(Long currencyId)
    {
        this.currencyId = currencyId;
    }

    /**
     * @return the accountId
     */
    public Long getAccountId()
    {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    /**
     * @return the product_id
     */
    public Long getProduct_id()
    {
        return product_id;
    }

    /**
     * @param product_id the product_id to set
     */
    public void setProduct_id(Long product_id)
    {
        this.product_id = product_id;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    /**
     * @return the applID
     */
    public Long getApplID()
    {
        return applID;
    }

    /**
     * @param applID the applID to set
     */
    public void setApplID(Long applID)
    {
        this.applID = applID;
    }

}
