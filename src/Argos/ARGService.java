/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import DataHolder.createGroupLoan1;
import DataHolder.CustomerDetails;
import DataHolder.CreateGroupLoanResponse;
import DataHolder.AttachGroupMember;
import DataHolder.LoanDataByLoanIdResponse;
import DataHolder.CustomerData;
import DataHolder.CreateGroupLoan;
import DataHolder.LoanIDByGroupLoanNumber;
import DataHolder.UpdateGroupLoanResponce;
import DataHolder.LoanIDByGroupLoanNumberResponse;
import DataHolder.InsertGroupMemberResponse;
import DataHolder.LoanDataByLoanId;
import DataHolder.IndividualLoanUserData;
import DataHolder.IndividualLoanUserDataResponse;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author NJINU
 */
@WebService(serviceName = "ArgosService")
public class ARGService
{
    
    private TXProcessor tXProcessor = new TXProcessor();
    BRController brControler = new BRController();

    /**
     * Web service operation
     *
     * @param createGroupLoan
     * @return
     */
    @WebMethod(operationName = "createGroupLoan")
    public CreateGroupLoanResponse createGroupLoan(@WebParam(name = "createGroupLoan") createGroupLoan1 createGroupLoan)
    {
        CreateGroupLoanResponse createGroupLoanResponse = new CreateGroupLoanResponse();
        ArgosMain.brMeter.setValue(-400, "GL");
        try
        {
            createGroupLoanResponse = tXProcessor.createGroupLoan(createGroupLoan);
            
        }
        catch (Exception x)
        {
            ArgosMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
            
        }
        ArgosMain.brMeter.setValue(400, "GL");
        return createGroupLoanResponse;
    }

    /**
     * Web service operation
     *
     * @param LoanDataByLoanId
     * @return
     */
    @WebMethod(operationName = "getLoanDataByLoanId")
    public LoanDataByLoanIdResponse getLoanDataByLoanId(@WebParam(name = "getLoanDataByLoanId") LoanDataByLoanId LoanDataByLoanId)
    {
        LoanDataByLoanIdResponse getLoanDataByLoanIdResponse = new LoanDataByLoanIdResponse();
        ArgosMain.brMeter.setValue(-400, "LD");
        try
        {
            getLoanDataByLoanIdResponse = tXProcessor.getLoanDataByLoanId(LoanDataByLoanId);
            
        }
        catch (Exception x)
        {
            ArgosMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
            
        }
        ArgosMain.brMeter.setValue(400, "LD");
        return getLoanDataByLoanIdResponse;
    }

    /**
     * Web service operation
     *
     * @param LoanIDByGroupLoanNumber
     * @return
     */
    @WebMethod(operationName = "getLoanIDByGroupLoanNumber")
    public LoanIDByGroupLoanNumberResponse getLoanIDByGroupLoanNumber(@WebParam(name = "getLoanIDByGroupLoanNumber") LoanIDByGroupLoanNumber LoanIDByGroupLoanNumber)
    {
        LoanIDByGroupLoanNumberResponse byGroupLoanNumberResponse = new LoanIDByGroupLoanNumberResponse();
        ArgosMain.brMeter.setValue(-400, "LG");
        try
        {
            byGroupLoanNumberResponse = tXProcessor.getGroupLoanDataByLoanId(LoanIDByGroupLoanNumber);
            
        }
        catch (Exception x)
        {
            ArgosMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
            
        }
        ArgosMain.brMeter.setValue(400, "LG");
        return byGroupLoanNumberResponse;
    }

    /**
     * Web service operation
     *
     * @param insertGroupMember
     * @return
     */
    @WebMethod(operationName = "insertGroupMember")
    public InsertGroupMemberResponse insertGroupMember(@WebParam(name = "insertGroupMember") AttachGroupMember insertGroupMember)
    {
        InsertGroupMemberResponse groupMemberResponse = new InsertGroupMemberResponse();
        ArgosMain.brMeter.setValue(-400, "IM");
        try
        {
            System.err.println(">>>> " + insertGroupMember.getAccountNumber());
            groupMemberResponse = tXProcessor.insertGroupMember(insertGroupMember);
            
        }
        catch (Exception x)
        {
            ArgosMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
            
        }
        ArgosMain.brMeter.setValue(400, "IM");
        return groupMemberResponse;
    }

    /**
     * Web service operation
     *
     * @param getLoanData
     * @return
     */
    @WebMethod(operationName = "updateGroupLoanInfo")
    public UpdateGroupLoanResponce updateGroupLoanInfo(@WebParam(name = "updateGroupLoanInfo") CreateGroupLoan getLoanData)
    {
        UpdateGroupLoanResponce groupLoanResponce = new UpdateGroupLoanResponce();
        ArgosMain.brMeter.setValue(-400, "UL");
        try
        {
            groupLoanResponce = tXProcessor.updateGroupLoan(getLoanData);
            
        }
        catch (Exception x)
        {
            ArgosMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
            
        }
        ArgosMain.brMeter.setValue(400, "UL");
        return groupLoanResponce;
    }

    /**
     * Web service operation
     *
     * @param customerData
     * @return
     */
    @WebMethod(operationName = "getCustomerDetail")
    public CustomerDetails getCustomerDetail(@WebParam(name = "getCustomerDetail") CustomerData customerData)
    {
        CustomerDetails customerDetails = new CustomerDetails();
        ArgosMain.brMeter.setValue(-400, "CD");
        
        try
        {
            customerDetails = tXProcessor.getLoanData(customerData);
        }
        catch (Exception x)
        {
            ArgosMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
        }
        
        ArgosMain.brMeter.setValue(400, "CD");
        return customerDetails;
    }

    /**
     * Web service operation
     *
     * @param createIndividualLoan
     * @return
     */
    @WebMethod(operationName = "createIndividualLoan")
    public IndividualLoanUserDataResponse createIndividualLoan(@WebParam(name = "createIndividualLoan") IndividualLoanUserData createIndividualLoan)
    {
        IndividualLoanUserDataResponse loanUserDataResponse = new IndividualLoanUserDataResponse();
        ArgosMain.brMeter.setValue(-400, "IL");
        
        try
        {
            loanUserDataResponse = tXProcessor.createIndividualLoan(createIndividualLoan);
        }
        catch (Exception x)
        {
            ArgosMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
        }
        
        ArgosMain.brMeter.setValue(400, "IL");
        return loanUserDataResponse;
    }
}
