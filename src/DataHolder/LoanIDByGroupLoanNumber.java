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
public class LoanIDByGroupLoanNumber
{

    private String groupLoanNumber;

    /**
     * @return the groupLoanNumber
     */
    public String getGroupLoanNumber()
    {
        return groupLoanNumber;
    }

    /**
     * @param groupLoanNumber the groupLoanNumber to set
     */
    public void setGroupLoanNumber(String groupLoanNumber)
    {
        this.groupLoanNumber = groupLoanNumber;
    }

}
