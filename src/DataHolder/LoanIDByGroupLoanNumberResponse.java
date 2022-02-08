/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataHolder;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class LoanIDByGroupLoanNumberResponse
{

    private List<LoanIDByGroupLoanList> Acct_Loan;

    /**
     * @return the groupLoanData
     */
    public List<LoanIDByGroupLoanList> getAcct_Loan()
    {
        return Acct_Loan;
    }

    /**
     * @param Acct_Loan the groupLoanData to set
     */
    public void setAcct_Loan(List<LoanIDByGroupLoanList> Acct_Loan)
    {
        this.Acct_Loan = Acct_Loan;
    }

}
