/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataHolder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class LoanIDByGroupLoanList
{

    private String Account_number;
    private String LoanId;
    //private List

    /**
     * @return the Account_number
     */
    public String getAccount_number()
    {
        return Account_number;
    }

    /**
     * @param Account_number the Account_number to set
     */
    public void setAccount_number(String Account_number)
    {
        this.Account_number = Account_number;
    }

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
}
