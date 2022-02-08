/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import com.neptunesoftware.supernova.ws.client.AccountWebService;
import com.neptunesoftware.supernova.ws.client.AccountWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.CreditAppWebService;
import com.neptunesoftware.supernova.ws.client.CreditAppWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.FundsTransferWebService;
import com.neptunesoftware.supernova.ws.client.FundsTransferWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.LoanAccountWebService;
import com.neptunesoftware.supernova.ws.client.LoanAccountWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.TransactionsWebService;
import com.neptunesoftware.supernova.ws.client.TransactionsWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.TxnProcessWebService;
import com.neptunesoftware.supernova.ws.client.TxnProcessWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.WorkflowWebService;
import com.neptunesoftware.supernova.ws.client.WorkflowWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.common.XAPIException;
import com.neptunesoftware.supernova.ws.common.XAPIRequestBaseObject;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceOutputData;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceRequest;
import com.neptunesoftware.supernova.ws.client.CustomerWebService;
import com.neptunesoftware.supernova.ws.client.CustomerWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.server.creditapp.data.CreditApplRequestData;
import com.neptunesoftware.supernova.ws.server.customer.data.CustomerBasicInformation;
import com.neptunesoftware.supernova.ws.server.customer.data.CustomerRequest;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.GroupLoanBeneficiaryOutputData;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.GroupLoanBeneficiaryRequestData;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.GroupLoanOutputData;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.GroupLoanRequestData;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.LoanAccountRequestData;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.LoanAccountSummaryOutputData;
import com.neptunesoftware.supernova.ws.server.workflow.data.WFItemOutputData;
import com.neptunesoftware.supernova.ws.server.workflow.data.WFViewRequestData;
import com.neptunesoftware.supernova.ws.server.workflow.data.WorkListRequestData;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 @author Njinu
 */
public final class TXClient
{

    private AccountWebService accountWebService;
    private LoanAccountWebService loanAccountWebService;
    private WorkflowWebService WorkflowWebService;
    private CustomerWebService customerWebService;
    private CreditAppWebService creditAppWebService;
    public int Counter;
    private Long contId;
    String SYSTEM_ERROR = "91";
    private XAPICaller xAPICaller;

