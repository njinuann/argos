package DataHolder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author NJINU
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerData
{

    public String acct_no;

    public String getAcct_no()
    {
        return acct_no;
    }

    public void setAcct_no(String acct_no)
    {
        this.acct_no = acct_no;
    }

}
