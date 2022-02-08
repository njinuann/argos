package DataHolder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ApprovalParameter
{

    public Long GRP_LOAN_ID;
    public String GRP_LOAN_REF;
    public String ACCESS_CODE;
    public Long GRP_CUST_ID;
    public Long LOAN_PROD_ID;
    public String GRP_CUST_NO;
    public String GRP_ACCT_NO;
    public Long GRP_CR_TY;
    public String GRP_PROD_COMBINATION;
    public Long GRP_CRNCY_ID;
    public BigDecimal LOAN_AMOUNT;
    public String LOAN_START_DATE;
    public String REPMNT_START_DATE;
    public Long TERM_VALUE;
    public String TERM_CODE;
    public String MAT_DATE;
    public String RATE_TY;
    public String PURPOSE_CR;
    public Long OFFICER_ID;
    public Long BU_ID;
    public String PMT_TY;
    public String REPAY_FREQ_CD;
    public Long REPAY_FREQ_VAL;
    public String DISB_DATE;
    public String DISB_METHOD;
    public String REF_NO;
    public Long INDEX_RATE_ID;
    public Long INDUSTRY_ID;
    public Long PORTFOLIO_ID;
    public Long CHANNEL_ID;
    public String CHANNEL_CODE;
    public String USER_ID;
    public String REPMNT_METHOD;
    public String MULTIPLE_DISB_PM;
    public Long TRAMSN_TIME;
    public String TXN_NARRATION;
    public String SYS_USER_ID;

    public Long getGRP_LOAN_ID()
    {
        return GRP_LOAN_ID;
    }

    public void setGRP_LOAN_ID(Long GRP_LOAN_ID)
    {
        this.GRP_LOAN_ID = GRP_LOAN_ID;
    }

    public String getACCESS_CODE()
    {
        return ACCESS_CODE;
    }

    public void setACCESS_CODE(String ACCESS_CODE)
    {
        this.ACCESS_CODE = ACCESS_CODE;
    }

    public Long getGRP_CUST_ID()
    {
        return GRP_CUST_ID;
    }

    public void setGRP_CUST_ID(Long GRP_CUST_ID)
    {
        this.GRP_CUST_ID = GRP_CUST_ID;
    }

    public Long getLOAN_PROD_ID()
    {
        return LOAN_PROD_ID;
    }

    public void setLOAN_PROD_ID(Long LOAN_PROD_ID)
    {
        this.LOAN_PROD_ID = LOAN_PROD_ID;
    }

    public String getGRP_CUST_NO()
    {
        return GRP_CUST_NO;
    }

    public void setGRP_CUST_NO(String GRP_CUST_NO)
    {
        this.GRP_CUST_NO = GRP_CUST_NO;
    }

    public String getGRP_ACCT_NO()
    {
        return GRP_ACCT_NO;
    }

    public void setGRP_ACCT_NO(String GRP_ACCT_NO)
    {
        this.GRP_ACCT_NO = GRP_ACCT_NO;
    }

    public Long getGRP_CR_TY()
    {
        return GRP_CR_TY;
    }

    public void setGRP_CR_TY(Long GRP_CR_TY)
    {
        this.GRP_CR_TY = GRP_CR_TY;
    }

    public String getGRP_PROD_COMBINATION()
    {
        return GRP_PROD_COMBINATION;
    }

    public void setGRP_PROD_COMBINATION(String GRP_PROD_COMBINATION)
    {
        this.GRP_PROD_COMBINATION = GRP_PROD_COMBINATION;
    }

    public Long getGRP_CRNCY_ID()
    {
        return GRP_CRNCY_ID;
    }

    public void setGRP_CRNCY_ID(Long GRP_CRNCY_ID)
    {
        this.GRP_CRNCY_ID = GRP_CRNCY_ID;
    }

    public BigDecimal getLOAN_AMOUNT()
    {
        return LOAN_AMOUNT;
    }

    public void setLOAN_AMOUNT(BigDecimal LOAN_AMOUNT)
    {
        this.LOAN_AMOUNT = LOAN_AMOUNT;
    }

    public String getLOAN_START_DATE()
    {
        return LOAN_START_DATE;
    }

    public void setLOAN_START_DATE(String LOAN_START_DATE)
    {
        this.LOAN_START_DATE = LOAN_START_DATE;
    }

    public String getREPMNT_START_DATE()
    {
        return REPMNT_START_DATE;
    }

    public void setREPMNT_START_DATE(String REPMNT_START_DATE)
    {
        this.REPMNT_START_DATE = REPMNT_START_DATE;
    }

    public Long getTERM_VALUE()
    {
        return TERM_VALUE;
    }

    public void setTERM_VALUE(Long TERM_VALUE)
    {
        this.TERM_VALUE = TERM_VALUE;
    }

    public String getTERM_CODE()
    {
        return TERM_CODE;
    }

    public void setTERM_CODE(String TERM_CODE)
    {
        this.TERM_CODE = TERM_CODE;
    }

    public String getMAT_DATE()
    {
        return MAT_DATE;
    }

    public void setMAT_DATE(String MAT_DATE)
    {
        this.MAT_DATE = MAT_DATE;
    }

    public String getRATE_TY()
    {
        return RATE_TY;
    }

    public void setRATE_TY(String RATE_TY)
    {
        this.RATE_TY = RATE_TY;
    }

    public String getPURPOSE_CR()
    {
        return PURPOSE_CR;
    }

    public void setPURPOSE_CR(String PURPOSE_CR)
    {
        this.PURPOSE_CR = PURPOSE_CR;
    }

    public Long getOFFICER_ID()
    {
        return OFFICER_ID;
    }

    public void setOFFICER_ID(Long OFFICER_ID)
    {
        this.OFFICER_ID = OFFICER_ID;
    }

    public Long getBU_ID()
    {
        return BU_ID;
    }

    public void setBU_ID(Long BU_ID)
    {
        this.BU_ID = BU_ID;
    }

    public String getPMT_TY()
    {
        return PMT_TY;
    }

    public void setPMT_TY(String PMT_TY)
    {
        this.PMT_TY = PMT_TY;
    }

    public String getREPAY_FREQ_CD()
    {
        return REPAY_FREQ_CD;
    }

    public void setREPAY_FREQ_CD(String REPAY_FREQ_CD)
    {
        this.REPAY_FREQ_CD = REPAY_FREQ_CD;
    }

    public Long getREPAY_FREQ_VAL()
    {
        return REPAY_FREQ_VAL;
    }

    public void setREPAY_FREQ_VAL(Long REPAY_FREQ_VAL)
    {
        this.REPAY_FREQ_VAL = REPAY_FREQ_VAL;
    }

    public String getDISB_DATE()
    {
        return DISB_DATE;
    }

    public void setDISB_DATE(String DISB_DATE)
    {
        this.DISB_DATE = DISB_DATE;
    }

    public String getDISB_METHOD()
    {
        return DISB_METHOD;
    }

    public void setDISB_METHOD(String DISB_METHOD)
    {
        this.DISB_METHOD = DISB_METHOD;
    }

    public String getREF_NO()
    {
        return REF_NO;
    }

    public void setREF_NO(String REF_NO)
    {
        this.REF_NO = REF_NO;
    }

    public Long getINDEX_RATE_ID()
    {
        return INDEX_RATE_ID;
    }

    public void setINDEX_RATE_ID(Long INDEX_RATE_ID)
    {
        this.INDEX_RATE_ID = INDEX_RATE_ID;
    }

    public Long getINDUSTRY_ID()
    {
        return INDUSTRY_ID;
    }

    public void setINDUSTRY_ID(Long INDUSTRY_ID)
    {
        this.INDUSTRY_ID = INDUSTRY_ID;
    }

    public Long getPORTFOLIO_ID()
    {
        return PORTFOLIO_ID;
    }

    public void setPORTFOLIO_ID(Long PORTFOLIO_ID)
    {
        this.PORTFOLIO_ID = PORTFOLIO_ID;
    }

    public Long getCHANNEL_ID()
    {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(Long CHANNEL_ID)
    {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public String getCHANNEL_CODE()
    {
        return CHANNEL_CODE;
    }

    public void setCHANNEL_CODE(String CHANNEL_CODE)
    {
        this.CHANNEL_CODE = CHANNEL_CODE;
    }

    public String getUSER_ID()
    {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID)
    {
        this.USER_ID = USER_ID;
    }

    public String getREPMNT_METHOD()
    {
        return REPMNT_METHOD;
    }

    public void setREPMNT_METHOD(String REPMNT_METHOD)
    {
        this.REPMNT_METHOD = REPMNT_METHOD;
    }

    public String getMULTIPLE_DISB_PM()
    {
        return MULTIPLE_DISB_PM;
    }

    public void setMULTIPLE_DISB_PM(String MULTIPLE_DISB_PM)
    {
        this.MULTIPLE_DISB_PM = MULTIPLE_DISB_PM;
    }

    public Long getTRAMSN_TIME()
    {
        return TRAMSN_TIME;
    }

    public void setTRAMSN_TIME(Long TRAMSN_TIME)
    {
        this.TRAMSN_TIME = TRAMSN_TIME;
    }

    public String getTXN_NARRATION()
    {
        return TXN_NARRATION;
    }

    public void setTXN_NARRATION(String TXN_NARRATION)
    {
        this.TXN_NARRATION = TXN_NARRATION;
    }

    public String getSYS_USER_ID()
    {
        return SYS_USER_ID;
    }

    public void setSYS_USER_ID(String SYS_USER_ID)
    {
        this.SYS_USER_ID = SYS_USER_ID;
    }

    public String getGRP_LOAN_REF()
    {
        return GRP_LOAN_REF;
    }

    public void setGRP_LOAN_REF(String GRP_LOAN_REF)
    {
        this.GRP_LOAN_REF = GRP_LOAN_REF;
    }

    
}
