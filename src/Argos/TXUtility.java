/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import DataHolder.ApprovalParameter;
import DataHolder.LoanIDByGroupLoanList;
import DataHolder.LoanDataByLoanIdResponse;
import DataHolder.GroupMemberParameter;
import DataHolder.LoanIDByGroupLoanNumberResponse;
import DataHolder.CustomerDetails;
import DataHolder.LoanParameter;
import static Argos.ArgosMain.brMeter;
import static Argos.BRController.JdbcDriverName;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JComboBox;

/**
 *
 * @author Pecherk
 */
public final class TXUtility
{

    int conIndex = -1;
    long startTime = 0;
    private XAPICaller xapiCaller = new XAPICaller();
    private Connection dbConnection = null;
    private CallableStatement logTxnStatement = null;
    private CallableStatement logTaxTxn = null;
    private String txnJournalId, chargeJournal, respCode = "00";
    private  Long DefaultBuId = null;
    private  final String successResponse = "00";
//    private  final String invalidError = "91";
    public java.util.Date timestamp = new java.util.Date();
    public SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
    public SimpleDateFormat STformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat callformatter = new SimpleDateFormat("dd MMM yyyy");
    public SimpleDateFormat taxDtformat = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat procDtformat = new SimpleDateFormat("dd/MM/yyyy");

    public Long numberOfDays = 7L;
//    private final String success = "0";
//    private final String ConError = "1";
//    private final String invalidAcct = "2";
//    private final String memberExists = "3";
//    private final String amountGreater = "4";

    public TXUtility()
    {
        BRController.initialize();
    }

    public TXUtility(XAPICaller xapiCaller)
    {
        setXapiCaller(new XAPICaller());
    }

    private void connectToDB()
    {
        try
        {
            Class.forName(JdbcDriverName);
            setDbConnection(DriverManager.getConnection(BRController.CMSchemaJdbcUrl, BRController.CMSchemaName, BRController.CMSchemaPassword));

            setLogTxnStatement(getDbConnection().prepareCall("{call " + BRController.CMSchemaName + ".PSP_EX_LOG_BILLS_TXN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"));
            setLogTaxTxn(getDbConnection().prepareCall("{call " + BRController.CMSchemaName + ".PSP_EX_LOG_TAX_TXN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"));
            brMeter.setConnected(true);

        }
        catch (ClassNotFoundException | SQLException ex)
        {
            ArgosMain.bRLogger.logError("connectToDB()-ERROR", ex);
        }
    }