    public TXClient(XAPICaller xaPICaller)
    {
        this.xAPICaller = xaPICaller;
        //  connectToCore();
    }

//    private void connectToCore()
//    {
//
//        try
//        {
//            Object[] coreBankingNodes = BRController.CoreBankingNodes.toArray();
//            if (coreBankingNodes.length > 0)
//            {
//                Arrays.sort(coreBankingNodes);
//                for (Object cBNode : coreBankingNodes)
//                {
//                    accountWebService = new AccountWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "AccountWebServiceBean?wsdl").getAccountWebServiceSoapPort();
////                    transactionsWebService = new TransactionsWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "TransactionsWebServiceBean?wsdl").getTransactionsWebServiceSoapPort();
////                    transferWebService = new FundsTransferWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "FundsTransferWebServiceBean?wsdl").getFundsTransferWebServiceSoapPort();
////                    processWebService = new TxnProcessWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "TxnProcessWebServiceBean?wsdl").getTxnProcessWebServiceSoapPort();
//                    loanAccountWebService = new LoanAccountWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "LoanAccountWebServiceBean?wsdl").getLoanAccountWebServiceSoapPort();
//                    WorkflowWebService = new WorkflowWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "WorkflowWebServiceBean?wsdl").getWorkflowWebServiceSoapPort();
//                    customerWebService = new CustomerWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "CustomerWebServiceBean?wsdl").getCustomerWebServiceSoapPort();
//                    creditAppWebService = new CreditAppWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "CreditAppWebServiceBean?wsdl").getCreditAppWebServiceSoapPort();
//                    if (isCoreConnected())
//                    {
//                        ((CBNode) cBNode).setCounter(((CBNode) cBNode).getCounter() + 1);
//                        contId = ((CBNode) cBNode).getCounter();
//                        initialize();
//                        break;
//                    }
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            ArgosMain.bRLogger.logError("connectToDB()-ERROR", ex);
//        }
//    }
    private <T> T getPort(Class<T> clazz)
    {
        T port = null;
        try
        {
            boolean errorFound = false;
            for (CBNode cBNode : BRController.getXapiNodes())
            {
                try
                {
                    switch (clazz.getSimpleName())
                    {
                        case "AccountWebService":
                            port = (T) (accountWebService == null ? accountWebService = new AccountWebServiceEndPointPort_Impl(((CBNode) cBNode).getContextUrl() + "AccountWebServiceBean?wsdl").getAccountWebServiceSoapPort(BRController.XapiUser.getBytes(), BRController.XapiPassword.getBytes()) : accountWebService);
                            break;
                        case "WorkflowWebService":
                            port = (T) (WorkflowWebService == null ? WorkflowWebService = new WorkflowWebServiceEndPointPort_Impl(((CBNode) cBNode).getContextUrl() + "WorkflowWebServiceBean?wsdl").getWorkflowWebServiceSoapPort(BRController.XapiUser.getBytes(), BRController.XapiPassword.getBytes()) : WorkflowWebService);
                            break;
                        case "CustomerWebService":
                            port = (T) (customerWebService == null ? customerWebService = new CustomerWebServiceEndPointPort_Impl(((CBNode) cBNode).getContextUrl() + "CustomerWebServiceBean?wsdl").getCustomerWebServiceSoapPort(BRController.XapiUser.getBytes(), BRController.XapiPassword.getBytes()) : customerWebService);
                            break;
                        case "LoanAccountWebService":
                            port = (T) (loanAccountWebService == null ? loanAccountWebService = new LoanAccountWebServiceEndPointPort_Impl(((CBNode) cBNode).getContextUrl() + "LoanAccountWebServiceBean?wsdl").getLoanAccountWebServiceSoapPort(BRController.XapiUser.getBytes(), BRController.XapiPassword.getBytes()) : loanAccountWebService);
                            break;
                        case "CreditAppWebService":
                            port = (T) (creditAppWebService == null ? creditAppWebService = new CreditAppWebServiceEndPointPort_Impl(((CBNode) cBNode).getContextUrl() + "CreditAppWebServiceBean?wsdl").getCreditAppWebServiceSoapPort(BRController.XapiUser.getBytes(), BRController.XapiPassword.getBytes()) : creditAppWebService);
                            break;
                    }
                    swapNodes(cBNode);
                    break;
                }
                catch (Exception ex)
                {
                    cBNode.setOnline(Boolean.FALSE);
                    ArgosMain.bRLogger.logError("connectService-ERROR", ex);
                    errorFound = true;
                }
            }
            if (errorFound)
            {
                sortArrayList(BRController.getXapiNodes(), false);
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("connectService>>ERROR", ex);
        }
        return port;
    }

    public void swapNodes(CBNode cBNode)
    {
        int y = BRController.getXapiNodes().indexOf(cBNode);
        for (int i = y + 1; i < BRController.getXapiNodes().size(); i++)
        {
            if (BRController.getXapiNodes().get(i).isOnline())
            {
                Collections.swap(BRController.getXapiNodes(), y, i);
                y = i;
            }
        }
    }

    public <T> ArrayList<T> sortArrayList(ArrayList<T> arrayList, boolean ascending)
    {
        Collections.sort(arrayList, ascending ? ((Comparator<T>) (T o1, T o2) -> ((Comparable) o1).compareTo(o2)) : ((Comparator<T>) (T o1, T o2) -> ((Comparable) o2).compareTo(o1)));
        return arrayList;
    }

//    private void checkCoreConnection()
//    {
//        if (!isCoreConnected())
//        {
//            connectToCore();
//        }
//    }
    private boolean isCoreConnected()
    {
        return accountWebService != null;
    }

    private void initialize()
    {
        TXRequest tXRequest = new TXRequest();
        tXRequest.setAccessCode(BRController.ChannelCode);
    }

    private XAPIRequestBaseObject getBaseRequest(XAPIRequestBaseObject requestData, TXRequest tXRequest)
    {
        //checkCoreConnection();
        requestData.setChannelId(BRController.ChannelID);
        requestData.setChannelCode(BRController.ChannelCode);

        requestData.setCardNumber(tXRequest.getAccessCode());
        requestData.setTransmissionTime(System.currentTimeMillis());

        requestData.setOriginatorUserId(BRController.SystemUserID);
        requestData.setTerminalNumber(BRController.ChannelCode);

        requestData.setReference(tXRequest.getReference());
        return requestData;
    }

    public Object queryDepositAccountBalance(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
            AccountBalanceRequest accountBalanceRequest = (AccountBalanceRequest) getBaseRequest(new AccountBalanceRequest(), tXRequest);
            accountBalanceRequest.setAccountNumber(tXRequest.getDebitAccount());

            accountBalanceRequest.setAccountNumber(tXRequest.getAccountNumber2());
            accountBalanceRequest.setChannelId(tXRequest.getChannelId());

            accountBalanceRequest.setChannelCode(tXRequest.getChannelCode());
            accountBalanceRequest.setOriginatorUserId(-99L);

            accountBalanceRequest.setReference(tXRequest.getReference());
            accountBalanceRequest.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("acctbalreq", accountBalanceRequest.toString());
            AccountBalanceOutputData response = getPort(AccountWebService.class).findAccountBalance(accountBalanceRequest);
            getxAPICaller().setCall("acctbalres", response.toString());
            return response;
        }
        catch (Exception ex)
        {
            System.err.println("" + ex);
            return ex;
        }
    }

