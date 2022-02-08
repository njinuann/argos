package DataHolder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateGroupLoanResponse")
public class CreateGroupLoanResponse extends BaseResponse
{

    protected String createGroupLoanResult;

    public String getCreateGroupLoanResult()
    {
        return createGroupLoanResult;
    }

    public void setCreateGroupLoanResult(String createGroupLoanResult)
    {
        this.createGroupLoanResult = createGroupLoanResult;
    }

}
