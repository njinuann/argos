/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataHolder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateGroupLoanResponce
{
 
    
    protected String updateGroupLoanResult;

    public String getUpdateGroupLoanResult()
    {
        return updateGroupLoanResult;
    }

    public void setUpdateGroupLoanResult(String updateGroupLoanResult)
    {
        this.updateGroupLoanResult = updateGroupLoanResult;
    }
}