    public Object createGroupLoan(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
            //checkCoreConnection();
            GroupLoanRequestData accrequest = new GroupLoanRequestData();
            accrequest = (GroupLoanRequestData) getBaseRequest(accrequest, tXRequest);

            accrequest.setGroupLoanId(tXRequest.getGroupLoanId());
            accrequest.setCustomerId(tXRequest.getCustomerId());

            accrequest.setLoanProductId(tXRequest.getLoanProductId());
            accrequest.setCustomerNo(tXRequest.getCustomerNo());

            accrequest.setCreditTypeId(tXRequest.getCreditTypeId());
            accrequest.setProductCombination(tXRequest.getProductCombination());

            accrequest.setLoanCrncyId(tXRequest.getLoanCrncyId());
            accrequest.setLoanAmount(tXRequest.getLoanAmount());

            accrequest.setLnStartDate(tXRequest.getLnStartDate());
            accrequest.setTermValue(tXRequest.getTermValue());

            accrequest.setTermCode(tXRequest.getTermCode());
            accrequest.setMaturityDate(tXRequest.getMaturityDate());

            accrequest.setRateType(tXRequest.getRateType());
            accrequest.setPurposeOfCredit(tXRequest.getPurposeOfCredit());

            accrequest.setBankOfficerId(tXRequest.getBankOfficerId());
            accrequest.setBusinessUnitId(tXRequest.getBusinessUnitId());

            accrequest.setPaymentType(tXRequest.getPaymentType());
            accrequest.setRepayFreqCd(tXRequest.getRepayFreqCd());

            accrequest.setRepayFreqValue(tXRequest.getRepayFreqValue());
            accrequest.setRepaymentStartDate(tXRequest.getRepaymentStartDate());

            accrequest.setDisbursementDate(tXRequest.getDisbursementDate());
            accrequest.setDisbursementMethod(tXRequest.getDisbursementMethod());

            accrequest.setReferenceNumber(tXRequest.getReferenceNumber());
            accrequest.setIndexRateId(tXRequest.getIndexRateId());

            accrequest.setIndustryId(tXRequest.getIndustryId());
            accrequest.setPortfolioId(tXRequest.getPortfolioId());

            accrequest.setChannelId(tXRequest.getChannelId());
            accrequest.setChannelCode(tXRequest.getChannelCode());

            accrequest.setUserLoginId(tXRequest.getUserLoginId());
            accrequest.setRepaymentMethod(tXRequest.getRepaymentMethod());

            accrequest.setMultipleDisbPerMember(tXRequest.getMultipleDisbPerMember());
            accrequest.setAutoCreateDisbursements(true);

            accrequest.setMoratoriumFreqCd("");
            accrequest.setApplChargeAcctType("MA");

            accrequest.setMemberLimit(tXRequest.getLoanAmtLimit());
            accrequest.setCurrBUId(tXRequest.getBusinessUnitId());

            accrequest.setUserRoleId(-99L);
            accrequest.setDisbursementChargeOption("NET_OFF");

            accrequest.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("groupLoanCreation", getxAPICaller().convertToString(accrequest));
            GroupLoanOutputData response = getPort(LoanAccountWebService.class).createGroupLoan(accrequest);
            getxAPICaller().setCall("groupLoanCreation", getxAPICaller().convertToString(response));
            System.out.println(response.getLoanStartDate());
            System.out.println(response.getGroupLoanId());
            System.out.println(response.getCreditApplRefNo());
            System.out.println(response.getCustomerName());
            System.out.println(response.getReferenceNumber());
            return response;
        }
        catch (Exception ex)
        {
            return ex;
        }
    }

    public Object attachGroupMembers(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
           // checkCoreConnection();
            GroupLoanBeneficiaryRequestData benrequest = new GroupLoanBeneficiaryRequestData();
            benrequest = (GroupLoanBeneficiaryRequestData) getBaseRequest(benrequest, tXRequest);

            benrequest.setMemberCustomerId(tXRequest.getCustomerId());
            benrequest.setMemberCustomerNo(tXRequest.getCustomerNo());

            benrequest.setMembershipId(tXRequest.getMembershipId());
            benrequest.setGroupLoanId(tXRequest.getGroupLoanId());

            benrequest.setLoanAmountCrncyId(tXRequest.getLoanCrncyId());
            benrequest.setLoanAmount(tXRequest.getLoanAmount());

            benrequest.setSettlementAcctId(tXRequest.getSettlementAcctId());
            benrequest.setLoanProductId(tXRequest.getLoanProductId());

            benrequest.setChannelId(tXRequest.getChannelId());
            benrequest.setChannelCode(tXRequest.getChannelCode());

            benrequest.setUserLoginId("SYSTEM");
            benrequest.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("attachBenefiaciary", getxAPICaller().convertToString(benrequest));
            GroupLoanBeneficiaryOutputData response = getPort(LoanAccountWebService.class).createGroupLoanBeneficiary(benrequest);
            getxAPICaller().setCall("attachBeneficiary", getxAPICaller().convertToString(response));

            System.err.println("" + response.getGroupLoanId());
            return response;
        }
        catch (RemoteException ex)
        {
            Logger.getLogger(TXClient.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
    }

    public Object updateGroupLoan(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
           // checkCoreConnection();
            GroupLoanRequestData accrequest = new GroupLoanRequestData();
            accrequest = (GroupLoanRequestData) getBaseRequest(accrequest, tXRequest);

            accrequest.setGroupLoanId(tXRequest.getGroupLoanId());
            accrequest.setCustomerId(tXRequest.getCustomerId());

            accrequest.setLoanProductId(tXRequest.getLoanProductId());
            accrequest.setCustomerNo(tXRequest.getCustomerNo());

            accrequest.setCreditTypeId(tXRequest.getCreditTypeId());
            accrequest.setProductCombination(tXRequest.getProductCombination());

            accrequest.setLoanCrncyId(tXRequest.getLoanCrncyId());
            accrequest.setLoanAmount(tXRequest.getLoanAmount());

            accrequest.setLnStartDate(tXRequest.getLnStartDate());
            accrequest.setTermValue(tXRequest.getTermValue());

            accrequest.setTermCode(tXRequest.getTermCode());
            accrequest.setMaturityDate(tXRequest.getMaturityDate());

            accrequest.setRateType(tXRequest.getRateType());
            accrequest.setPurposeOfCredit(tXRequest.getPurposeOfCredit());

            accrequest.setBankOfficerId(tXRequest.getBankOfficerId());
            accrequest.setBusinessUnitId(tXRequest.getBusinessUnitId());

            accrequest.setPaymentType(tXRequest.getPaymentType());
            accrequest.setRepayFreqCd(tXRequest.getRepayFreqCd());

            accrequest.setRepayFreqValue(tXRequest.getRepayFreqValue());
            accrequest.setRepaymentStartDate(tXRequest.getRepaymentStartDate());

            accrequest.setDisbursementDate(tXRequest.getDisbursementDate());
            accrequest.setDisbursementMethod(tXRequest.getDisbursementMethod());

            accrequest.setReferenceNumber(tXRequest.getReferenceNumber());
            accrequest.setIndexRateId(tXRequest.getIndexRateId());

            accrequest.setIndustryId(tXRequest.getIndustryId());
            accrequest.setPortfolioId(tXRequest.getPortfolioId());

            accrequest.setChannelId(tXRequest.getChannelId());
            accrequest.setChannelCode(tXRequest.getChannelCode());

            accrequest.setUserLoginId(tXRequest.getUserLoginId());
            accrequest.setRepaymentMethod(tXRequest.getRepaymentMethod());

            accrequest.setMultipleDisbPerMember(tXRequest.getMultipleDisbPerMember());
            accrequest.setAutoCreateDisbursements(true);

            accrequest.setMoratoriumFreqCd("");
            accrequest.setApplChargeAcctType("MA");

            accrequest.setMemberLimit(tXRequest.getLoanAmtLimit());
            accrequest.setCurrBUId(tXRequest.getBusinessUnitId());

            accrequest.setUserRoleId(-99L);
            accrequest.setDisbursementChargeOption("NET_OFF");

            accrequest.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("createWF", getxAPICaller().convertToString(accrequest));
            GroupLoanOutputData response = getPort(LoanAccountWebService.class).updateGroupLoan(accrequest);
            getxAPICaller().setCall("createWF", getxAPICaller().convertToString(response));
            System.out.println(response.getLoanStartDate());
            System.out.println(response.getGroupLoanId());

            return response;
        }
        catch (Exception ex)
        {
            return ex;
        }
    }

    public Object getWFItem(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
           // checkCoreConnection();
            WorkListRequestData accrequest = new WorkListRequestData();
            accrequest = (WorkListRequestData) getBaseRequest(accrequest, tXRequest);

            String[] listdata =
            {
                "com.neptunesoftware.supernova.service.grouploan.data.GroupLoanListData"
            };
            String[] categoryType =
            {
                "GROUPLOAN"
            };

            accrequest.setChannelId(8L);
            accrequest.setUserLoginId("SYSTEM");
            accrequest.setChannelCode("POS");
            accrequest.setReference(String.valueOf(System.currentTimeMillis()));
            accrequest.setAmount(tXRequest.getLoanAmount());
            accrequest.setSysUserId(-99L);
            accrequest.setUserRoleId(-99L);
            accrequest.setUserId(-99L);
            accrequest.setUserAccessCode("Argos999999999");
            accrequest.setQueueId(tXRequest.getQueueId());
            accrequest.setBusinessProcessCd("GPLA1");//may change
            accrequest.setOriginatorUserId(tXRequest.getBusinessUnitId());
            accrequest.setCurrBUId(tXRequest.getBusinessUnitId()); //maychange
            accrequest.setWorkItemClassName(listdata);
            accrequest.setBusinessProcWFCatCode(categoryType);
            accrequest.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("getWFItem", getxAPICaller().convertToString(accrequest));
            WFItemOutputData response = getPort(WorkflowWebService.class).getWorkList(accrequest);
            getxAPICaller().setCall("getWFItem", getxAPICaller().convertToString(response));
            System.err.println(">> Workflow submitted ");
            return response;
        }
        catch (Exception ex)
        {
            return ex;
        }
    }

    public Object saveGroupLoan(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
            //checkCoreConnection();
            WFViewRequestData accrequest = new WFViewRequestData();
            accrequest = (WFViewRequestData) getBaseRequest(accrequest, tXRequest);

            accrequest.setWorkItemId(tXRequest.getWorkListItem());
            accrequest.setEventId(new Long(1009695));

            accrequest.setChannelId(new Long(8));
            accrequest.setChannelCode("POS");

            accrequest.setUserLoginId("NEPTUNE");
            accrequest.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("approveGrpLnWFItem", getxAPICaller().convertToString(accrequest));
            getPort(WorkflowWebService.class).saveGroupLoanData(accrequest);
            getxAPICaller().setCall("approveGrpLnWFItem", "00");
            System.out.println("00 - out");

            return "00";
        }
        catch (Exception ex)
        {
            return ex;
        }
    }

    public Object queryCustomerData(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
            CustomerRequest requestData = new CustomerRequest();
            requestData = (CustomerRequest) getBaseRequest(requestData, tXRequest);

            requestData.setChannelId(tXRequest.getChannelId());
            requestData.setChannelCode(tXRequest.getChannelCode());
            requestData.setUserLoginId("SYSTEM");
            requestData.setUserId(-99L);
            requestData.setCustomerNumber(tXRequest.getCustomerNo());
            requestData.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("dptogltfrreq", getxAPICaller().convertToString(requestData));
            CustomerBasicInformation response = getPort(CustomerWebService.class).findCustomerBasicInformation(requestData);
            getxAPICaller().setCall("dptogltfrres", getxAPICaller().convertToString(response));
            return response;
        }
        catch (Exception ex)
        {
            System.err.println("" + ex);
            return ex;
        }
    }

    public boolean createCreditApplication(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        Calendar cal = Calendar.getInstance();
        Long acctID = tXRequest.getAccountId();//create variable for account id
        try
        {
            CreditApplRequestData creditApplRequestData = new CreditApplRequestData();
            creditApplRequestData = (CreditApplRequestData) getBaseRequest(creditApplRequestData, tXRequest);

            creditApplRequestData.setChannelId(tXRequest.getChannelId());
            creditApplRequestData.setApplAvailableAmount(tXRequest.getTxnAmount());
            creditApplRequestData.setApprovedAmount(tXRequest.getTxnAmount());
            creditApplRequestData.setAmount(tXRequest.getTxnAmount());
            creditApplRequestData.setApprovedCurrencyId(tXRequest.getCurrencyId());
            creditApplRequestData.setApprovedTermCode(tXRequest.getTermCode());
            creditApplRequestData.setApprovedTermValue(tXRequest.getTermValue());
            creditApplRequestData.setBuId(tXRequest.getBusinessUnitId());
            creditApplRequestData.setBusinessUnitId(tXRequest.getBusinessUnitId());
            creditApplRequestData.setCurrBUId(tXRequest.getBusinessUnitId());
            creditApplRequestData.setBankOfficerId(tXRequest.getBankOfficerId());
            creditApplRequestData.setCrUtilizationMethodCode("SINGLEDISB");
            creditApplRequestData.setCreditTypeId(tXRequest.getCreditTypeId());
            creditApplRequestData.setCurrencyId(tXRequest.getCurrencyId());
            creditApplRequestData.setCreditPortfolioId(tXRequest.getPortfolioId());
            creditApplRequestData.setCustomerNumber(tXRequest.getCustomerNo());
            creditApplRequestData.setDepositAcctId(tXRequest.getAccountId());
            creditApplRequestData.setProductCombinationOption("SING_PROD");
            creditApplRequestData.setStrFromDate(tXRequest.getLnStartDate());
            creditApplRequestData.setStrRequiredDate(tXRequest.getLnStartDate());
            creditApplRequestData.setTermCode(tXRequest.getTermCode());
            creditApplRequestData.setTermValue(tXRequest.getTermValue());
            creditApplRequestData.setProductId(tXRequest.getProduct_id());
            creditApplRequestData.setPurposeOfCreditId(tXRequest.getPurposeOfCredit());
            creditApplRequestData.setStrApplicationDate(tXRequest.getLnStartDate());
            creditApplRequestData.setReferenceNumber(tXRequest.getReference());
            creditApplRequestData.setRepaySourceAcctId(acctID);
            creditApplRequestData.setRequiredDate(cal);
            creditApplRequestData.setStrToDate(tXRequest.getMaturityDate());
            creditApplRequestData.setStrExpiryDate(tXRequest.getMaturityDate());
            creditApplRequestData.setValidXapiRequest(true);
            creditApplRequestData.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("createcreditappreq", getxAPICaller().convertToString(creditApplRequestData));
            getPort(CreditAppWebService.class).createCreditApplication(creditApplRequestData);
            getxAPICaller().setCall("createcreditappres", Boolean.TRUE);
            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public Object createAndApproveLoanAccount(TXRequest tXRequest)
    {
        getxAPICaller().setConnectionID(String.valueOf(contId));
        try
        {
          //  checkCoreConnection();
            LoanAccountRequestData loanAccountRequestData = new LoanAccountRequestData();
            loanAccountRequestData = (LoanAccountRequestData) getBaseRequest(loanAccountRequestData, tXRequest);

            loanAccountRequestData.setAccountName(tXRequest.getCustomerName());
            loanAccountRequestData.setChannelId(tXRequest.getChannelId());
            loanAccountRequestData.setAutoReclassifyFlag("Y");
            loanAccountRequestData.setApplId(tXRequest.getApplID());   //get application id
            loanAccountRequestData.setCurrencyId(tXRequest.getCurrencyId());
            loanAccountRequestData.setCustomerId(tXRequest.getCustomerId());
            loanAccountRequestData.setCapitalisedEventDueDateOption("AMORTISE");
            loanAccountRequestData.setDisbChargeSettlement("NET_OFF");
            loanAccountRequestData.setDisbursementLimit(tXRequest.getLoanAmount());
            loanAccountRequestData.setDisbrsmntSetlmntAcctId(tXRequest.getAccountId());
            loanAccountRequestData.setLoanFeeAccountId(tXRequest.getAccountId());
            loanAccountRequestData.setMainBranchId(tXRequest.getBusinessUnitId());
            loanAccountRequestData.setPortfolioId(tXRequest.getPortfolioId());
            loanAccountRequestData.setPrimaryOfficerId(tXRequest.getBankOfficerId());
            loanAccountRequestData.setProductId(tXRequest.getProduct_id());
            loanAccountRequestData.setStartDate(tXRequest.getLnStartDate().trim());
            loanAccountRequestData.setRiskClassId(551L);
            loanAccountRequestData.setTermCode(tXRequest.getTermCode());
            loanAccountRequestData.setTermValue(tXRequest.getTermValue().intValue());
            loanAccountRequestData.setMultiCurrency(false);
            loanAccountRequestData.setTransmissionTime(System.currentTimeMillis());

            getxAPICaller().setCall("createindvloanreq", getxAPICaller().convertToString(loanAccountRequestData));
            LoanAccountSummaryOutputData loanAccountSummaryOutputData = getPort(LoanAccountWebService.class).createAndActivateLoanAccount(loanAccountRequestData);
            System.err.println(loanAccountSummaryOutputData.getAccountNo());
            getxAPICaller().setCall("createindvloanres", getxAPICaller().convertToString(loanAccountSummaryOutputData));
            return loanAccountSummaryOutputData;
        }
        catch (Exception ex)
        {
            return ex;
        }
    }

    public String getXapiErrorCode(Exception ex)
    {
        System.err.println("error message " + getTagValue(String.valueOf(ex), "error-code"));
        String errorCode = SYSTEM_ERROR;
        if (ex instanceof XAPIException)
        {
            System.out.println("exception is an instance ");
            if (((XAPIException) ex).getErrors() != null && "91".equals(errorCode))
            {
                errorCode = ((XAPIException) ex).getErrors().length >= 1 ? ((XAPIException) ex).getErrors()[0].getErrorCode() : errorCode;
            }
        }
        return errorCode;
    }

    public String getTagValue(String xml, String tagName)
    {
        return xml.split("<" + tagName + ">")[1].split("</" + tagName + ">")[0];
    }

//    /**
//     * @return the tXProcessor
//     */
//    public TXProcessor gettXProcessor()
//    {
//        return tXProcessor;
//    }
//
//    /**
//     * @param tXProcessor the tXProcessor to set
//     */
//    public void settXProcessor(TXProcessor tXProcessor)
//    {
//        this.tXProcessor = tXProcessor;
//    }
    /**
     @return the xAPICaller
     */
    public XAPICaller getxAPICaller()
    {
        return xAPICaller;
    }

    /**
     @param xAPICaller the xAPICaller to set
     */
    public void setxAPICaller(XAPICaller xAPICaller)
    {
        this.xAPICaller = xAPICaller;
    }
}