    private void LogEventMessage()
    {
        ArgosMain.bRLogger.logEvent(xapiCaller.toString());
    }

//    /* Argos WS*/
//    public CreateGroupLoanResponse createGroupLoan(createGroupLoan1 acctBalReq)
//    {
//        setXapiCaller(new XAPICaller());
//        startTime = System.currentTimeMillis();
//        Long grpLoanId, grpLoanNo;
//        ArgosMain.bRLogger.logEvent("Create Group Loan Request", convertToString(acctBalReq));
//
//        CreateGroupLoanResponse createGroupLoanResponse = new CreateGroupLoanResponse();
//        TXRequest tXRequest = new TXRequest();
//
//        TXClient tXClient = new TXClient(getXapiCaller());
//        LoanParameter groupLoanParameter;
//
//        groupLoanParameter = queryProductDetails(acctBalReq.getClass_code());
//        if (!"".equalsIgnoreCase(acctBalReq.getGroup_RIM()))
//        {
//            if (!isCustomerValid(acctBalReq.getGroup_RIM()))
//            {
//                createGroupLoanResponse.setCreateGroupLoanResult(invalidAcct);
//                ArgosMain.bRLogger.logEvent("<Response>", convertToString(createGroupLoanResponse));
//                return createGroupLoanResponse;
//            }
//        }
//        else
//        {
//            createGroupLoanResponse.setCreateGroupLoanResult(invalidAcct);
//            ArgosMain.bRLogger.logEvent("<Response>", convertToString(createGroupLoanResponse));
//
//            return createGroupLoanResponse;
//        }
//
//        tXRequest.setAccessCode("ArgosInterface99999");
//        tXRequest.setCustomerId(getCustomerId(acctBalReq.getGroup_RIM().trim()));
//
//        tXRequest.setLoanProductId(groupLoanParameter.getProdId());
//        tXRequest.setCustomerNo(acctBalReq.getGroup_RIM().trim());
//
//        tXRequest.setAccountNumber1(acctBalReq.getGroupe_account().trim());
//        tXRequest.setCreditTypeId(new Long(acctBalReq.getCredit_type()));//new Long(779)
//
//        tXRequest.setProductCombination("SING_PROD");//constant
//        tXRequest.setLoanCrncyId(groupLoanParameter.getCrncyId());
//
//        tXRequest.setLoanAmount(new BigDecimal(acctBalReq.getLoan_amount()));
//        tXRequest.setLnStartDate(acctBalReq.getContract_date());
//        tXRequest.setRepaymentStartDate(acctBalReq.getFirst_payment_date());
//
//        tXRequest.setTermValue(Long.parseLong(acctBalReq.getLoan_trm()));
//        tXRequest.setTermCode(groupLoanParameter.getTermFreqcode());
//
//        tXRequest.setLoanAmtLimit(new BigDecimal(acctBalReq.getLoan_amt_limit()));
//        tXRequest.setMaturityDate(getMaturityDate(acctBalReq.getContract_date(), Long.parseLong(acctBalReq.getLoan_trm())));                                // compute based on product setings
//
//        tXRequest.setRateType(groupLoanParameter.getRateType());
//        tXRequest.setSupervisorId(acctBalReq.getSurpvisor_code());
//
//        tXRequest.setPurposeOfCredit(new Long(447));
//        /*has been defaulted awaitng the new argos release to include this parameter*/
//
//        tXRequest.setBankOfficerId(getBankOfficerId(acctBalReq.getCredit_Officer_Code()));
//
//        tXRequest.setBusinessUnitId(geBuid(acctBalReq.getBranch_no()));
//        tXRequest.setPaymentType(groupLoanParameter.getPmtType());
//
//        tXRequest.setRepayFreqCd(groupLoanParameter.getRepmtFreqCode());
//        tXRequest.setRepayFreqValue(groupLoanParameter.getRepmtFreqValue());
//
//        tXRequest.setDisbursementDate(acctBalReq.getContract_date());
//        tXRequest.setDisbursementMethod("MA"); //
//
//        tXRequest.setReferenceNumber(acctBalReq.getDescription());
//        tXRequest.setIndexRateId(groupLoanParameter.getIndexRateId());
//        tXRequest.setIndustryId(2420L);
//        /* tXRequest.setIndustryId(Long.valueOf(acctBalReq.getIndustry_code()));//29 jan 2019/ this was again defaulted on request from Daddy to industry id =2420
//                                                                                                                                    industry code =40009
//                                                                                                                                    industry desc =Grossiste, commerce general*/
// /*has been defaulted awaitng the new argos release to include this parameter*/
////19th march 2018 >> previously mapped to 1980
//        tXRequest.setPortfolioId(new Long(11));
//        tXRequest.setChannelId(new Long(8));
//
//        tXRequest.setChannelCode("POS");
//        tXRequest.setUserLoginId(acctBalReq.getCredit_Officer_Code());
//
//        tXRequest.setRepaymentMethod("MA");
//        tXRequest.setMultipleDisbPerMember("S");
//
//        tXRequest.setTransmissionTime(System.currentTimeMillis());
//        tXRequest.setTxnNarration(acctBalReq.getDescription());
//        tXRequest.setSystemUserID(acctBalReq.getSurpvisor_code());
//
//        getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
//        getXapiCaller().setCurrency(tXRequest.getCurrencyId());
//        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
//        getXapiCaller().setRefNumber(tXRequest.getCustomerNo());
//        getXapiCaller().setTxnDescription("Group Loan Creation [" + tXRequest.getReferenceNumber() + "]");
//        getXapiCaller().setMainRequest(acctBalReq);
//        getXapiCaller().settXRequest(tXRequest);
//
//        ArgosMain.bRLogger.logEvent("<TXRequest>", convertToString(tXRequest));
//        System.err.println("isGroupLoanPending(tXRequest) " + isGroupLoanPending(tXRequest));
//        if (isGroupLoanPending(tXRequest))
//        {
//            String groupLoanRef = querryExistingGroupLoanNo(tXRequest);
//            if (!groupLoanRef.equals("") || !groupLoanRef.isEmpty())
//            {
//                createGroupLoanResponse.setCreateGroupLoanResult(groupLoanRef);
//            }
//        }
//        else
//        {
//            Object response = tXClient.createGroupLoan(tXRequest);
//            if (response instanceof GroupLoanOutputData)
//            {
//                grpLoanId = (((GroupLoanOutputData) response).getGroupLoanId());
//                respRef = String.valueOf(((GroupLoanOutputData) response).getCustomerNo());
//
//                System.out.println("" + respRef);
//                String grpLoanReference = querryGroupLoanNumber(grpLoanId);
//
//                createGroupLoanResponse.setCreateGroupLoanResult(grpLoanReference);
//                tXRequest.setGrpLoanReference(grpLoanReference);
//
//                tXRequest.setGroupLoanId(grpLoanId);
//                ArgosMain.bRLogger.logEvent("<Response>", convertToString(createGroupLoanResponse));
//
//                if (LogArgosTxn(tXRequest, "01"))
//                {
//                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "SUCCESS!");
//                }
//                else
//                {
//                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "FAILED!");
//                }
//
//            }
//            else if (response instanceof XAPIException)
//            {
//                createGroupLoanResponse.setCreateGroupLoanResult(ConError);
//                ArgosMain.bRLogger.logEvent("ERROR", convertToString(response));
//            }
//            else
//            {
//                createGroupLoanResponse.setResponseCode(ConError);
//                ArgosMain.bRLogger.logEvent("GLOBAL ERROR", convertToString(response));
//            }
//        }
//        getXapiCaller().setMainResponse(createGroupLoanResponse);
//        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
//        LogEventMessage();
//        return createGroupLoanResponse;
//    }
//
//    public InsertGroupMemberResponse insertGroupMember(AttachGroupMember acctBalReq)
//    {
//        setXapiCaller(new XAPICaller());
//        startTime = System.currentTimeMillis();
//        Long grpLoanId, grpLoanNo;
//        InsertGroupMemberResponse groupMemberResponse = new InsertGroupMemberResponse();
//
//        GroupMemberParameter memberParameter;
//        memberParameter = queryGrpMemberDetail(acctBalReq.getAccountNumber(), acctBalReq.getGroupLoanNo());
//
//        TXRequest tXRequest = new TXRequest();
//        TXClient tXClient = new TXClient(getXapiCaller());
//
//        if (!"".equals(acctBalReq.getAccountNumber()))
//        {
//            if (!groupMemberExist(memberParameter.getGrpCustId()))
//            {
//                groupMemberResponse.setInsertGroupMemberResult(Integer.parseInt(invalidAcct));
//            }
//            if (!memberLimitExceeded(Double.parseDouble(String.valueOf(acctBalReq.getMemberLoanAmount())), acctBalReq.getGroupLoanNo()))
//            {
//                groupMemberResponse.setInsertGroupMemberResult(Integer.parseInt(amountGreater));
//            }
//        }
//        else
//        {
//            System.out.println("Validation Pass");
//        }
//
//        tXRequest.setAccessCode("ArgosInterface99999");
//        tXRequest.setAccountNumber1(acctBalReq.getAccountNumber());
//
//        tXRequest.setCustomerNo(acctBalReq.getGroupLoanNo());
//        tXRequest.setCustomerId(memberParameter.getGrpMemberCustId());
//
//        tXRequest.setMembershipId(memberParameter.getGrpMemberId());
//        tXRequest.setGroupLoanId(memberParameter.getGrpLoanId());
//
//        tXRequest.setLoanCrncyId(memberParameter.getGrpLoanCrncyId());
//        tXRequest.setLoanAmount(BigDecimal.valueOf(Double.parseDouble(String.valueOf(acctBalReq.getMemberLoanAmount()))));
//
//        tXRequest.setSettlementAcctId(memberParameter.getGrpmemberAcctId());
//        tXRequest.setLoanProductId(memberParameter.getGrpLoanProdId());
//
//        tXRequest.setIndustryId(Long.parseLong(acctBalReq.getSicCode()));
//        tXRequest.setChannelId(8L);
//
//        tXRequest.setChannelCode("POS");
//        tXRequest.setUserLoginId("SYSTEM");
//
//        getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
//        getXapiCaller().setCurrency(tXRequest.getCurrencyId());
//        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
//        getXapiCaller().setRefNumber(acctBalReq.getGroupLoanNo());
//        getXapiCaller().setTxnDescription("Attach Beneficiary member [" + tXRequest.getAccountNumber1() + "->" + tXRequest.getCustomerNo() + "]");
//        getXapiCaller().setMainRequest(acctBalReq);
//        getXapiCaller().settXRequest(tXRequest);
//        if (isGroupMemberAttached(acctBalReq.getGroupLoanNo(), acctBalReq.getAccountNumber()))
//        {
//            System.out.println("a1cctBalReq 6 " + isGroupMemberAttached(acctBalReq.getGroupLoanNo(), acctBalReq.getAccountNumber()));
//            groupMemberResponse.setInsertGroupMemberResult(Integer.parseInt(memberExists));
//
//        }
//        else
//        {
//            ArgosMain.bRLogger.logEvent("Attach Beneficiary Request", convertToString(tXRequest));
//            Object response = tXClient.attachGroupMembers(tXRequest);
//
//            if (response instanceof GroupLoanBeneficiaryOutputData)
//            {
//                grpLoanId = (((GroupLoanBeneficiaryOutputData) response).getGroupLoanId());
//                respRef = String.valueOf(((GroupLoanBeneficiaryOutputData) response).getMembershipId());
//
//                groupMemberResponse.setInsertGroupMemberResult(0);
//                System.out.println("Success attach");
//                if (LogArgosBeneficiaryAttach(tXRequest, respCode))
//                {
//                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "SUCCESS!");
//                }
//                else
//                {
//                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "FAILED!");
//                }
//            }
//            else
//            {
//                groupMemberResponse.setInsertGroupMemberResult(1);
//            }
//        }
//        ArgosMain.bRLogger.logEvent(acctBalReq.getAccountNumber() + " Attach Beneficiary Response", acctBalReq.getGroupLoanNo() + "~" + convertToString(groupMemberResponse));
//        getXapiCaller().setMainResponse(groupMemberResponse);
//        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
//        LogEventMessage();
//        return groupMemberResponse;
//    }
//
//    public UpdateGroupLoanResponce updateGroupLoan(CreateGroupLoan acctBalReq)
//    {
//        setXapiCaller(new XAPICaller());
//        startTime = System.currentTimeMillis();
//        Long grpLoanId = null, grpLoanNo;
//        ArgosMain.bRLogger.logEvent("Create Group Loan Request", convertToString(acctBalReq));
//
//        UpdateGroupLoanResponce createGroupLoanResponse = new UpdateGroupLoanResponce();
//        TXRequest tXRequest = new TXRequest();
//
//        TXClient tXClient = new TXClient(getXapiCaller());
//        ApprovalParameter approvalParameter;
//
//        approvalParameter = queryGroupLoanDetails(acctBalReq.getGroupLoanRefNo());
//
//        if (!"".equalsIgnoreCase(acctBalReq.getGroupLoanRefNo()))
//        {
//            System.err.println("acctBalReq.getGroupLoanRefNo() " + acctBalReq.getGroupLoanRefNo());
//            if (!isGroupLoanValid(acctBalReq.getGroupLoanRefNo()))
//            {
//                createGroupLoanResponse.setUpdateGroupLoanResult(invalidAcct);
//                ArgosMain.bRLogger.logEvent("<Response>", convertToString(createGroupLoanResponse));
//
//                return createGroupLoanResponse;
//            }
//        }
//        else
//        {
//            createGroupLoanResponse.setUpdateGroupLoanResult(invalidAcct);
//            ArgosMain.bRLogger.logEvent("<Response>", convertToString(createGroupLoanResponse));
//
//            return createGroupLoanResponse;
//        }
//        System.err.println(" approvalParameter.getPURPOSE_CR() " + approvalParameter.getGRP_LOAN_ID());
//        tXRequest.setGroupLoanId(approvalParameter.getGRP_LOAN_ID());
//
//        tXRequest.setCustomerId(approvalParameter.getGRP_CUST_ID());
//        tXRequest.setLoanProductId(approvalParameter.getLOAN_PROD_ID());
//
//        tXRequest.setCustomerNo(approvalParameter.getGRP_CUST_NO().trim());
//        getXapiCaller().setRefNumber(approvalParameter.getGRP_LOAN_REF().trim());
//
//        tXRequest.setAccountNumber1(approvalParameter.getGRP_ACCT_NO().trim());
//        getXapiCaller().setAccountNo(approvalParameter.getGRP_ACCT_NO().trim());
//
//        tXRequest.setCreditTypeId(approvalParameter.getGRP_CR_TY());
//        tXRequest.setProductCombination(approvalParameter.getGRP_PROD_COMBINATION());
//
//        tXRequest.setLoanCrncyId(approvalParameter.getGRP_CRNCY_ID());
//        tXRequest.setLoanAmount(approvalParameter.getLOAN_AMOUNT());
//
//        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
//        tXRequest.setLnStartDate(approvalParameter.getLOAN_START_DATE());
//
//        tXRequest.setRepaymentStartDate(approvalParameter.getREPMNT_START_DATE());
//        tXRequest.setTermValue(approvalParameter.getTERM_VALUE());
//
//        tXRequest.setTermCode(approvalParameter.getTERM_CODE());
//        tXRequest.setMaturityDate(approvalParameter.getMAT_DATE());
//
//        tXRequest.setRateType(approvalParameter.getRATE_TY());
//        tXRequest.setPurposeOfCredit(Long.parseLong(approvalParameter.getPURPOSE_CR()));
//
//        tXRequest.setBankOfficerId(approvalParameter.getOFFICER_ID());
//        tXRequest.setBusinessUnitId(approvalParameter.getBU_ID());
//
//        tXRequest.setPaymentType(approvalParameter.getPMT_TY());
//        tXRequest.setRepayFreqCd(approvalParameter.getREPAY_FREQ_CD());
//
//        tXRequest.setRepayFreqValue(approvalParameter.getREPAY_FREQ_VAL());
//        tXRequest.setRepaymentStartDate(approvalParameter.getREPMNT_START_DATE());
//
//        tXRequest.setDisbursementDate(approvalParameter.getDISB_DATE());
//        tXRequest.setDisbursementMethod(approvalParameter.getDISB_METHOD());
//
//        tXRequest.setReferenceNumber(approvalParameter.getGRP_LOAN_REF());
//        getXapiCaller().setTxnDescription(tXRequest.getReferenceNumber());
//        tXRequest.setIndexRateId(approvalParameter.getINDEX_RATE_ID());
//
//        tXRequest.setIndustryId(approvalParameter.getINDUSTRY_ID());
//        tXRequest.setPortfolioId(approvalParameter.getPORTFOLIO_ID());
//
//        tXRequest.setChannelId(approvalParameter.getCHANNEL_ID());
//        tXRequest.setChannelCode(approvalParameter.getCHANNEL_CODE());
//
//        tXRequest.setUserLoginId(approvalParameter.getUSER_ID());
//        tXRequest.setRepaymentMethod(approvalParameter.getREPMNT_METHOD());
//
//        tXRequest.setMultipleDisbPerMember(approvalParameter.getMULTIPLE_DISB_PM()); // 
//        tXRequest.setTransmissionTime(System.currentTimeMillis());
//
//        tXRequest.setUserLoginId("SYSTEM");
//        tXRequest.setBusinessUnitId((approvalParameter.getBU_ID()));
//
//        getXapiCaller().settXRequest(tXRequest);
//        ArgosMain.bRLogger.logEvent("<TXRequest>", convertToString(tXRequest));
//
//        getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
//        getXapiCaller().setCurrency(tXRequest.getLoanCrncyId());
//        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
//        getXapiCaller().setRefNumber(tXRequest.getCustomerNo());
//        getXapiCaller().setTxnDescription("Update Loan [" + tXRequest.getAccountNumber1() + "~" + tXRequest.getCustomerNo() + "]");
//        getXapiCaller().setMainRequest(acctBalReq);
//
//        Object response = tXClient.updateGroupLoan(tXRequest);
//        if (response instanceof GroupLoanOutputData)
//        {
//            grpLoanId = (((GroupLoanOutputData) response).getGroupLoanId());
//            respRef = String.valueOf(((GroupLoanOutputData) response).getCustomerNo());
//
//            Long queueId = getQueueId(tXRequest.getReferenceNumber());
//            tXRequest.setQueueId(queueId);
//
//            ArgosMain.bRLogger.logEvent("<TXRequestWF>", convertToString(tXRequest));
//            Object wfitemresponse = tXClient.getWFItem(tXRequest);
//            if (wfitemresponse instanceof WFItemOutputData)
//            {
//                System.out.println("i have the instance " + tXRequest.getQueueId());
//                for (WorkListOutputData items : ((WFItemOutputData) wfitemresponse).getWorkItemList())
//                {
//                    System.out.println("fetching for group loan ref " + tXRequest.getReferenceNumber());
//                    if (tXRequest.getReferenceNumber().equals(items.getDescription()))
//                    {
//                        System.out.println(" items.getId()" + items.getWorkItemId());
//                        System.out.println("getListDesc()" + items.getQueueId());
//                        System.out.println("getListKey()" + items.getCustomerName());
//                        System.out.println("getListKey()" + items.getDescription());
//                        tXRequest.setWorkListItem(items.getWorkItemId());
//                    }
//                }
//                System.out.println("approving loan  " + tXRequest.getWorkListItem());
//
//                updateArgosGrpLoanLog(tXRequest, successResponse);
//                if (tXClient.saveGroupLoan(tXRequest).equals("00"))
//                {
//                    tXClient.saveGroupLoan(tXRequest);
//                    ArgosMain.bRLogger.logEvent("<Approved Success>", convertToString(((WFItemOutputData) wfitemresponse).getQueueList()));
//                }
//                else
//                {
//                    ArgosMain.bRLogger.logEvent("<Error Approving >", tXClient.saveGroupLoan(tXRequest));
//                    ArgosMain.bRLogger.logEvent("<Approval Failed>", convertToString(((WFItemOutputData) wfitemresponse).getQueueList()));
//                }
//
//            }
//            else if (response instanceof XAPIException)
//            {
//                ArgosMain.bRLogger.logEvent(((XAPIException) response).getErrorCode());
//                ArgosMain.bRLogger.logEvent("Workflow Item XapiERROR", response);
//            }
//            else
//            {
//                //  ArgosMain.bRLogger.logEvent(getTagValue(String.valueOf(response), "error-code"));
//                ArgosMain.bRLogger.logEvent("Workflow Item ERROR", response);
//            }
//            createGroupLoanResponse.setUpdateGroupLoanResult(success);
//            ArgosMain.bRLogger.logEvent("<RessetCreateGroupLoanResultponse>", convertToString(createGroupLoanResponse));
//
//        }
//        else if (response instanceof XAPIException)
//        {
//            //ArgosMain.bRLogger.logEvent(getTagValue(String.valueOf(response), "error-code"));
//            ArgosMain.bRLogger.logEvent("ERROR", response);
//            createGroupLoanResponse.setUpdateGroupLoanResult(ConError);
//
//        }
//        else
//        {
//            System.err.println("queueId " + tXRequest.getReferenceNumber());
//            Long queueId = getQueueId(tXRequest.getReferenceNumber());
//
//            tXRequest.setQueueId(queueId);
//            System.err.println("queueId " + queueId);
//
//            System.err.println("queueId " + tXRequest.getQueueId());
//            createGroupLoanResponse.setUpdateGroupLoanResult(ConError);
//            ArgosMain.bRLogger.logEvent("GLOBAL ERROR", response);
//            //createGroupLoanResponse.setCreateGroupLoanResult(getTagValue(String.valueOf(response), "error-code"));
//        }
//        getXapiCaller().settXRequest(tXRequest);
//        getXapiCaller().setMainResponse(createGroupLoanResponse);
//        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
//        LogEventMessage();
//        return createGroupLoanResponse;
//    } //getLoanDataByLoanId
//
//    public IndividualLoanUserDataResponse createIndividualLoan(IndividualLoanUserData acctBalReq)
//    {
//        setXapiCaller(new XAPICaller());
//        startTime = System.currentTimeMillis();
//        IndividualLoanUserDataResponse loanUserDataResponse = new IndividualLoanUserDataResponse();
//        try
//        {
//            String acct_no, account_name;
//
//            ArgosMain.bRLogger.logEvent("queryCustomerDetailsReq", convertToString(acctBalReq));
//            TXRequest tXRequest = new TXRequest();
//            TXClient tXClient = new TXClient(getXapiCaller());
//            LoanParameter loanParameter;
//            CustomerDetails customerDetails;
//            loanParameter = queryProductDetails(acctBalReq.getClass_code());
//
//            tXRequest.setChannelId(BRController.ChannelID);
//            tXRequest.setCustomerId(getCustomerId(acctBalReq.getRim_no().trim()));
//            tXRequest.setTxnAmount(acctBalReq.getLoan_amt());
//            tXRequest.setLoanAmount(acctBalReq.getLoan_amt());
//            tXRequest.setCurrencyId(loanParameter.getCrncyId());
//            tXRequest.setTermValue(acctBalReq.getLoan_term());
//            tXRequest.setTermCode(acctBalReq.getLoan_term_Code());
//            tXRequest.setBusinessUnitId(geBuid(acctBalReq.getBranch_no()));
//            tXRequest.setUserLoginId(acctBalReq.getCredit_Officer_Code());
//            tXRequest.setBankOfficerId(getBankOfficerId(acctBalReq.getCredit_Officer_Code()));
//            tXRequest.setCreditTypeId(loanParameter.getCreditTypeId());
//            tXRequest.setCustomerNo(acctBalReq.getRim_no().trim());
//            tXRequest.setAccountId(getAccountId(acctBalReq.getSVAccountNumber()));
//            tXRequest.setLnStartDate(acctBalReq.getContract_date());
//            tXRequest.setProduct_id(getProductId(acctBalReq.getClass_code()));
//            tXRequest.setAccountNumber1(acctBalReq.getSVAccountNumber().trim());
//            tXRequest.setPurposeOfCredit(477L);
//            tXRequest.setPortfolioId(11L);
//            tXRequest.setReference(acctBalReq.getSVAccountNumber().trim());
//            tXRequest.setRateType(loanParameter.getRateType());
//            tXRequest.setIndexRateId(loanParameter.getIndexRateId());
//            tXRequest.setIndustryId(new Long(acctBalReq.getSic_Code()));
//            tXRequest.setAccessCode("ArgosInterface99999");
//            tXRequest.setTransmissionTime(System.currentTimeMillis());
//            tXRequest.setMaturityDate(getMaturityDate(acctBalReq.getContract_date(), acctBalReq.getLoan_term()));
//
//            getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
//            getXapiCaller().setCurrency(tXRequest.getCurrencyId());
//            getXapiCaller().setTxnAmount(tXRequest.getTxnAmount());
//            getXapiCaller().setRefNumber(tXRequest.getAccountNumber1());
//            getXapiCaller().setTxnDescription("Individual Loan Creation");
//            getXapiCaller().setMainRequest(acctBalReq);
//            getXapiCaller().settXRequest(tXRequest);
//
//            boolean checkIfApplExist = isApplicationNew(tXRequest.getCustomerNo(), tXRequest.getLoanAmount());
//            System.err.println("exists application... " + checkIfApplExist);
//            if (checkIfApplExist)
//            {
//                System.err.println("Application Exists");
//                Long maxApplId = queryMaxApplID(tXRequest.getCustomerNo(), tXRequest.getLoanAmount());
//                customerDetails = queryCustomerDetails(tXRequest.getCustomerNo(), tXRequest.getLoanAmount(), maxApplId);
//                tXRequest.setApplID(customerDetails.getApplId());
//                tXRequest.setCustomerName(customerDetails.getFirstName());
//                if (SicCodeValid(acctBalReq.getSic_Code()))
//                {
//                    updateCreditAppSicCode(customerDetails.getApplId(), acctBalReq.getSic_Code());
//                }
//                String Ln_AcctNo = createindividualLoan(tXRequest);
//                loanUserDataResponse.setCreateIndividualLoanResult(Ln_AcctNo);
//                updateArgosIndvLoanLog(tXRequest, successResponse);
//            }
//            else
//            {
//                boolean applicationResponse = tXClient.createCreditApplication(tXRequest);
//
//                if (applicationResponse)
//                {
//                    customerDetails = queryCustomerDetails(tXRequest.getCustomerNo().trim(), tXRequest.getCustomerId());
//                    tXRequest.setApplID(customerDetails.getApplId());
//                    tXRequest.setCustomerName(customerDetails.getFirstName());
//
//                    if (SicCodeValid(acctBalReq.getSic_Code()))
//                    {
//                        updateCreditAppSicCode(customerDetails.getApplId(), acctBalReq.getSic_Code());
//                    }
//                    boolean logLoanDetail = LogArgosIndividualLoan(tXRequest, "01");
//                    if (logLoanDetail)
//                    {
//                        try
//                        {
//                            Object response = tXClient.createAndApproveLoanAccount(tXRequest);
//                            if (response instanceof LoanAccountSummaryOutputData)
//                            {
//                                acct_no = (((LoanAccountSummaryOutputData) response).getAccountNo());
//                                account_name = String.valueOf(((LoanAccountSummaryOutputData) response).getAccountName());
//                                loanUserDataResponse.setCreateIndividualLoanResult(acct_no);
//                                updateArgosIndvLoanLog(tXRequest, successResponse);
//
//                            }
//                            else
//                            {
//                                if (response instanceof XAPIException)
//                                {
//                                    ArgosMain.bRLogger.logEvent("Loan creation Error", "[Loan creation Failed] " + convertToString(response));
//                                }
//                                else
//                                {
//                                    ArgosMain.bRLogger.logEvent("Loan creation Error", "[Loan creation Failed] " + convertToString(response));
//                                }
//                            }
//                        }
//                        catch (Exception ex)
//                        {
//                            if (ex instanceof XAPIException)
//                            {
//                                ArgosMain.bRLogger.logEvent("Loan creation Exception", "[Loan creation Failed] " + ex);
//
//                            }
//                            else
//                            {
//                                ArgosMain.bRLogger.logEvent("Loan creation Exception", "[Loan creation Failed] " + ex);
//
//                            }
//                        }
//                    }
//                    else
//                    {
//                        ArgosMain.bRLogger.logEvent("Loan data Logging", "[Loan data Logging Failed] " + Boolean.FALSE);
//                    }
//                }
//                else
//                {
//                    ArgosMain.bRLogger.logEvent("Credit Application Creation", "[Credit Application Creation] " + Boolean.FALSE);
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            ArgosMain.bRLogger.logEvent("Global Error", "[An error occured] " + ex);
//        }
//        getXapiCaller().setRespCode(loanUserDataResponse.getCreateIndividualLoanResult());
//        getXapiCaller().setMainResponse(loanUserDataResponse);
//        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
//        LogEventMessage();
//        return loanUserDataResponse;
//    }
//
//    public String createindividualLoan(TXRequest tXRequest)
//    {
//        setXapiCaller(new XAPICaller());
//        startTime = System.currentTimeMillis();
//        TXClient tXClient = new TXClient(getXapiCaller());
//        String acct_no = "0";
//        try
//        {
//            Object response = tXClient.createAndApproveLoanAccount(tXRequest);
//            if (response instanceof LoanAccountSummaryOutputData)
//            {
//                acct_no = (((LoanAccountSummaryOutputData) response).getAccountNo());
//                String account_name = String.valueOf(((LoanAccountSummaryOutputData) response).getAccountName());
//
//                updateArgosIndvLoanLog(tXRequest, successResponse);
//            }
//            else
//            {
//                if (response instanceof XAPIException)
//                {
//                    ArgosMain.bRLogger.logEvent("Loan creation Error For Existing creditApp", "[Loan creation Failed] " + convertToString(response));
//                }
//                else
//                {
//                    ArgosMain.bRLogger.logEvent("Loan creation Error For Existing creditApp", "[Loan creation Failed] " + convertToString(response));
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            if (ex instanceof XAPIException)
//            {
//                ex.printStackTrace();
//            }
//            else
//            {
//                ex.printStackTrace();
//            }
//        }
//        return acct_no;
//    }
//
//    public LoanDataByLoanIdResponse getLoanDataByLoanId(LoanDataByLoanId getLoanDataByLoanId)
//    {
//        setXapiCaller(new XAPICaller());
//        LoanDataByLoanIdResponse byLoanIdResponse;
//        startTime = System.currentTimeMillis();
//        ArgosMain.bRLogger.logEvent("queryLoanDataById", convertToString(getLoanDataByLoanId));
//        byLoanIdResponse = queryLoanDataById(getLoanDataByLoanId.getLoanId());
//
//        getXapiCaller().setAccountNo(getLoanDataByLoanId.getLoanId());
//        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
//        getXapiCaller().setRefNumber(getLoanDataByLoanId.getLoanId());
//        getXapiCaller().setTxnDescription("Get Loan Data By Id");
//        getXapiCaller().setMainRequest(convertToString(getLoanDataByLoanId));
//        getXapiCaller().setRefNumber(byLoanIdResponse.getLoanId());
//        getXapiCaller().setRespCode("0");
//        getXapiCaller().setMainResponse(convertToString(byLoanIdResponse));
//        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
//        LogEventMessage();
//        ArgosMain.bRLogger.logEvent("queryLoanDataById", convertToString(byLoanIdResponse));
//        return byLoanIdResponse;
//    }
//
//    public LoanIDByGroupLoanNumberResponse getGroupLoanDataByLoanId(LoanIDByGroupLoanNumber byGroupLoanNumber)
//    {
//        setXapiCaller(new XAPICaller());
//        LoanIDByGroupLoanNumberResponse byGroupLoanNumberResponse;
//        startTime = System.currentTimeMillis();
//        ArgosMain.bRLogger.logEvent("queryLoanDataById", convertToString(byGroupLoanNumber));
//        byGroupLoanNumberResponse = queryGroupLoanDataById(byGroupLoanNumber.getGroupLoanNumber());
//
//        getXapiCaller().setAccountNo(byGroupLoanNumber.getGroupLoanNumber());
//        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
//        getXapiCaller().setRefNumber(byGroupLoanNumber.getGroupLoanNumber());
//        getXapiCaller().setTxnDescription("Get Loan Data By Id");
//        getXapiCaller().setMainRequest(convertToString(byGroupLoanNumber));
//        getXapiCaller().setRespCode("0");
//        getXapiCaller().setMainResponse(byGroupLoanNumberResponse);
//        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
//        ArgosMain.bRLogger.logEvent("queryLoanDataById", convertToString(byGroupLoanNumber));
//        LogEventMessage();
//        return byGroupLoanNumberResponse;
//    }
//
//    public CustomerDetails getLoanData(CustomerData acctBalReq)
//    {
//        setXapiCaller(new XAPICaller());
//        startTime = System.currentTimeMillis();
//        CustomerDetails customerDetails;
//        ArgosMain.bRLogger.logEvent("queryCustomerDetailsReq", convertToString(acctBalReq));
//
//        customerDetails = queryCustomerDetails(acctBalReq.getAcct_no());
//
//        getXapiCaller().setAccountNo(acctBalReq.getAcct_no());
//        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
//        getXapiCaller().setRefNumber(acctBalReq.getAcct_no());
//        getXapiCaller().setTxnDescription("Get Loan Data");
//        getXapiCaller().setMainRequest(convertToString(acctBalReq));
//        getXapiCaller().setRespCode("0");
//        getXapiCaller().setMainResponse(convertToString(customerDetails));
//        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
//        ArgosMain.bRLogger.logEvent("queryCustomerDetailsRes", convertToString(customerDetails));
//        LogEventMessage();
//        return customerDetails;
//    }
//
//   public static String getTagValue(String xml, String tagName)
//    {
//
//        if (xml.contains("java.lang.NullPointerException") || xml.equalsIgnoreCase("null") || xml.isEmpty())
//        {
//            return invalidError;
//        }
//        else
//        {
//            return xml.split("<" + tagName + ">")[1].split("</" + tagName + ">")[0];
//        }
//    }

