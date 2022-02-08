/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataHolder;

import java.math.BigDecimal;

/**
 *
 * @author NJINU
 */
public class IndividualLoanUserData
{

    private BigDecimal loan_amt;
    private String branch_no;
    private String rim_no;
    private String class_code;
    private Long loan_term;
    private String Credit_Officer_Code;
    private String contract_date;
    private String Sic_Code;
    private String SVAccountNumber;
    private String loan_term_Code;
   
  
    /**
     * @return the loan_amt
     */
    public BigDecimal getLoan_amt()
    {
        return loan_amt;
    }

    /**
     * @param loan_amt the loan_amt to set
     */
    public void setLoan_amt(BigDecimal loan_amt)
    {
        this.loan_amt = loan_amt;
    }

    /**
     * @return the branch_no
     */
    public String getBranch_no()
    {
        return branch_no;
    }

    /**
     * @param branch_no the branch_no to set
     */
    public void setBranch_no(String branch_no)
    {
        this.branch_no = branch_no;
    }

    /**
     * @return the rim_no
     */
    public String getRim_no()
    {
        return rim_no;
    }

    /**
     * @param rim_no the rim_no to set
     */
    public void setRim_no(String rim_no)
    {
        this.rim_no = rim_no;
    }

    /**
     * @return the class_code
     */
    public String getClass_code()
    {
        return class_code;
    }

    /**
     * @param class_code the class_code to set
     */
    public void setClass_code(String class_code)
    {
        this.class_code = class_code;
    }

    /**
     * @return the loan_term
     */
    public Long getLoan_term()
    {
        return loan_term;
    }

    /**
     * @param loan_term the loan_term to set
     */
    public void setLoan_term(Long loan_term)
    {
        this.loan_term = loan_term;
    }

    /**
     * @return the Credit_Officer_Code
     */
    public String getCredit_Officer_Code()
    {
        return Credit_Officer_Code;
    }

    /**
     * @param Credit_Officer_Code the Credit_Officer_Code to set
     */
    public void setCredit_Officer_Code(String Credit_Officer_Code)
    {
        this.Credit_Officer_Code = Credit_Officer_Code;
    }

    /**
     * @return the contract_date
     */
    public String getContract_date()
    {
        return contract_date;
    }

    /**
     * @param contract_date the contract_date to set
     */
    public void setContract_date(String contract_date)
    {
        this.contract_date = contract_date;
    }

    /**
     * @return the Sic_Code
     */
    public String getSic_Code()
    {
        return Sic_Code;
    }

    /**
     * @param Sic_Code the Sic_Code to set
     */
    public void setSic_Code(String Sic_Code)
    {
        this.Sic_Code = Sic_Code;
    }

    /**
     * @return the SVAccountNumber
     */
    public String getSVAccountNumber()
    {
        return SVAccountNumber;
    }

    /**
     * @param SVAccountNumber the SVAccountNumber to set
     */
    public void setSVAccountNumber(String SVAccountNumber)
    {
        this.SVAccountNumber = SVAccountNumber;
    }

    /**
     * @return the loan_term_Code
     */
    public String getLoan_term_Code()
    {
        return loan_term_Code;
    }

    /**
     * @param loan_term_Code the loan_term_Code to set
     */
    public void setLoan_term_Code(String loan_term_Code)
    {
        this.loan_term_Code = loan_term_Code;
    }

   

}
