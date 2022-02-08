package DataHolder;




import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InsertGroupMemberResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "insertGroupMemberResult"
})
@XmlRootElement(name = "InsertGroupMemberResponse")
public class InsertGroupMemberResponse {

    @XmlElement(name = "InsertGroupMemberResult")
    protected int insertGroupMemberResult;

    /**
     * Gets the value of the insertGroupMemberResult property.
     * 
     */
    public int getInsertGroupMemberResult() {
        return insertGroupMemberResult;
    }

    /**
     * Sets the value of the insertGroupMemberResult property.
     * 
     */
    public void setInsertGroupMemberResult(int value) {
        this.insertGroupMemberResult = value;
    }

}