    public boolean isApplicationNew(String custNo, BigDecimal loanAmount)
    {
        return checkIfExists("select APPL_ID from " + BRController.CMSchemaName + ".ARGOS_INDIVIDUAL_LOAN "
                + "where status = '01' and loan_amt =" + loanAmount + " and cust_no = '" + custNo.trim() + "'");
    }

    public boolean isGLValid(String acctNo)
    {
        return checkIfExists("SELECT GL_ACCT_NO FROM " + BRController.CoreSchemaName + ".GL_ACCOUNT WHERE GL_ACCT_NO='" + acctNo.trim() + "'");
    }

    public boolean isAcctValid(Long currencyid, String acctNo)
    {
        return checkIfExists("SELECT DISTINCT AC.ACCT_NO FROM " + BRController.CoreSchemaName + ".ACCOUNT AC," + BRController.CoreSchemaName + ".CURRENCY  CU "
                + "WHERE  AC.CRNCY_ID = CU.CRNCY_ID AND CU.CRNCY_ID = '" + currencyid + "' AND AC.ACCT_NO = '" + acctNo.trim() + "'");
    }

    public boolean isCustomerValid(String custNo)
    {
        return checkIfExists("SELECT DISTINCT AC.CUST_ID FROM " + BRController.CoreSchemaName + ".CUSTOMER AC "
                + "WHERE   AC.CUST_NO = '" + custNo.trim() + "' AND AC.REC_ST = 'A'");
    }

