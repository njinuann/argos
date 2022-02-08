/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataHolder;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class LoanDataByLoanIdResponse
{

    private String LoanId;
    private BigDecimal AmountFinanced;
    private BigDecimal CurrentBalance;
    private String Status;
    private BigDecimal PaidBack;
    private int NumberOfDayDelinquent;

    /**
     * @return the LoanId
     */
    public String getLoanId()
    {
        return LoanId;
    }

    /**
     * @param LoanId the LoanId to set
     */
    public void setLoanId(String LoanId)
    {
        this.LoanId = LoanId;
    }

    /**
     * @return the AmountFinanced
     */
    public BigDecimal getAmountFinanced()
    {
        return AmountFinanced;
    }

    /**
     * @param AmountFinanced the AmountFinanced to set
     */
    public void setAmountFinanced(BigDecimal AmountFinanced)
    {
        this.AmountFinanced = AmountFinanced;
    }

    /**
     * @return the CurrentBalance
     */
    public BigDecimal getCurrentBalance()
    {
        return CurrentBalance;
    }

    /**
     * @param CurrentBalance the CurrentBalance to set
     */
    public void setCurrentBalance(BigDecimal CurrentBalance)
    {
        this.CurrentBalance = CurrentBalance;
    }

    /**
     * @return the Status
     */
    public String getStatus()
    {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(String Status)
    {
        this.Status = Status;
    }

    /**
     * @return the PaidBack
     */
    public BigDecimal getPaidBack()
    {
        return PaidBack;
    }

    /**
     * @param PaidBack the PaidBack to set
     */
    public void setPaidBack(BigDecimal PaidBack)
    {
        this.PaidBack = PaidBack;
    }

    /**
     * @return the NumberOfDayDelinquent
     */
    public int getNumberOfDayDelinquent()
    {
        return NumberOfDayDelinquent;
    }

    /**
     * @param NumberOfDayDelinquent the NumberOfDayDelinquent to set
     */
    public void setNumberOfDayDelinquent(int NumberOfDayDelinquent)
    {
        this.NumberOfDayDelinquent = NumberOfDayDelinquent;
    }

}
