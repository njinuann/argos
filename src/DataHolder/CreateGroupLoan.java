package DataHolder;




import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateGroupLoan")
public class CreateGroupLoan {

        public String groupLoanRefNo;

    public String getGroupLoanRefNo()
    {
        return groupLoanRefNo;
    }

    public void setGroupLoanRefNo(String groupLoanRefNo)
    {
        this.groupLoanRefNo = groupLoanRefNo;
    }    

}