    public boolean isGroupLoanPending(TXRequest tXRequest)
    {
        return checkIfExists("SELECT DISTINCT GRP_CUST_ID  FROM " + BRController.CMSchemaName + ".BL_ARGOS_TXN_LOG "
                + "WHERE LOAN_AMOUNT = " + tXRequest.getLoanAmount() + " AND LOAN_START_DATE = '" + tXRequest.getLnStartDate() + "' AND TERM_VALUE = " + tXRequest.getTermValue() + " "
                + "AND TERM_CODE = '" + tXRequest.getTermCode() + "' AND GRP_CRNCY_ID = " + tXRequest.getLoanCrncyId() + " AND GRP_CUST_ID = " + tXRequest.getCustomerId() + " "
                + "AND GRP_CUST_NO = '" + tXRequest.getCustomerNo().trim() + "' AND RETURN_CODE = '01'");
    }

    public boolean isGroupLoanValid(String custNo)
    {
        return checkIfExists("SELECT DISTINCT GRP_LOAN_REF FROM " + BRController.CMSchemaName + ".BL_ARGOS_TXN_LOG "
                + "where GRP_LOAN_REF = '" + custNo.trim() + "' AND RETURN_CODE = '01'");
    }

    public boolean isSchFeeValid(String currencyCode, String acctNo)
    {
        return checkIfExists("SELECT DISTINCT AC.ACCT_NO FROM " + BRController.CMSchemaName + ".BL_BILLER  BL," + BRController.CoreSchemaName + ".ACCOUNT AC, " + BRController.CoreSchemaName + ".CURRENCY  CU "
                + "WHERE BL.BILLER_COL_ACCT = AC.ACCT_NO AND AC.CRNCY_ID = CU.CRNCY_ID AND CU.CRNCY_CD = '" + currencyCode.trim() + "' AND AC.ACCT_NO = '" + acctNo.trim() + "'");
    }

    public boolean groupMemberExist(Long grpAcctId)
    {
        return checkIfExists("SELECT MEMBERSHIP_REF_NO from " + BRController.CoreSchemaName + ".group_member where GROUP_CUST_ID = " + grpAcctId + "");
    }

    public boolean isGroupMemberAttached(String grpRefNo, String AccountNumber)
    {
        return checkIfExists("SELECT ACCT_NO FROM " + BRController.CMSchemaName + ".BL_ARGOS_MEMBERS "
                + "WHERE  GRP_LOAN_NO = '" + grpRefNo.trim() + "' AND ACCT_NO = '" + AccountNumber.trim() + "' "
                + "AND SUBSTR(DATETIME,0,10) >=(SELECT TO_CHAR(SYSDATE-5,'YYYY-MM-DD') FROM DUAL) ");
    }

    public boolean memberLimitExceeded(Double memberAmt, String refNo)
    {
        return checkIfExists("SELECT  GRP_LOAN_REF FROM " + BRController.CMSchemaName + ".BL_ARGOS_TXN_LOG "
                + "WHERE LOAN_AMOUNT_LIMIT >=" + memberAmt + " AND  GRP_LOAN_REF = '" + refNo.trim() + "'");
    }

    public boolean checkIfExists(String query)
    {
        boolean exists = false;
        try
        {
            try (ResultSet rs = executeQueryToResultSet(query))
            {
                exists = rs.next();
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return exists;
    }

    public LoanParameter queryProductDetails(String prodCode)
    {
        LoanParameter groupLoanParameter = new LoanParameter();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT A.PROD_ID, A.CRNCY_ID, B.RATE_TY, B.INDEX_RATE_ID, C.PAY_TY, C.PAY_MTHD_TY, C.REPAY_FREQ_VALUE, C.REPAY_FREQ_CD, D.MULTI_DISBRSMNT_FG, E.TERM_FREQ_CD, E.TERM_FREQ_VALUE,CTP.CR_TY_ID  "
                    + "FROM " + BRController.CoreSchemaName + ".PRODUCT A, " + BRController.CoreSchemaName + ".LOAN_PRODUCT_INTEREST B, " + BRController.CoreSchemaName + ".LOAN_PRODUCT_PAYMENT_INFO C, "
                    + "" + BRController.CoreSchemaName + ".LOAN_PRODUCT_DISBURSEMENT D, " + BRController.CoreSchemaName + ".LOAN_PRODUCT_BASIC_INFO E, " + BRController.CoreSchemaName + ".CREDIT_TYPE_PRODUCT CTP "
                    + "WHERE A.PROD_ID = B.PROD_ID "
                    + "AND B.PROD_ID = C.PROD_ID "
                    + "AND C.PROD_ID = D.PROD_ID "
                    + "AND D.PROD_ID = E.PROD_ID "
                    + "AND A.PROD_ID = CTP.PROD_ID "
                    + "AND A.PROD_CD = '" + prodCode.trim() + "'"))
            {
                while (rs.next())
                {
                    groupLoanParameter.setProdId(rs.getLong(1));
                    groupLoanParameter.setCrncyId(rs.getLong(2));
                    groupLoanParameter.setRateType(rs.getString(3));
                    groupLoanParameter.setIndexRateId(rs.getLong(4));
                    groupLoanParameter.setPmtType(rs.getString(5));
                    groupLoanParameter.setRepmtMethodtype(rs.getString(6));
                    groupLoanParameter.setRepmtFreqValue(rs.getLong(7));
                    groupLoanParameter.setRepmtFreqCode(rs.getString(8));
                    groupLoanParameter.setMultiDisbFg(rs.getString(9));
                    groupLoanParameter.setTermFreqcode(rs.getString(10));
                    groupLoanParameter.setTermFreqValue(rs.getLong(11));
                    groupLoanParameter.setCreditTypeId(rs.getLong(12));
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return groupLoanParameter;
    }

    public CustomerDetails queryCustomerDetails(String acctNo)
    {
        System.err.println(">>>>>>>>>>>>>> " + acctNo);
        CustomerDetails customerDetails = new CustomerDetails();

        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT NVL(A.ACCT_NO,' '),NVL(F.FIRST_NM,' '),NVL(F.MIDDLE_NM,F.LAST_NM),NVL(F.LAST_NM,' '),F.BIRTH_DT,AD.ADDR_LINE_1 street,AD.ADDR_LINE_1 houseNumber,AD.ADDR_LINE_2 neighbourhood, "
                    + "AD.ADDR_LINE_2, AD.ADDR_LINE_3, D.CONTACT,D.CONTACT,G.LEDGER_BAL, AD.CITY province, IR.INDUSTRY_CD ,CU.CNTRY_NM  ,AD.CITY birthProvince, F.GENDER_TY , AD.PHONE_NO businessPhone, C.IDENT_NO, "
                    + "(SELECT ITF.IDENT_DESC FROM " + BRController.CoreSchemaName + ".IDENTIFICATION_TYPE_REF ITF ," + BRController.CoreSchemaName + ".CUSTOMER_IDENTIFICATION_XREF T1  "
                    + "WHERE T1.IDENT_ID =ITF.IDENT_ID AND T1.CUST_IDENT_XREF_ID=C.CUST_IDENT_XREF_ID  ) AS identificationType,BU.BU_NM, "
                    + "NVL((SELECT LEDGER_BAL FROM " + BRController.CoreSchemaName + ".TD_ACCOUNT_SUMMARY WHERE ACCT_ID =(SELECT max(ACCT_ID) FROM " + BRController.CoreSchemaName + ".ACCOUNT WHERE CUST_ID = A.CUST_ID AND PROD_CAT_TY = 'TDEP' AND REC_ST = 'A' )),0) AS TDEP_ACCT, "
                    + "NVL((SELECT NVL(C.DISBURSEMENT_LIMIT,0) FROM " + BRController.CoreSchemaName + ".ACCOUNT A," + BRController.CoreSchemaName + ".LOAN_ACCOUNT C "
                    + "WHERE  A.ACCT_ID = C.ACCT_ID "
                    + "AND A.CUST_ID =A.ACCT_ID "
                    + "AND PROD_CAT_TY = 'LN' "
                    + "GROUP BY C.DISBURSEMENT_LIMIT,C.CLOSED_DT,A.ACCT_ID "
                    + "HAVING C.CLOSED_DT = (SELECT MAX(CLOSED_DT) FROM " + BRController.CoreSchemaName + ".LOAN_ACCOUNT WHERE  ACCT_ID = (SELECT ACCT_ID FROM " + BRController.CoreSchemaName + ".CUSTOMER WHERE CUST_ID = B.CUST_ID))),0)  AS previousLoanAmount  "
                    + "FROM " + BRController.CoreSchemaName + ".ACCOUNT A," + BRController.CoreSchemaName + ".CUSTOMER B," + BRController.CoreSchemaName + ".CUSTOMER_IDENTIFICATION C, "
                    + " " + BRController.CoreSchemaName + ".CUSTOMER_CONTACT_MODE D, " + BRController.CoreSchemaName + ".CUSTOMER_ADDRESS E,  "
                    + " " + BRController.CoreSchemaName + ".PERSON F," + BRController.CoreSchemaName + ".DEPOSIT_ACCOUNT_SUMMARY G ," + BRController.CoreSchemaName + ".INDUSTRY_REF IR," + BRController.CoreSchemaName + ".COUNTRY CU," + BRController.CoreSchemaName + ".BUSINESS_UNIT BU," + BRController.CoreSchemaName + ".ADDRESS AD  "
                    + " where A.CUST_ID(+) = B.CUST_ID  "
                    + " AND C.CUST_ID(+) = A.CUST_ID "
                    + " AND D.CUST_ID(+) = B.CUST_ID "
                    + " AND E.CUST_ID(+) = B.CUST_ID "
                    + " AND F.CUST_ID(+) = B.CUST_ID  "
                    + " AND AD.ADDR_ID(+) = E.ADDR_ID  "
                    + " AND F.CUST_ID = A.CUST_ID"
                    + " AND G.DEPOSIT_ACCT_ID(+) = A.ACCT_ID "
                    + " AND IR.INDUSTRY_ID(+) = B.INDUSTRY_ID "
                    + " AND F.CNTRY_OF_BIRTH_ID(+) = CU.CNTRY_ID "
                    + " AND B.MAIN_BRANCH_ID = BU.BU_ID(+) "
                    + "AND A.ACCT_NO =  '" + acctNo.trim() + "'"
            ))
            {
                while (rs.next())
                {
                    customerDetails.setAccountNumber(rs.getString(1));
                    customerDetails.setFirstName(rs.getString(2));
                    customerDetails.setMiddleName(rs.getString(3));
                    customerDetails.setLastName(rs.getString(4));
                    customerDetails.setBirthday(rs.getString(5));
                    customerDetails.setStreet(rs.getString(6));
                    customerDetails.setHouseNumber(rs.getString(7));
                    customerDetails.setNeighbourhood(rs.getString(8));
                    customerDetails.setCommunityTerritory(rs.getString(9));
                    customerDetails.setFreeAddressField(rs.getString(10));
                    customerDetails.setHomePhoneNumber(rs.getString(11));
                    customerDetails.setMobilePhoneNumber(rs.getString(12));
                    customerDetails.setSavingsBalance(rs.getString(13));
                    customerDetails.setProvince(rs.getString(14));
                    customerDetails.setSicCode(rs.getString(15));
                    customerDetails.setBirthPlace(rs.getString(16));
                    customerDetails.setBirthProvince(rs.getString(17));
                    customerDetails.setGender(rs.getString(18));
                    customerDetails.setBusinessPhone(rs.getString(19));
                    customerDetails.setIdNumber(rs.getString(20));
                    customerDetails.setIdentificationType(rs.getString(21));
                    customerDetails.setBranchName(rs.getString(22));
                    customerDetails.setTDPleage(rs.getBigDecimal(23));
                    customerDetails.setPreviousLoanAmount(rs.getBigDecimal(24));
                }
            }
        }
        catch (Exception ex)
        {
            customerDetails.setAccountNumber("N/A");
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return customerDetails;
    }

