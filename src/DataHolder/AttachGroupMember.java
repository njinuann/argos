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
public class AttachGroupMember
{
    public String AccountNumber;
    public BigDecimal MemberLoanAmount;
    public String GroupLoanNo;
    public String SicCode;

    public String getAccountNumber()
    {
        return AccountNumber;
    }

    public void setAccountNumber(String AccountNumber)
    {
        this.AccountNumber = AccountNumber;
    }

    public BigDecimal getMemberLoanAmount()
    {
        return MemberLoanAmount;
    }

    public void setMemberLoanAmount(BigDecimal MemberLoanAmount)
    {
        this.MemberLoanAmount = MemberLoanAmount;
    }

    public String getGroupLoanNo()
    {
        return GroupLoanNo;
    }

    public void setGroupLoanNo(String GroupLoanNo)
    {
        this.GroupLoanNo = GroupLoanNo;
    }

    public String getSicCode()
    {
        return SicCode;
    }

    public void setSicCode(String SicCode)
    {
        this.SicCode = SicCode;
    }

}
