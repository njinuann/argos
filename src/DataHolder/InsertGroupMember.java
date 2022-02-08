package DataHolder;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class InsertGroupMember
{

    public String accountNumber;
    public int memberLoanAmount;
    public String groupLoanNo;
    public String sicCode;

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public int getMemberLoanAmount()
    {
        return memberLoanAmount;
    }

    public void setMemberLoanAmount(int memberLoanAmount)
    {
        this.memberLoanAmount = memberLoanAmount;
    }

    public String getGroupLoanNo()
    {
        return groupLoanNo;
    }

    public void setGroupLoanNo(String groupLoanNo)
    {
        this.groupLoanNo = groupLoanNo;
    }

    public String getSicCode()
    {
        return sicCode;
    }

    public void setSicCode(String sicCode)
    {
        this.sicCode = sicCode;
    }

}