    public CustomerDetails queryCustomerDetails(String custNo, Long custID)
    {
        System.err.println(">>>>>>>>>>>>>> " + custNo);
        CustomerDetails customerDetails = new CustomerDetails();

        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT MAX(CA.APPL_ID),CU.CUST_NM FROM " + BRController.CoreSchemaName + ".CUSTOMER CU, " + BRController.CoreSchemaName + ".CREDIT_APPL CA "
                    + "WHERE CU.CUST_ID = CA.CUST_ID AND CU.CUST_ID = " + custID + " AND CU.CUST_NO =  '" + custNo.trim() + "' "
                    + "GROUP BY CU.CUST_NM,CA.APPL_ID "
                    + "HAVING CA.APPL_ID = (SELECT MAX(APPL_ID) FROM " + BRController.CoreSchemaName + ".CREDIT_APPL WHERE CUST_ID = " + custID + " )"
            ))
            {
                while (rs.next())
                {
                    customerDetails.setApplId(rs.getLong(1));
                    customerDetails.setFirstName(rs.getString(2));
                }
            }
        }
        catch (Exception ex)
        {
            customerDetails.setAccountNumber("N/A");
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return customerDetails;
    }

    public boolean SicCodeValid(String sicCode)
    {
        return checkIfExists("SELECT INDUSTRY_ID FROM " + BRController.CoreSchemaName + ".INDUSTRY_REF AC WHERE  AC.INDUSTRY_CD =  '" + sicCode + "'");
    }

    public boolean updateCreditAppSicCode(Long applId, String IndCode)
    {
        return executeUpdate("UPDATE " + BRController.CoreSchemaName + ".CREDIT_APPL SET  INDUSTRY_ID = (SELECT INDUSTRY_ID FROM " + BRController.CoreSchemaName + ".INDUSTRY_REF WHERE INDUSTRY_CD = '" + IndCode + "') WHERE  APPL_ID = " + applId + " ", true);

    }

    public CustomerDetails queryCustomerDetails(String custNo, BigDecimal Amount, Long ApplID)
    {
        System.err.println(">>>>>>>>>>>>>> " + custNo);
        CustomerDetails customerDetails = new CustomerDetails();

        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT APPL_ID,CUSTOMER_NAME FROM " + BRController.CMSchemaName + ".ARGOS_INDIVIDUAL_LOAN "
                    + "WHERE STATUS = '01' and cust_no ='" + custNo.trim() + "' AND APPL_ID = " + ApplID + " AND LOAN_AMT = " + Amount + ""
            ))
            {
                while (rs.next())
                {
                    customerDetails.setApplId(rs.getLong(1));
                    customerDetails.setFirstName(rs.getString(2));
                }
            }
        }
        catch (Exception ex)
        {
            customerDetails.setAccountNumber("N/A");
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return customerDetails;
    }

    public Long queryMaxApplID(String custNo, BigDecimal Amount)
    {
        System.err.println(">>>>>>>>>>>>>> " + custNo);
        Long applId = 0L;

        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT max(APPL_ID) FROM " + BRController.CMSchemaName + ".ARGOS_INDIVIDUAL_LOAN "
                    + "WHERE STATUS = '01' and cust_no ='" + custNo.trim() + "'  AND LOAN_AMT = " + Amount + ""
            ))
            {
                while (rs.next())
                {
                    applId = (rs.getLong(1));
                }
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return applId;
    }

    public LoanDataByLoanIdResponse queryLoanDataById(String acctNo)
    {
        System.err.println(">>>>>>>>>>>>>> " + acctNo);
        LoanDataByLoanIdResponse LoanByIdResponse = new LoanDataByLoanIdResponse();

        try
        {
            try (ResultSet rs = executeQueryToResultSet("select distinct a.acct_no, LAC.DISBURSEMENT_LIMIT, LAS.LEDGER_BAL , a.REC_ST, Q.RISK_CLASS_ID, "
                    + "NVL(((select to_date(display_value,'dd/MM/yyyy') from  " + BRController.CoreSchemaName + ".ctrl_parameter where param_cd = 'S02') - lf.due_dt),0)delinquent_days "
                    + "FROM   " + BRController.CoreSchemaName + ".account a , " + BRController.CoreSchemaName + ".STATUS_RISK_CLASSIFICATION Q,  " + BRController.CoreSchemaName + ".loan_account lac, " + BRController.CoreSchemaName + ".loan_account_summary las,  "
                    + "(select (le.acct_id) acct_id, min(le.due_dt) due_dt from  " + BRController.CoreSchemaName + ".ln_acct_repmnt_event le   "
                    + "where le.due_dt < (select to_date(display_value,'dd/MM/yyyy') from  " + BRController.CoreSchemaName + ".ctrl_parameter where param_cd = 'S02') "
                    + "and le.rec_st in ('N', 'P') group by le.acct_id)lf  "
                    + "where a.acct_id = lf.acct_id(+)  "
                    + "and A.RISK_CLASS_ID = Q.RISK_CLASS_ID  "
                    + "and A.ACCT_ID = LAC.ACCT_ID "
                    + "and LAC.ACCT_ID = LAS.ACCT_ID "
                    + "and las.ACCT_ID  = lf.acct_id(+) "
                    + "and lac.ACCT_ID = las.acct_id  "
                    + "and lac.ACCT_ID = lf.acct_id(+)  "
                    + "and a.acct_no='" + acctNo.trim() + "' order by 5 "
            ))
            {
                while (rs.next())
                {
                    LoanByIdResponse.setLoanId(rs.getString(1));
                    LoanByIdResponse.setAmountFinanced(rs.getBigDecimal(2));
                    LoanByIdResponse.setCurrentBalance(rs.getBigDecimal(3));
                    LoanByIdResponse.setStatus(rs.getString(4));
                    LoanByIdResponse.setPaidBack(rs.getBigDecimal(5));
                    LoanByIdResponse.setNumberOfDayDelinquent(rs.getInt(6));

                }
            }
        }
        catch (Exception ex)
        {
            LoanByIdResponse.setLoanId("N/A");
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return LoanByIdResponse;
    }

    public LoanIDByGroupLoanNumberResponse queryGroupLoanDataById(String acctNo)
    {
        System.err.println(">>>>>>>>>>>>>> " + acctNo);
        List<LoanIDByGroupLoanList> Acct_Loan = new LinkedList<>();
        LoanIDByGroupLoanList iDByGroupLoanList;// = new LoanIDByGroupLoanList();
        LoanIDByGroupLoanNumberResponse groupLoanByIdResponse = new LoanIDByGroupLoanNumberResponse();;

        try
        {
            try (ResultSet rs = executeQueryToResultSet("select grm.DEFLT_ACCT_NO, "
                    + "(select D.ACCT_NO from " + BRController.CoreSchemaName + ".CREDIT_APPL_MEMBER_DISBRSMNT a,"
                    + "" + BRController.CoreSchemaName + ".GROUP_MEMBER b," + BRController.CoreSchemaName + ".customer c," + BRController.CoreSchemaName + ".account d  "
                    + "where a.MEMBER_NO = C.CUST_NO "
                    + "and C.CUST_ID = d.CUST_ID "
                    + "and B.MEMBER_CUST_ID = C.CUST_ID "
                    + "and D.PROD_CAT_TY = 'LN' and c.cust_id =GRM.MEMBER_CUST_ID)as ln_acct_no "
                    + "from  "
                    + "" + BRController.CoreSchemaName + ".GROUP_MEMBER grm," + BRController.CoreSchemaName + ".group_loan grl "
                    + "where grm.GROUP_CUST_ID = GRL.CUST_ID "
                    + "and grl.REF_NO ='" + acctNo.trim() + "' "
            ))
            {
                while (rs.next())
                {
                    iDByGroupLoanList = new LoanIDByGroupLoanList();
                    iDByGroupLoanList.setAccount_number(rs.getString(1));
                    iDByGroupLoanList.setLoanId(rs.getString(2));
                    Acct_Loan.add(iDByGroupLoanList);
                    groupLoanByIdResponse.setAcct_Loan(Acct_Loan);
                }

            }
        }
        catch (Exception ex)
        {
            // iDByGroupLoanList.setLoanId("N/A");
            //Acct_Loan.add(iDByGroupLoanList);
            //    groupLoanByIdResponse.setAcct_Loan(Acct_Loan);
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return groupLoanByIdResponse;
    }

    public ApprovalParameter queryGroupLoanDetails(String grpLoanRef)
    {
        ApprovalParameter approvalParameter = new ApprovalParameter();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT GRP_LOAN_ID,GRP_LOAN_REF,ACCESS_CODE ,GRP_CUST_ID ,LOAN_PROD_ID ,GRP_CUST_NO ,GRP_ACCT_NO ,GRP_CR_TY ,GRP_PROD_COMBINATION ,GRP_CRNCY_ID , "
                    + "LOAN_AMOUNT ,LOAN_START_DATE ,REPMNT_START_DATE ,TERM_VALUE ,TERM_CODE ,MAT_DATE ,RATE_TY ,PURPOSE_CR ,OFFICER_ID ,BU_ID , PMT_TY ,"
                    + "REPAY_FREQ_CD ,REPAY_FREQ_VAL,DISB_DATE ,DISB_METHOD ,REF_NO ,INDEX_RATE_ID,INDUSTRY_ID,PORTFOLIO_ID,CHANNEL_ID,CHANNEL_CODE, "
                    + "USER_ID,REPMNT_METHOD ,MULTIPLE_DISB_PM,TRAMSN_TIME,TXN_NARRATION ,SYS_USER_ID "
                    + "FROM BL_ARGOS_TXN_LOG "
                    + "WHERE GRP_LOAN_REF = '" + grpLoanRef.trim() + "'"))
            {
                while (rs.next())
                {
                    approvalParameter.setGRP_LOAN_ID(rs.getLong(1));
                    approvalParameter.setGRP_LOAN_REF(rs.getString(2));
                    approvalParameter.setACCESS_CODE(rs.getString(3));
                    approvalParameter.setGRP_CUST_ID(rs.getLong(4));
                    approvalParameter.setLOAN_PROD_ID(rs.getLong(5));
                    approvalParameter.setGRP_CUST_NO(rs.getString(6));
                    approvalParameter.setGRP_ACCT_NO(rs.getString(7));
                    approvalParameter.setGRP_CR_TY(rs.getLong(8));
                    approvalParameter.setGRP_PROD_COMBINATION(rs.getString(9));
                    approvalParameter.setGRP_CRNCY_ID(rs.getLong(10));
                    approvalParameter.setLOAN_AMOUNT(rs.getBigDecimal(11));
                    approvalParameter.setLOAN_START_DATE(rs.getString(12));
                    approvalParameter.setREPMNT_START_DATE(rs.getString(13));
                    approvalParameter.setTERM_VALUE(rs.getLong(14));
                    approvalParameter.setTERM_CODE(rs.getString(15));
                    approvalParameter.setMAT_DATE(rs.getString(16));
                    approvalParameter.setRATE_TY(rs.getString(17));
                    approvalParameter.setPURPOSE_CR(rs.getString(18));
                    approvalParameter.setOFFICER_ID(rs.getLong(19));
                    approvalParameter.setBU_ID(rs.getLong(20));
                    approvalParameter.setPMT_TY(rs.getString(21));
                    approvalParameter.setREPAY_FREQ_CD(rs.getString(22));
                    approvalParameter.setREPAY_FREQ_VAL(rs.getLong(23));
                    approvalParameter.setDISB_DATE(rs.getString(24));
                    approvalParameter.setDISB_METHOD(rs.getString(25));
                    approvalParameter.setREF_NO(rs.getString(26));
                    approvalParameter.setINDEX_RATE_ID(rs.getLong(27));
                    approvalParameter.setINDUSTRY_ID(rs.getLong(28));
                    approvalParameter.setPORTFOLIO_ID(rs.getLong(29));
                    approvalParameter.setCHANNEL_ID(rs.getLong(30));
                    approvalParameter.setCHANNEL_CODE(rs.getString(31));
                    approvalParameter.setUSER_ID(rs.getString(32));
                    approvalParameter.setREPMNT_METHOD(rs.getString(33));
                    approvalParameter.setMULTIPLE_DISB_PM(rs.getString(34));
                    approvalParameter.setTRAMSN_TIME(rs.getLong(35));
                    approvalParameter.setTXN_NARRATION(rs.getString(36));
                    approvalParameter.setSYS_USER_ID(rs.getString(37));
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return approvalParameter;
    }

    public GroupMemberParameter queryGrpMemberDetail(String acctNo, String refNo)
    {
        GroupMemberParameter memberParameter = new GroupMemberParameter();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT C.CUST_ID,C.CUST_NO, B.GROUP_MEMBER_ID,B.GROUP_CUST_ID, "
                    + "E.CRNCY_ID,A.ACCT_NO ,E.PROD_ID, D.GROUP_LOAN_ID,A.ACCT_ID "
                    + "FROM " + BRController.CoreSchemaName + ".ACCOUNT A, " + BRController.CoreSchemaName + ".GROUP_MEMBER B, " + BRController.CoreSchemaName + ".CUSTOMER C, " + BRController.CoreSchemaName + ".GROUP_LOAN D," + BRController.CoreSchemaName + ".PRODUCT E "
                    + "WHERE A.CUST_ID = B.MEMBER_CUST_ID "
                    + "AND B.MEMBER_CUST_ID = C.CUST_ID "
                    + "AND D.CUST_ID = B.GROUP_CUST_ID "
                    + "AND D.PROD_ID = E.PROD_ID "
                    + "AND A.ACCT_NO = '" + acctNo.trim() + "' "
                    + "AND D.REF_NO = '" + refNo.trim() + "' "))
            {
                while (rs.next())
                {
                    memberParameter.setGrpMemberCustId(rs.getLong(1));
                    memberParameter.setGrpMemberAcctNo(rs.getString(2));
                    memberParameter.setGrpMemberId(rs.getLong(3));
                    memberParameter.setGrpCustId(rs.getLong(4));
                    memberParameter.setGrpLoanCrncyId(rs.getLong(5));
                    memberParameter.setGrpMemberAcctNo(rs.getString(6));
                    memberParameter.setGrpLoanProdId(rs.getLong(7));
                    memberParameter.setGrpLoanId(rs.getLong(8));
                    memberParameter.setGrpmemberAcctId(rs.getLong(9));
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return memberParameter;
    }

    public String getMembershipNo(String grpActId)
    {
        String grouMember = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT MEMBERSHIP_REF_NO from group_member where GROUP_CUST_ID = " + grpActId + ""))
            {
                while (rs.next())
                {
                    grouMember = rs.getString(1);
                }
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("Member No Found", ex);
        }
        return grouMember;
    }

    public CNAccount queryAccount(String abrv)
    {
        CNAccount cNAccount = new CNAccount();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT BILLER_ACCT_NO,ACCT_ID,ACCOUNT_NAME,ACCOUNT_ABVR FROM " + BRController.CMSchemaName + ".BILLER_ACCOUNT  "
                    + "WHERE ACCOUNT_ABVR LIKE '" + abrv + "%'"))
            {
                while (rs.next())
                {
                    cNAccount.setAccountNumber(rs.getString(1));
                    cNAccount.setAcctId(rs.getLong(2));
                    cNAccount.setAccountName(rs.getString(3));
                    cNAccount.setAccountType(rs.getString(4));
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return cNAccount;
    }

    public String unmaskGLAccount(String glAccount, long buId)
    {
        if (glAccount != null)
        {
            if (glAccount.contains("***"))
            {
                try
                {
                    try (ResultSet rs = executeQueryToResultSet("SELECT GL_PREFIX_CD FROM " + BRController.CoreSchemaName + ".BUSINESS_UNIT WHERE BU_ID=" + buId))
                    {
                        if (rs.next())
                        {
                            glAccount = rs.getString("GL_PREFIX_CD") + glAccount.substring(glAccount.indexOf("***") + 3);
                        }
                    }
                }
                catch (Exception ex)
                {
                    if (getXapiCaller() != null)
                    {
                        getXapiCaller().logException(ex);
                    }
                    else
                    {
                        ArgosMain.bRLogger.logError("ERROR", ex);
                    }
                }
            }
        }
        return glAccount;
    }

    public long getAccountId(String accountNumber)
    {
        long acctId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCT_ID FROM " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + accountNumber + "'"))
            {
                if (rs.next())
                {
                    acctId = rs.getLong("ACCT_ID");
                }
            }
        }
        catch (Exception ex)
        {

            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return acctId;
    }

    public long getQueueId(String grpLoanNo)
    {
        long queuId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT QUEUE_ID FROM " + BRController.CoreSchemaName + ".WF_WORK_ITEM WHERE ITEM_REF_NO = '" + grpLoanNo + "'"))
            {
                if (rs.next())
                {
                    queuId = rs.getLong("QUEUE_ID");
                }
            }
        }
        catch (Exception ex)
        {

            ArgosMain.bRLogger.logError("getQueueId - ERROR", ex);
        }
        return queuId;
    }

    public String querryGroupLoanNumber(Long loanId)
    {
        String groupLoanNumber = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT REF_NO FROM " + BRController.CoreSchemaName + ".GROUP_LOAN A WHERE A.GROUP_LOAN_ID =" + loanId + ""))
            {
                if (rs.next())
                {
                    groupLoanNumber = rs.getString("REF_NO");
                }
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("ERROR", ex);

        }
        return groupLoanNumber;
    }

    public String querryExistingGroupLoanNo(TXRequest tXRequest)
    {
        String groupLoanNumber = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT NVL(MAX(GRP_LOAN_REF),'') as GRP_LOAN_REF FROM  " + BRController.CMSchemaName + ".BL_ARGOS_TXN_LOG "
                    + "WHERE LOAN_AMOUNT = " + tXRequest.getLoanAmount() + " AND LOAN_START_DATE = '" + tXRequest.getLnStartDate() + "' AND TERM_VALUE = " + tXRequest.getTermValue() + " "
                    + "AND TERM_CODE = '" + tXRequest.getTermCode() + "' AND GRP_CRNCY_ID = " + tXRequest.getLoanCrncyId() + " AND GRP_CUST_ID = " + tXRequest.getCustomerId() + " "
                    + "AND GRP_CUST_NO = '" + tXRequest.getCustomerNo().trim() + "' AND RETURN_CODE = '01'"))
            {
                if (rs.next())
                {
                    groupLoanNumber = rs.getString("GRP_LOAN_REF");
                }
                else
                {
                    groupLoanNumber = "";
                }
            }
        }
        catch (Exception ex)
        {
            groupLoanNumber = "";
            ArgosMain.bRLogger.logError("ERROR", ex);

        }
        return groupLoanNumber;
    }

    public ResultSet queryIndividualLoan()
    {
        return executeQueryToResultSet("SELECT CUST_NO,CUSTOMER_NAME,LOAN_AMT,LN_START_DATE,MATURITY_DATE,STATUS FROM " + BRController.CMSchemaName + ".ARGOS_INDIVIDUAL_LOAN "
                + "WHERE TO_DATE(LN_START_DATE,'dd/mm/yyyy') >= (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy')-1 FROM " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02' )");
    }

    public ResultSet queryGroupLoan()
    {
        return executeQueryToResultSet("SELECT GRP_CUST_NO AS CUST_NO,GRP_ACCT_NO AS ACCOUNT_NO,LOAN_AMOUNT,LOAN_START_DATE,MAT_DATE AS MATURITY_DATE,RETURN_CODE AS STATUS FROM " + BRController.CMSchemaName + ".BL_ARGOS_TXN_LOG "
                + " WHERE TO_DATE(LOAN_START_DATE,'dd/mm/yyyy') >= (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy')-1 FROM " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02' )");
    }

    public ResultSet queryattachedMembers()
    {
        return executeQueryToResultSet("SELECT GRP_LOAN_NO AS GROUP_LOAN_NO,ACCT_NO AS ACCOUNT_NO,AMOUNT AS LOAN_AMOUNT,TO_DATE((SUBSTR(DATETIME, 9, 2)||'/'||SUBSTR(DATETIME, 6, 2)||'/'||SUBSTR(DATETIME, 1, 4)),'DD/MM/YYYY') AS CREATE_DT, RETURN_CODE AS STATUS FROM " + BRController.CMSchemaName + ".BL_ARGOS_MEMBERS "
                + "WHERE TO_DATE((SUBSTR(DATETIME, 9, 2)||'/'||SUBSTR(DATETIME, 6, 2)||'/'||SUBSTR(DATETIME, 1, 4)),'dd/mm/yyyy') >= (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy')-1 FROM " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02' )"
        );
    }

    public long getCustomerId(String custNo)
    {
        long acctId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT CUST_ID FROM " + BRController.CoreSchemaName + ".CUSTOMER WHERE CUST_NO='" + custNo.trim() + "'"))
            {
                if (rs.next())
                {
                    acctId = rs.getLong("CUST_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return acctId;
    }

    public String getCustomerNo(String acctNo)
    {
        String acctId = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT CUST_NO FROM " + BRController.CoreSchemaName + ".CUSTOMER A, " + BRController.CoreSchemaName + ".ACCOUNT B "
                    + "WHERE A.CUST_ID = B.CUST_ID AND B.ACCT_NO = '" + acctNo.trim() + "'"))
            {
                if (rs.next())
                {
                    acctId = rs.getString("CUST_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return acctId;
    }

    public long getBankOfficerId(String userName)
    {
        long userId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT SYSUSER_ID FROM " + BRController.CoreSchemaName + ".SYSUSER WHERE LOGIN_ID ='" + userName + "'"))
            {
                if (rs.next())
                {
                    userId = rs.getLong("SYSUSER_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return userId;
    }

    public Long getAccountProductId(String accountNumber)
    {
        Long productId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT PROD_ID FROM " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + accountNumber.trim() + "'"))
            {
                if (rs.next())
                {
                    productId = rs.getLong("PROD_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return productId;
    }

    public Long getProductId(String prodCd)
    {
        Long productId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT PROD_ID FROM " + BRController.CoreSchemaName + ".PRODUCT "
                    + "WHERE PROD_CD='" + prodCd.trim() + "'"))
            {
                if (rs.next())
                {
                    productId = rs.getLong("PROD_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return productId;
    }

    public String getAccountProfileName(String accountNumber)
    {
        String profileName = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCT_NM FROM " + BRController.CoreSchemaName + ".V_ACCOUNTS WHERE ACCT_NO='" + accountNumber.trim() + "'"))
            {
                if (rs.next())
                {
                    profileName = rs.getString("ACCT_NM");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return profileName;
    }

    public String querryServiceCode(Long channelID)
    {

        String channelCode = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT CHANNEL_CD FROM " + BRController.CoreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + channelID + ""))
            {
                if (rs.next())
                {
                    channelCode = rs.getString("CHANNEL_CD");
                }
                else
                {
                    ArgosMain.bRLogger.logEvent("INFO", "CHANNEL NOT FOUND");
                }
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("ERROR", ex);

        }
        return channelCode;
    }

    public String removeSpaces(String text)
    {
        text = text == null ? "" : text;
        StringBuilder buffer = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(text);
        try
        {
            while (tokenizer.hasMoreTokens())
            {
                buffer.append(" ").append(tokenizer.nextToken());
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return buffer.toString().trim();
    }

    public String fetchJournalId(String accountNumber, BigDecimal txnAmount)
    {
        String journalId = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT MAX(TRAN_JOURNAL_ID) TRAN_JOURNAL_ID "
                    + "FROM " + BRController.CoreSchemaName + ".TXN_JOURNAL "
                    + "WHERE ACCT_NO='" + accountNumber + "' AND TRAN_AMT=" + txnAmount + " AND SYS_CREATE_TS > TO_DATE(SYSDATE-1)"))
            {
                if (rs.next())
                {
                    journalId = rs.getString("TRAN_JOURNAL_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return journalId;
    }

    public boolean LogArgosIndividualLoan(TXRequest tXRequest, String returnCode)//
    {
        try
        {
            return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".ARGOS_INDIVIDUAL_LOAN"
                    + "(APPL_ID,CUSTOMER_NAME ,CHANNEL_ID ,LOAN_AMT ,CURRENCY_ID ,TERM_VALUE ,TERM_CODE ,BU_ID ,"
                    + "USER_LOGIN_ID ,CREDIT_TY_ID ,CUST_NO ,ACCT_ID ,LN_START_DATE ,PRODUCT_ID ,"
                    + "ACCT_NUMBER ,PURPOSE_OF_CREDIT ,PORTFOLIO_ID ,REF_ID ,RATE_TY,INDEX_RATE_TY ,"
                    + "INDUSTRY_ID ,ACCESS_CODE ,TRANSMISSION_TIME ,MATURITY_DATE, STATUS )VALUES"
                    + "('" + tXRequest.getApplID() + "','" + tXRequest.getCustomerName() + "'," + tXRequest.getChannelId() + "," + tXRequest.getLoanAmount() + ","
                    + "" + tXRequest.getCurrencyId() + "," + tXRequest.getTermValue() + ",'" + tXRequest.getTermCode() + "'," + tXRequest.getBusinessUnitId() + ","
                    + "" + tXRequest.getBankOfficerId() + "," + tXRequest.getCreditTypeId() + ",'" + tXRequest.getCustomerNo().trim() + "'," + tXRequest.getAccountId() + ","
                    + "'" + tXRequest.getLnStartDate() + "'," + tXRequest.getProduct_id() + ",'" + tXRequest.getAccountNumber1().trim() + "'," + tXRequest.getPurposeOfCredit() + ","
                    + "" + tXRequest.getPortfolioId() + ",'" + tXRequest.getReference() + "','" + tXRequest.getRateType() + "'," + tXRequest.getIndexRateId() + ","
                    + "" + tXRequest.getIndustryId() + ",'" + tXRequest.getAccessCode() + "','" + tXRequest.getTransmissionTime() + "','" + tXRequest.getMaturityDate() + "','" + returnCode + "')", true);

        }
        catch (Exception e)
        {
            ArgosMain.bRLogger.logError("LogTxn", e);
            return false;
        }
    }

    public boolean updateArgosGrpLoanLog(TXRequest tXRequest, String status)
    {
        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".BL_ARGOS_TXN_LOG SET RETURN_CODE='" + status + "' "
                + "WHERE LOAN_AMOUNT = " + tXRequest.getLoanAmount() + " AND LOAN_START_DATE = '" + tXRequest.getLnStartDate() + "' AND TERM_VALUE = " + tXRequest.getTermValue() + " "
                + "AND TERM_CODE = '" + tXRequest.getTermCode().trim() + "' AND GRP_CRNCY_ID = " + tXRequest.getLoanCrncyId() + " AND GRP_CUST_ID = " + tXRequest.getCustomerId() + " "
                + "AND GRP_CUST_NO = '" + tXRequest.getCustomerNo().trim() + "' AND RETURN_CODE = '01'", true);
    }

    public boolean updateArgosIndvLoanLog(TXRequest tXRequest, String status)
    {
        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".ARGOS_INDIVIDUAL_LOAN SET STATUS='" + status + "' "
                + "WHERE APPL_ID = " + tXRequest.getApplID() + " AND ACCT_ID = " + tXRequest.getAccountId() + "", true);
    }

    public boolean LogArgosTxn(TXRequest tXRequest, String returnCode)//
    {
        try
        {
            return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BL_ARGOS_TXN_LOG("
                    + "GRP_LOAN_ID,GRP_LOAN_REF,ACCESS_CODE ,GRP_CUST_ID ,LOAN_PROD_ID ,GRP_CUST_NO ,GRP_ACCT_NO ,GRP_CR_TY ,GRP_PROD_COMBINATION ,GRP_CRNCY_ID , "
                    + "LOAN_AMOUNT ,LOAN_START_DATE ,REPMNT_START_DATE ,TERM_VALUE ,TERM_CODE ,MAT_DATE ,RATE_TY ,PURPOSE_CR ,OFFICER_ID ,BU_ID , "
                    + "PMT_TY ,REPAY_FREQ_CD ,REPAY_FREQ_VAL,DISB_DATE ,DISB_METHOD ,REF_NO ,INDEX_RATE_ID,INDUSTRY_ID,PORTFOLIO_ID,CHANNEL_ID,CHANNEL_CODE, "
                    + "USER_ID,REPMNT_METHOD ,MULTIPLE_DISB_PM,TRAMSN_TIME,TXN_NARRATION ,SYS_USER_ID,LOAN_AMOUNT_LIMIT,DATETIME,RETURN_CODE,SUPERVISOR_ID )"
                    + "VALUES (" + tXRequest.getGroupLoanId() + ",'" + tXRequest.getGrpLoanReference().trim() + "','" + tXRequest.getAccessCode() + "'," + tXRequest.getCustomerId() + "," + tXRequest.getLoanProductId() + ",'" + tXRequest.getCustomerNo().trim() + "',"
                    + "'" + tXRequest.getAccountNumber1().trim() + "'," + tXRequest.getCreditTypeId() + ",'" + tXRequest.getProductCombination() + "'," + tXRequest.getLoanCrncyId() + ","
                    + "" + tXRequest.getLoanAmount() + ",'" + tXRequest.getLnStartDate() + "','" + tXRequest.getRepaymentStartDate() + "'," + tXRequest.getTermValue() + ",'" + tXRequest.getTermCode() + "',"
                    + "'" + tXRequest.getMaturityDate() + "','" + tXRequest.getRateType() + "'," + tXRequest.getPurposeOfCredit() + "," + tXRequest.getBankOfficerId() + "," + tXRequest.getBusinessUnitId() + ","
                    + "'" + tXRequest.getPaymentType() + "','" + tXRequest.getRepayFreqCd() + "'," + tXRequest.getRepayFreqValue() + ",'" + tXRequest.getDisbursementDate() + "','" + tXRequest.getDisbursementMethod() + "',"
                    + "'" + tXRequest.getReferenceNumber() + "'," + tXRequest.getIndexRateId() + "," + tXRequest.getIndustryId() + "," + tXRequest.getPortfolioId() + "," + tXRequest.getChannelId() + ",'" + tXRequest.getChannelCode() + "',"
                    + "'" + tXRequest.getUserLoginId() + "','" + tXRequest.getRepaymentMethod() + "','" + tXRequest.getMultipleDisbPerMember() + "'," + tXRequest.getTransmissionTime() + ",'" + tXRequest.getTxnNarration() + "','" + tXRequest.getUserLoginId() + "',"
                    + "" + tXRequest.getLoanAmtLimit() + ",'" + STformatter.format(timestamp) + "','" + returnCode + "','" + tXRequest.getSupervisorId() + "')", true);

        }
        catch (Exception e)
        {
            ArgosMain.bRLogger.logError("LogTxn", e);
            return false;
        }
    }

    public boolean LogArgosBeneficiaryAttach(TXRequest tXRequest, String returnCode)//
    {
        try
        {
            return executeUpdate("INSERT INTO BL_ARGOS_MEMBERS(ACCT_NO,AMOUNT,GRP_LOAN_NO,SIC_CODE,DATETIME,RETURN_CODE)"
                    + "VALUES('" + tXRequest.getAccountNumber1() + "'," + tXRequest.getLoanAmount() + ","
                    + "'" + tXRequest.getCustomerNo().trim() + "','" + tXRequest.getIndustryId() + "','" + STformatter.format(timestamp) + "','" + returnCode + "')", true);

        }
        catch (Exception e)
        {
            ArgosMain.bRLogger.logError("LogTxn", e);
            return false;
        }
    }

    public boolean upsertCustomField(long fieldId, long parentId, long fieldValue)
    {
        if (executeUpdate("MERGE INTO " + BRController.CoreSchemaName + ".UDS_FIELD_VALUE D USING (SELECT (SELECT NEXT_NO + 1 FROM " + BRController.CoreSchemaName + ".ENTITY WHERE ENTITY_NM = 'UDS_FIELD_VALUE') AS UDS_FIELD_VALUE_ID, " + fieldId + " AS FIELD_ID, " + parentId + " AS PARENT_ID, '" + fieldValue + "' AS FIELD_VALUE, 'A' AS REC_ST, 1 AS VERSION_NO, SYSDATE AS ROW_TS, 'SYSTEM' AS USER_ID, SYSDATE AS CREATE_DT, 'SYSTEM' AS CREATED_BY, SYSDATE AS SYS_CREATE_TS FROM DUAL) S ON (D.FIELD_ID = S.FIELD_ID AND D.PARENT_ID=S.PARENT_ID) WHEN MATCHED THEN UPDATE SET D.FIELD_VALUE = S.FIELD_VALUE WHEN NOT MATCHED THEN INSERT (UDS_FIELD_VALUE_ID, FIELD_ID, PARENT_ID, FIELD_VALUE, REC_ST, VERSION_NO, ROW_TS, USER_ID, CREATE_DT, CREATED_BY, SYS_CREATE_TS) VALUES(S.UDS_FIELD_VALUE_ID, S.FIELD_ID, S.PARENT_ID, S.FIELD_VALUE, S.REC_ST, S.VERSION_NO, S.ROW_TS, S.USER_ID, S.CREATE_DT, S.CREATED_BY, S.SYS_CREATE_TS)", true))
        {
            return updateEntityId();
        }
        return false;
    }

    public String getChannelContraGL(long channelId, long buId)
    {
        String drContraGL = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT GL_DR_ACCT FROM " + BRController.CoreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + channelId))
            {
                if (rs.next())
                {
                    drContraGL = rs.getString("GL_DR_ACCT");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return unmaskGLAccount(drContraGL, buId);
    }

    public Long getDefaultBuid()
    {
        if (DefaultBuId == null)
        {
            try
            {
                try (ResultSet rs = executeQueryToResultSet("SELECT ORIGIN_BU_ID FROM " + BRController.CoreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + BRController.ChannelID))
                {
                    if (rs.next())
                    {
                        DefaultBuId = rs.getLong("ORIGIN_BU_ID");
                    }
                }
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ArgosMain.bRLogger.logError("ERROR", ex);
                }
            }
        }
        return DefaultBuId;
    }

    public Long geBuid(String branchCode)
    {
        Long BuId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT BU_ID FROM " + BRController.CoreSchemaName + ".BUSINESS_UNIT WHERE BU_CD ='" + branchCode + "'"))
            {
                if (rs.next())
                {
                    BuId = rs.getLong("BU_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }

        return BuId;
    }

    private boolean updateEntityId()
    {
        return executeUpdate("UPDATE " + BRController.CoreSchemaName + ".ENTITY SET NEXT_NO=(SELECT MAX(UDS_FIELD_VALUE_ID)+1 FROM " + BRController.CoreSchemaName + ".UDS_FIELD_VALUE) WHERE ENTITY_NM = 'UDS_FIELD_VALUE'", true);
    }

    private String formatDate(Date date)
    {
        if (date != null)
        {
            return "TO_DATE('" + new SimpleDateFormat("dd-MM-yyyy").format(date) + "','DD-MM-YYYY')";
        }
        return null;
    }

    public Object[][] queryBusinessUnits()
    {
        return executeQueryToArray("SELECT BU_ID, BU_NM FROM " + BRController.CoreSchemaName + ".BUSINESS_UNIT WHERE REC_ST='A' ORDER BY BU_NO");
    }

    public Object[][] queryCurrencies()
    {
        return executeQueryToArray("SELECT CRNCY_CD, CRNCY_NM FROM " + BRController.CoreSchemaName + ".CURRENCY WHERE REC_ST='A' ORDER BY CRNCY_CD");
    }

    public Object[][] queryProducts()
    {
        return executeQueryToArray("SELECT PROD_ID, PROD_DESC FROM " + BRController.CoreSchemaName + ".PRODUCT WHERE REC_ST='A' ORDER BY PROD_CD");
    }

    public Object[][] queryProducts(int productId)
    {
        return executeQueryToArray("SELECT PROD_ID, PROD_DESC FROM " + BRController.CoreSchemaName + ".PRODUCT WHERE REC_ST='A' AND PROD_ID=" + productId + " ORDER BY PROD_CD");
    }

    public Date getCurrentDate()
    {
        Date currentDate = new Date();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT SYSDATE FROM DUAL"))
            {
                if (rs.next())
                {
                    currentDate = rs.getDate(1);
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return currentDate;
    }

    public String getMaturityDate(String startDate, Long freqValue)
    {
        String currentDate = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT TO_CHAR((TO_DATE('" + startDate + "','dd/MM/yyyy') + (" + freqValue + "*" + numberOfDays + ")),'dd/MM/yyyy') FROM DUAL "))
            {

                if (rs.next())
                {
                    currentDate = rs.getString(1);
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return currentDate;
    }

    public String capitalize(String name)
    {
        if (name != null)
        {
            try
            {
                StringBuilder builder = new StringBuilder();
                for (String word : name.toLowerCase().split("\\s"))
                {
                    builder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
                }
                return builder.toString();
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ArgosMain.bRLogger.logError("ERROR", ex);
                }
                return name;
            }
        }
        return name;
    }

    public void selectItemByCode(JComboBox box, String code)
    {
        for (int i = 0; i < box.getItemCount(); i++)
        {
            if (box.getItemAt(i).toString().startsWith(code + "~"))
            {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    public String replaceAll(String message, String holder, String replacement)
    {
        if (message != null)
        {
            replacement = replacement == null ? "<>" : replacement;
            while (message.contains(holder) && !replacement.equals(holder))
            {
                message = message.replace(holder, replacement);
            }
        }
        return message;
    }

    public BFValue queryPcConfig()
    {
        BFValue bfValue = new BFValue();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select PARAM_VALUE,DISPLAY_VALUE from " + BRController.CoreSchemaName + ".ctrl_parameter where param_cd = 'S04'"))
            {
                if (rs.next())
                {
                    bfValue.setDISPLAY_VALUE(rs.getString("PARAM_VALUE"));
                    bfValue.setPARAM_VALUE(rs.getString("DISPLAY_VALUE"));
                }
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return bfValue;
    }

    public int acctId(String acct)
    {
        int act_no = 0;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCT_ID FROM " + BRController.CoreSchemaName + ".ACCOUNT A WHERE ACCT_NO = '" + acct.trim() + "'"))
            {
                if (rs.next())
                {
                    act_no = rs.getInt("ACCT_ID");
                }
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("ERROR", ex);
        }
        return act_no;
    }

    public Date getProcessingDate()
    {
        Date currentDate = new Date();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT TO_CHAR(TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY'),'DD/MM/YYYY') AS CURRENT_DATE FROM " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02'"))
            {
                if (rs.next())
                {
                    currentDate = rs.getDate("CURRENT_DATE");
                }
            }
        }
        catch (Exception ex)
        {
            // ArgosMain.bRLogger.logError("ERROR-getProcessingDate()", ex);
        }
        return currentDate;
    }

    /**
     * @return the dbConnection
     */
    public Connection getDbConnection()
    {
        return dbConnection;
    }

    /**
     * @param dbConnection the dbConnection to set
     */
    public void setDbConnection(Connection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    /**
     * @return the logTxnStatement
     */
    public CallableStatement getLogTxnStatement()
    {
        return logTxnStatement;
    }

    /**
     * @param logTxnStatement the logTxnStatement to set
     */
    public void setLogTxnStatement(CallableStatement logTxnStatement)
    {
        this.logTxnStatement = logTxnStatement;
    }

    public int countPendingAlerts()
    {
        int count = 0;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT REC_ST FROM " + BRController.CMSchemaName + ".ALERTS WHERE REC_ST='P'"))
            {
                count = getRowCount(rs);
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return count;
    }

    public long getAccountBuId(String accountNumber)
    {
        long buId = getDefaultBuid();
        System.out.println("get schema " + BRController.CoreSchemaName);
        System.out.println("get account  " + accountNumber);
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT MAIN_BRANCH_ID FROM " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + accountNumber.trim() + "'"))
            {
                if (rs.next())
                {
                    buId = rs.getLong("MAIN_BRANCH_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return buId;
    }

    public String getGlAccount(String ledgerNumber)
    {

        System.err.println("<>>>>>>>>>>>>>>>>>>>>>>>>>>.......<><><>" + ledgerNumber);
        long buId = -88;
        String glAcctNo = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT GL_ACCT_NO FROM " + BRController.CoreSchemaName + ".GL_ACCOUNT WHERE LEDGER_NO= '" + Integer.parseInt(ledgerNumber) + "' AND BU_ID = " + buId + ""))
            {
                if (rs.next())
                {
                    glAcctNo = rs.getString("GL_ACCT_NO");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return glAcctNo;
    }

    public String padString(String s, int i, char c, boolean leftPad)
    {
        StringBuilder buffer = new StringBuilder(s);
        int j = buffer.length();
        if (i > 0 && i > j)
        {
            for (int k = 0; k <= i; k++)
            {
                if (leftPad)
                {
                    if (k < i - j)
                    {
                        buffer.insert(0, c);
                    }
                }
                else if (k > j)
                {
                    buffer.append(c);
                }
            }
        }
        return buffer.toString().substring(0, i);
    }

    public String formatIsoAmount(BigDecimal amount)
    {
        String amtStr = padString(amount.abs().setScale(2, BigDecimal.ROUND_DOWN).toPlainString().replace(".", ""), 12, '0', true);
        return amtStr.substring(amtStr.length() - 12);
    }

    public void swapAccounts(TXRequest tXRequest)
    {
        String debitAcct = tXRequest.getAccountNumber2();
        tXRequest.setAccountNumber2(tXRequest.getAccountNumber1());
        tXRequest.setAccountNumber1(debitAcct);
    }

//    public boolean isAccountRestricted(String accountNumber) {
//        boolean accountRestricted = true;
//        try {
//            try (ResultSet resultSet = executeQueryToResultSet("SELECT ACCT_NO FROM " + BRController.CoreSchemaName + ".V_ACCOUNTS WHERE PROD_CAT_TY='DP' AND ACCT_NO='" + accountNumber + "' AND PROD_CD IN (" + BRController.AllowedProductCodes + ")")) {
//                if (resultSet.next()) {
//                    accountRestricted = false;
//                } else {
//                   // getXapiCaller().setXapiRespCode(EICodes.TRANSACTION_NOT_ALLOWED_FOR_ACCOUNT);
//                }
//            }
//        } catch (Exception ex) {
//           // getXapiCaller().setXapiRespCode(EICodes.TRANSACTION_NOT_ALLOWED_FOR_ACCOUNT);
//            getXapiCaller().logException(ex);
//        }
//        return accountRestricted;
//    }
    public String fetchJournalId(TXRequest tXRequest)
    {
        String journalId = null;
        try
        {
            try (ResultSet resultSet = executeQueryToResultSet("SELECT MAX(TRAN_JOURNAL_ID) TRAN_JOURNAL_ID FROM " + BRController.CoreSchemaName + ".TXN_JOURNAL WHERE ACCT_NO='" + tXRequest.getAccountNumber1() + "' AND TRAN_AMT=" + tXRequest.getTxnAmount() + " AND TRAN_DESC='" + tXRequest.getTxnNarration() + "' AND SYS_CREATE_TS > TO_DATE(SYSDATE-1)"))
            {
                if (resultSet.next())
                {
                    journalId = resultSet.getString("TRAN_JOURNAL_ID");
                }
            }
        }
        catch (Exception ex)
        {
            getXapiCaller().logException(ex);
        }
        return journalId;
    }

    public Object[][] executeQueryToArray(String query)
    {
        return rsToArray(executeQuery(query, true));
    }

    public ResultSet executeQueryToResultSet(String query)
    {
        return executeQuery(query, true);
    }

    public int getRowCount(ResultSet rs)
    {
        int records = 0;
        try
        {
            rs.last();
            records = rs.getRow();
            rs.beforeFirst();
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ArgosMain.bRLogger.logError("ERROR", ex);
            }
        }
        return records;
    }

    private Object[][] rsToArray(ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                int row = 0, records = getRowCount(rs), fields = rs.getMetaData().getColumnCount();
                Object[][] results = (records == 0) ? new Object[0][0] : new Object[records][fields];
                while (rs.next())
                {
                    for (int col = 0; col < fields; col++)
                    {
                        results[row][col] = rs.getObject(col + 1);
                    }
                    row++;
                }
                try
                {
                    rs.close();
                }
                catch (Exception ex)
                {
                    if (getXapiCaller() != null)
                    {
                        getXapiCaller().logException(ex);
                    }
                    else
                    {
                        ArgosMain.bRLogger.logError("ERROR", ex);
                    }
                }
                return results;
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ArgosMain.bRLogger.logError("ERROR", ex);
                }
            }
        }
        return new Object[0][0];
    }

    private ResultSet executeQuery(String query, boolean retry)
    {
        try
        {
            if ("Y".equalsIgnoreCase(BRController.EnableDebug))
            {
                ArgosMain.bRLogger.logEvent(query);

            }
            if (getDbConnection() == null)
            {
                connectToDB();
            }
            else if (getDbConnection().isClosed())
            {
                connectToDB();
            }
            if (getDbConnection() != null)
            {
                return getDbConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
            }
        }
        catch (Exception ex)
        {
            if (String.valueOf(ex.getMessage()).contains("ORA-01000"))
            {
                dispose();
                if (retry)
                {
                    return executeQuery(query, false);
                }
            }
            else
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ArgosMain.bRLogger.logError("ERROR", ex);
                }
            }
        }
        return null;
    }

    public boolean executeUpdate(String update, boolean retry)
    {
        try
        {
            if ("Y".equalsIgnoreCase(BRController.EnableDebug))
            {
                ArgosMain.bRLogger.logEvent("logTransaction", update);
            }
            if (getDbConnection() == null)
            {
                connectToDB();
            }
            else if (getDbConnection().isClosed())
            {
                connectToDB();
            }
            if (getDbConnection() != null)
            {
                update = update.replaceAll("'null'", "NULL").replaceAll("'NULL'", "NULL");
                getDbConnection().createStatement().executeUpdate(update);
                return true;
            }
        }
        catch (Exception ex)
        {
            if (String.valueOf(ex.getMessage()).contains("ORA-01000"))
            {
                dispose();
                if (retry)
                {
                    return executeUpdate(update, false);
                }
            }
            else
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ArgosMain.bRLogger.logError("executeUpdate", ex);
                }
            }
        }
        return false;
    }

    public void dispose()
    {
        try
        {
            if (getLogTxnStatement() != null)
            {
                getLogTxnStatement().close();
            }
            if (getDbConnection() != null)
            {
                getDbConnection().close();
            }
        }
        catch (Exception ex)
        {
            setLogTxnStatement(null);
            setDbConnection(null);
        }
    }

    private void logTransaction(TXRequest tXRequest, String respCode, String procCode, String utilType, String pmtType)
    {
        try
        {
            connectToDB();
            getLogTxnStatement().setString(1, tXRequest.getReference());
            getLogTxnStatement().setLong(2, tXRequest.getChannelId());
            getLogTxnStatement().setString(3, tXRequest.getAccessCode());

            getLogTxnStatement().setString(4, tXRequest.getAccountNumber1());
            getLogTxnStatement().setString(5, tXRequest.getAccountNumber2());

            getLogTxnStatement().setString(6, tXRequest.getCurrencyCode());
            getLogTxnStatement().setBigDecimal(7, tXRequest.getTxnAmount());

            getLogTxnStatement().setBigDecimal(8, tXRequest.getChargeAmount());
            getLogTxnStatement().setString(9, tXRequest.getTxnNarration());

            getLogTxnStatement().setString(10, respCode);
            getLogTxnStatement().setString(11, "");
            //getLogTxnStatement().setString(15, BRController.getXapiMessage(respCode));
            getLogTxnStatement().setString(12, successResponse.equals(respCode) ? "APPROVED" : "REJECTED");

            getLogTxnStatement().setString(13, respCode);
            getLogTxnStatement().setString(14, txnJournalId);

            getLogTxnStatement().setString(15, chargeJournal);
            getLogTxnStatement().setString(16, procCode);

            getLogTxnStatement().setString(17, utilType);
            getLogTxnStatement().setString(18, "N");
            getLogTxnStatement().setString(19, pmtType);
            // getLogTxnStatement().setString(20, tXRequest.getReferenceType());

            getLogTxnStatement().execute();

        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logError("logTransaction", ex);

        }
    }

    public  String convertToString(Object object)
    {
        boolean empty = true;
        Class<?> beanClass = object.getClass();
        String text = beanClass.getSimpleName() + "{ ";
        try
        {
            if (!(object instanceof String))
            {
                for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(beanClass).getPropertyDescriptors())
                {
                    if (!"class".equalsIgnoreCase(propertyDesc.getName()))
                    {
                        Method readMethod = propertyDesc.getReadMethod();
                        if (readMethod != null)
                        {
                            Object value = propertyDesc.getReadMethod().invoke(object);
                            if (value != null ? value.getClass().isArray() : false)
                            {
                                text += "<[\r\n";
                                for (Object item : (Object[]) value)
                                {
                                    text += convertToString(item) + "\r\n";
                                    empty = false;
                                }
                                text += "]>";
                            }
                            else
                            {
                                text += (empty ? "" : ", ") + propertyDesc.getName() + "=<" + value + ">";
                                empty = false;
                            }
                        }
                    }
                }
            }
            if (object instanceof List)
            {
                boolean append = false;
                text += (empty ? "" : ", ") + "items=<[ ";
                for (Object item : ((List) object).toArray())
                {
                    text += (append ? ", " : "") + convertToString(item);
                    append = true;
                }
                empty = false;
                text += " ]>";
            }
            if (object instanceof Map)
            {
                boolean append = false;
                text += (empty ? "" : ", ") + "items=<[ ";
                for (Object key : ((Map) object).keySet())
                {
                    text += (append ? ", " : "") + key + "=<" + convertToString(((Map) object).get(key)) + ">";
                    append = true;
                }
                empty = false;
                text += " ]>";
            }
        }
        catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            System.err.println("" + ex);
        }
        return empty ? String.valueOf(object) : text + " }";
    }

    /**
     * @return the xapiCaller
     */
    public XAPICaller getXapiCaller()
    {
        return xapiCaller;
    }

    /**
     * @param xapiCaller the xapiCaller to set
     */
    public void setXapiCaller(XAPICaller xapiCaller)
    {
        this.xapiCaller = xapiCaller;
    }

    /**
     * @return the logTaxTxn
     */
    public CallableStatement getLogTaxTxn()
    {
        return logTaxTxn;
    }

    /**
     * @param logTaxTxn the logTaxTxn to set
     */
    public void setLogTaxTxn(CallableStatement logTaxTxn)
    {
        this.logTaxTxn = logTaxTxn;
    }

}
