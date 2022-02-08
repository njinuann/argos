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
public class LoanParameter
{
  private Long prodId;
  private Long crncyId;
  private String RateType;
  private Long indexRateId;
  private String pmtType;
  private String repmtMethodtype;
  private Long repmtFreqValue;
  private String repmtFreqCode;
  private String multiDisbFg;
  private String termFreqcode;
  private Long termFreqValue;
  private Long creditTypeId;

    /**
     * @return the prodId
     */
    public Long getProdId()
    {
        return prodId;
    }

    /**
     * @param prodId the prodId to set
     */
    public void setProdId(Long prodId)
    {
        this.prodId = prodId;
    }

    /**
     * @return the crncyId
     */
    public Long getCrncyId()
    {
        return crncyId;
    }

    /**
     * @param crncyId the crncyId to set
     */
    public void setCrncyId(Long crncyId)
    {
        this.crncyId = crncyId;
    }

    /**
     * @return the RateType
     */
    public String getRateType()
    {
        return RateType;
    }

    /**
     * @param RateType the RateType to set
     */
    public void setRateType(String RateType)
    {
        this.RateType = RateType;
    }

    /**
     * @return the indexRateId
     */
    public Long getIndexRateId()
    {
        return indexRateId;
    }

    /**
     * @param indexRateId the indexRateId to set
     */
    public void setIndexRateId(Long indexRateId)
    {
        this.indexRateId = indexRateId;
    }

    /**
     * @return the pmtType
     */
    public String getPmtType()
    {
        return pmtType;
    }

    /**
     * @param pmtType the pmtType to set
     */
    public void setPmtType(String pmtType)
    {
        this.pmtType = pmtType;
    }

    /**
     * @return the repmtMethodtype
     */
    public String getRepmtMethodtype()
    {
        return repmtMethodtype;
    }

    /**
     * @param repmtMethodtype the repmtMethodtype to set
     */
    public void setRepmtMethodtype(String repmtMethodtype)
    {
        this.repmtMethodtype = repmtMethodtype;
    }

    /**
     * @return the repmtFreqValue
     */
    public Long getRepmtFreqValue()
    {
        return repmtFreqValue;
    }

    /**
     * @param repmtFreqValue the repmtFreqValue to set
     */
    public void setRepmtFreqValue(Long repmtFreqValue)
    {
        this.repmtFreqValue = repmtFreqValue;
    }

    /**
     * @return the repmtFreqCode
     */
    public String getRepmtFreqCode()
    {
        return repmtFreqCode;
    }

    /**
     * @param repmtFreqCode the repmtFreqCode to set
     */
    public void setRepmtFreqCode(String repmtFreqCode)
    {
        this.repmtFreqCode = repmtFreqCode;
    }

    /**
     * @return the multiDisbFg
     */
    public String getMultiDisbFg()
    {
        return multiDisbFg;
    }

    /**
     * @param multiDisbFg the multiDisbFg to set
     */
    public void setMultiDisbFg(String multiDisbFg)
    {
        this.multiDisbFg = multiDisbFg;
    }

    /**
     * @return the termFreqcode
     */
    public String getTermFreqcode()
    {
        return termFreqcode;
    }

    /**
     * @param termFreqcode the termFreqcode to set
     */
    public void setTermFreqcode(String termFreqcode)
    {
        this.termFreqcode = termFreqcode;
    }

    /**
     * @return the termFreqValue
     */
    public Long getTermFreqValue()
    {
        return termFreqValue;
    }

    /**
     * @param termFreqValue the termFreqValue to set
     */
    public void setTermFreqValue(Long termFreqValue)
    {
        this.termFreqValue = termFreqValue;
    }

    /**
     * @return the creditTypeId
     */
    public Long getCreditTypeId()
    {
        return creditTypeId;
    }

    /**
     * @param creditTypeId the creditTypeId to set
     */
    public void setCreditTypeId(Long creditTypeId)
    {
        this.creditTypeId = creditTypeId;
    }
}
