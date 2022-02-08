package DataHolder;




import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerDetails {

  
    public String accountNumber; 
    public String firstName;    
    public String middleName;  
    public String lastName;    
    public String birthday;
    public String street;    
    public String houseNumber;    
    public String neighbourhood;  
    public String communityTerritory;   
    public String freeAddressField;   
    public String homePhoneNumber;    
    public String mobilePhoneNumber;    
    public String savingsBalance;    
    public String province;    
    public String sicCode;    
    public String birthPlace;    
    public String birthProvince;    
    public String gender;    
    public String businessPhone;   
    public String idNumber;   
    public String identificationType;   
    public String branchName;
    private BigDecimal TDPleage;
    private BigDecimal previousLoanAmount;
    private Long applId;

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getHouseNumber()
    {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
    }

    public String getNeighbourhood()
    {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood)
    {
        this.neighbourhood = neighbourhood;
    }

    public String getCommunityTerritory()
    {
        return communityTerritory;
    }

    public void setCommunityTerritory(String communityTerritory)
    {
        this.communityTerritory = communityTerritory;
    }

    public String getFreeAddressField()
    {
        return freeAddressField;
    }

    public void setFreeAddressField(String freeAddressField)
    {
        this.freeAddressField = freeAddressField;
    }

    public String getHomePhoneNumber()
    {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber)
    {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getMobilePhoneNumber()
    {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber)
    {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getSavingsBalance()
    {
        return savingsBalance;
    }

    public void setSavingsBalance(String savingsBalance)
    {
        this.savingsBalance = savingsBalance;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getSicCode()
    {
        return sicCode;
    }

    public void setSicCode(String sicCode)
    {
        this.sicCode = sicCode;
    }

    public String getBirthPlace()
    {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace)
    {
        this.birthPlace = birthPlace;
    }

    public String getBirthProvince()
    {
        return birthProvince;
    }

    public void setBirthProvince(String birthProvince)
    {
        this.birthProvince = birthProvince;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getBusinessPhone()
    {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone)
    {
        this.businessPhone = businessPhone;
    }

    public String getIdNumber()
    {
        return idNumber;
    }

    public void setIdNumber(String idNumber)
    {
        this.idNumber = idNumber;
    }

    public String getIdentificationType()
    {
        return identificationType;
    }

    public void setIdentificationType(String identificationType)
    {
        this.identificationType = identificationType;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    /**
     * @return the TDPleage
     */
    public BigDecimal getTDPleage()
    {
        return TDPleage;
    }

    /**
     * @param TDPleage the TDPleage to set
     */
    public void setTDPleage(BigDecimal TDPleage)
    {
        this.TDPleage = TDPleage;
    }

    /**
     * @return the previousLoanAmount
     */
    public BigDecimal getPreviousLoanAmount()
    {
        return previousLoanAmount;
    }

    /**
     * @param previousLoanAmount the previousLoanAmount to set
     */
    public void setPreviousLoanAmount(BigDecimal previousLoanAmount)
    {
        this.previousLoanAmount = previousLoanAmount;
    }

    /**
     * @return the applId
     */
    public Long getApplId()
    {
        return applId;
    }

    /**
     * @param applId the applId to set
     */
    public void setApplId(Long applId)
    {
        this.applId = applId;
    }

    
    
   }
