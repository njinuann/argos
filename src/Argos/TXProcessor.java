/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import DataHolder.createGroupLoan1;
import DataHolder.ApprovalParameter;
import DataHolder.CreateGroupLoanResponse;
import DataHolder.LoanDataByLoanIdResponse;
import DataHolder.GroupMemberParameter;
import DataHolder.AttachGroupMember;
import DataHolder.CustomerData;
import DataHolder.LoanIDByGroupLoanNumber;
import DataHolder.UpdateGroupLoanResponce;
import DataHolder.LoanIDByGroupLoanNumberResponse;
import DataHolder.LoanDataByLoanId;
import DataHolder.CustomerDetails;
import DataHolder.LoanParameter;
import DataHolder.CreateGroupLoan;
import DataHolder.InsertGroupMemberResponse;
import DataHolder.IndividualLoanUserData;
import DataHolder.IndividualLoanUserDataResponse;
import com.neptunesoftware.supernova.ws.common.XAPIException;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.GroupLoanBeneficiaryOutputData;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.GroupLoanOutputData;
import com.neptunesoftware.supernova.ws.server.loanaccount.data.LoanAccountSummaryOutputData;
import com.neptunesoftware.supernova.ws.server.workflow.data.WFItemOutputData;
import com.neptunesoftware.supernova.ws.server.workflow.data.WorkListOutputData;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pecherk
 */
public final class TXProcessor
{

    int conIndex = -1;
    long startTime = 0;
    private XAPICaller xapiCaller = new XAPICaller();
    private Connection dbConnection = null;
    private CallableStatement logTxnStatement = null;
    private CallableStatement logTaxTxn = null;
    private String txnJournalId, chargeJournal, respCode = "00", respRef;
    private static Long DefaultBuId = null;
    private final String successResponse = "00";
    private final String invalidError = "91";
    public java.util.Date timestamp = new java.util.Date();
    public SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
    public SimpleDateFormat STformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat callformatter = new SimpleDateFormat("dd MMM yyyy");
    public SimpleDateFormat taxDtformat = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat procDtformat = new SimpleDateFormat("dd/MM/yyyy");

    public Long numberOfDays = 7L;
    private final String success = "0";
    private final String ConError = "1";
    private final String invalidAcct = "2";
    private final String memberExists = "3";
    private final String amountGreater = "4";
    private TXUtility tXUtility = new TXUtility();

    public TXProcessor(XAPICaller xapiCaller)
    {
        setXapiCaller(new XAPICaller());
    }

    public TXProcessor()
    {

    }

    private void LogEventMessage()
    {
        ArgosMain.bRLogger.logEvent(xapiCaller.toString());
    }

    /* Argos WS*/
    public CreateGroupLoanResponse createGroupLoan(createGroupLoan1 acctBalReq)
    {
        setXapiCaller(new XAPICaller());
        startTime = System.currentTimeMillis();
        Long grpLoanId, grpLoanNo;
        ArgosMain.bRLogger.logEvent("Create Group Loan Request", gettXUtility().convertToString(acctBalReq));

        CreateGroupLoanResponse createGroupLoanResponse = new CreateGroupLoanResponse();
        TXRequest tXRequest = new TXRequest();

        TXClient tXClient = new TXClient(getXapiCaller());
        LoanParameter groupLoanParameter;

        groupLoanParameter = gettXUtility().queryProductDetails(acctBalReq.getClass_code());
        if (!"".equalsIgnoreCase(acctBalReq.getGroup_RIM()))
        {
            if (!gettXUtility().isCustomerValid(acctBalReq.getGroup_RIM()))
            {
                createGroupLoanResponse.setCreateGroupLoanResult(invalidAcct);
                ArgosMain.bRLogger.logEvent("<Response>", gettXUtility().convertToString(createGroupLoanResponse));
                return createGroupLoanResponse;
            }
        }
        else
        {
            createGroupLoanResponse.setCreateGroupLoanResult(invalidAcct);
            ArgosMain.bRLogger.logEvent("<Response>", gettXUtility().convertToString(createGroupLoanResponse));

            return createGroupLoanResponse;
        }

        tXRequest.setAccessCode("ArgosInterface99999");
        tXRequest.setCustomerId(gettXUtility().getCustomerId(acctBalReq.getGroup_RIM().trim()));

        tXRequest.setLoanProductId(groupLoanParameter.getProdId());
        tXRequest.setCustomerNo(acctBalReq.getGroup_RIM().trim());

        tXRequest.setAccountNumber1(acctBalReq.getGroupe_account().trim());
        tXRequest.setCreditTypeId(new Long(acctBalReq.getCredit_type()));//new Long(779)

        tXRequest.setProductCombination("SING_PROD");//constant
        tXRequest.setLoanCrncyId(groupLoanParameter.getCrncyId());

        tXRequest.setLoanAmount(new BigDecimal(acctBalReq.getLoan_amount()));
        tXRequest.setLnStartDate(acctBalReq.getContract_date());
        tXRequest.setRepaymentStartDate(acctBalReq.getFirst_payment_date());

        tXRequest.setTermValue(Long.parseLong(acctBalReq.getLoan_trm()));
        tXRequest.setTermCode(groupLoanParameter.getTermFreqcode());

        tXRequest.setLoanAmtLimit(new BigDecimal(acctBalReq.getLoan_amt_limit()));
        tXRequest.setMaturityDate(gettXUtility().getMaturityDate(acctBalReq.getContract_date(), Long.parseLong(acctBalReq.getLoan_trm())));                                // compute based on product setings

        tXRequest.setRateType(groupLoanParameter.getRateType());
        tXRequest.setSupervisorId(acctBalReq.getSurpvisor_code());

        tXRequest.setPurposeOfCredit(new Long(447));
        /*has been defaulted awaitng the new argos release to include this parameter*/

        tXRequest.setBankOfficerId(gettXUtility().getBankOfficerId(acctBalReq.getCredit_Officer_Code()));

        tXRequest.setBusinessUnitId(gettXUtility().geBuid(acctBalReq.getBranch_no()));
        tXRequest.setPaymentType(groupLoanParameter.getPmtType());

        tXRequest.setRepayFreqCd(groupLoanParameter.getRepmtFreqCode());
        tXRequest.setRepayFreqValue(groupLoanParameter.getRepmtFreqValue());

        tXRequest.setDisbursementDate(acctBalReq.getContract_date());
        tXRequest.setDisbursementMethod("MA"); //

        tXRequest.setReferenceNumber(acctBalReq.getDescription());
        tXRequest.setIndexRateId(groupLoanParameter.getIndexRateId());
        tXRequest.setIndustryId(2420L);
        /* tXRequest.setIndustryId(Long.valueOf(acctBalReq.getIndustry_code()));//29 jan 2019/ this was again defaulted on request from Daddy to industry id =2420
                                                                                                                                    industry code =40009
                                                                                                                                    industry desc =Grossiste, commerce general*/
 /*has been defaulted awaitng the new argos release to include this parameter*/
//19th march 2018 >> previously mapped to 1980
        tXRequest.setPortfolioId(new Long(11));
        tXRequest.setChannelId(new Long(8));

        tXRequest.setChannelCode("POS");
        tXRequest.setUserLoginId(acctBalReq.getCredit_Officer_Code());

        tXRequest.setRepaymentMethod("MA");
        tXRequest.setMultipleDisbPerMember("S");

        tXRequest.setTransmissionTime(System.currentTimeMillis());
        tXRequest.setTxnNarration(acctBalReq.getDescription());
        tXRequest.setSystemUserID(acctBalReq.getSurpvisor_code());

        getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
        getXapiCaller().setCurrency(tXRequest.getCurrencyId());
        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
        getXapiCaller().setRefNumber(tXRequest.getCustomerNo());
        getXapiCaller().setTxnDescription("Group Loan Creation [" + tXRequest.getReferenceNumber() + "]");
        getXapiCaller().setMainRequest(acctBalReq);
        getXapiCaller().settXRequest(tXRequest);

        ArgosMain.bRLogger.logEvent("<TXRequest>", gettXUtility().convertToString(tXRequest));
        System.err.println("isGroupLoanPending(tXRequest) " + gettXUtility().isGroupLoanPending(tXRequest));
        if (gettXUtility().isGroupLoanPending(tXRequest))
        {
            String groupLoanRef = gettXUtility().querryExistingGroupLoanNo(tXRequest);
            if (!groupLoanRef.equals("") || !groupLoanRef.isEmpty())
            {
                createGroupLoanResponse.setCreateGroupLoanResult(groupLoanRef);
            }
        }
        else
        {
            Object response = tXClient.createGroupLoan(tXRequest);
            if (response instanceof GroupLoanOutputData)
            {
                grpLoanId = (((GroupLoanOutputData) response).getGroupLoanId());
                respRef = String.valueOf(((GroupLoanOutputData) response).getCustomerNo());

                System.out.println("" + respRef);
                String grpLoanReference = gettXUtility().querryGroupLoanNumber(grpLoanId);

                createGroupLoanResponse.setCreateGroupLoanResult(grpLoanReference);
                tXRequest.setGrpLoanReference(grpLoanReference);

                tXRequest.setGroupLoanId(grpLoanId);
                ArgosMain.bRLogger.logEvent("<Response>", gettXUtility().convertToString(createGroupLoanResponse));

                if (gettXUtility().LogArgosTxn(tXRequest, "01"))
                {
                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "SUCCESS!");
                }
                else
                {
                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "FAILED!");
                }

            }
            else if (response instanceof XAPIException)
            {
                createGroupLoanResponse.setCreateGroupLoanResult(ConError);
                ArgosMain.bRLogger.logEvent("ERROR", gettXUtility().convertToString(response));
            }
            else
            {
                createGroupLoanResponse.setResponseCode(ConError);
                ArgosMain.bRLogger.logEvent("GLOBAL ERROR", gettXUtility().convertToString(response));
            }
        }
        getXapiCaller().setMainResponse(createGroupLoanResponse);
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        LogEventMessage();
        return createGroupLoanResponse;
    }

    public InsertGroupMemberResponse insertGroupMember(AttachGroupMember acctBalReq)
    {
        setXapiCaller(new XAPICaller());
        startTime = System.currentTimeMillis();
        Long grpLoanId, grpLoanNo;
        InsertGroupMemberResponse groupMemberResponse = new InsertGroupMemberResponse();

        GroupMemberParameter memberParameter;
        memberParameter = gettXUtility().queryGrpMemberDetail(acctBalReq.getAccountNumber(), acctBalReq.getGroupLoanNo());

        TXRequest tXRequest = new TXRequest();
        TXClient tXClient = new TXClient(getXapiCaller());

        if (!"".equals(acctBalReq.getAccountNumber()))
        {
            if (!gettXUtility().groupMemberExist(memberParameter.getGrpCustId()))
            {
                groupMemberResponse.setInsertGroupMemberResult(Integer.parseInt(invalidAcct));
            }
            if (!gettXUtility().memberLimitExceeded(Double.parseDouble(String.valueOf(acctBalReq.getMemberLoanAmount())), acctBalReq.getGroupLoanNo()))
            {
                groupMemberResponse.setInsertGroupMemberResult(Integer.parseInt(amountGreater));
            }
        }
        else
        {
            System.out.println("Validation Pass");
        }

        tXRequest.setAccessCode("ArgosInterface99999");
        tXRequest.setAccountNumber1(acctBalReq.getAccountNumber());

        tXRequest.setCustomerNo(acctBalReq.getGroupLoanNo());
        tXRequest.setCustomerId(memberParameter.getGrpMemberCustId());

        tXRequest.setMembershipId(memberParameter.getGrpMemberId());
        tXRequest.setGroupLoanId(memberParameter.getGrpLoanId());

        tXRequest.setLoanCrncyId(memberParameter.getGrpLoanCrncyId());
        tXRequest.setLoanAmount(BigDecimal.valueOf(Double.parseDouble(String.valueOf(acctBalReq.getMemberLoanAmount()))));

        tXRequest.setSettlementAcctId(memberParameter.getGrpmemberAcctId());
        tXRequest.setLoanProductId(memberParameter.getGrpLoanProdId());

        tXRequest.setIndustryId(Long.parseLong(acctBalReq.getSicCode()));
        tXRequest.setChannelId(8L);

        tXRequest.setChannelCode("POS");
        tXRequest.setUserLoginId("SYSTEM");

        getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
        getXapiCaller().setCurrency(tXRequest.getCurrencyId());
        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
        getXapiCaller().setRefNumber(acctBalReq.getGroupLoanNo());
        getXapiCaller().setTxnDescription("Attach Beneficiary member [" + tXRequest.getAccountNumber1() + "->" + tXRequest.getCustomerNo() + "]");
        getXapiCaller().setMainRequest(acctBalReq);
        getXapiCaller().settXRequest(tXRequest);
        if (gettXUtility().isGroupMemberAttached(acctBalReq.getGroupLoanNo(), acctBalReq.getAccountNumber()))
        {
            System.out.println("a1cctBalReq 6 " + gettXUtility().isGroupMemberAttached(acctBalReq.getGroupLoanNo(), acctBalReq.getAccountNumber()));
            groupMemberResponse.setInsertGroupMemberResult(Integer.parseInt(memberExists));

        }
        else
        {
            ArgosMain.bRLogger.logEvent("Attach Beneficiary Request", gettXUtility().convertToString(tXRequest));
            Object response = tXClient.attachGroupMembers(tXRequest);

            if (response instanceof GroupLoanBeneficiaryOutputData)
            {
                grpLoanId = (((GroupLoanBeneficiaryOutputData) response).getGroupLoanId());
                respRef = String.valueOf(((GroupLoanBeneficiaryOutputData) response).getMembershipId());

                groupMemberResponse.setInsertGroupMemberResult(0);
                System.out.println("Success attach");
                if (gettXUtility().LogArgosBeneficiaryAttach(tXRequest, respCode))
                {
                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "SUCCESS!");
                }
                else
                {
                    ArgosMain.bRLogger.logEvent("<TXN Logging>", "FAILED!");
                }
            }
            else
            {
                groupMemberResponse.setInsertGroupMemberResult(1);
            }
        }
        ArgosMain.bRLogger.logEvent(acctBalReq.getAccountNumber() + " Attach Beneficiary Response", acctBalReq.getGroupLoanNo() + "~" + gettXUtility().convertToString(groupMemberResponse));
        getXapiCaller().setMainResponse(groupMemberResponse);
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        LogEventMessage();
        return groupMemberResponse;
    }

    public UpdateGroupLoanResponce updateGroupLoan(CreateGroupLoan acctBalReq)
    {
        setXapiCaller(new XAPICaller());
        startTime = System.currentTimeMillis();
        Long grpLoanId = null, grpLoanNo;
        ArgosMain.bRLogger.logEvent("Create Group Loan Request", gettXUtility().convertToString(acctBalReq));

        UpdateGroupLoanResponce createGroupLoanResponse = new UpdateGroupLoanResponce();
        TXRequest tXRequest = new TXRequest();

        TXClient tXClient = new TXClient(getXapiCaller());
        ApprovalParameter approvalParameter;

        approvalParameter = gettXUtility().queryGroupLoanDetails(acctBalReq.getGroupLoanRefNo());

        if (!"".equalsIgnoreCase(acctBalReq.getGroupLoanRefNo()))
        {
            System.err.println("acctBalReq.getGroupLoanRefNo() " + acctBalReq.getGroupLoanRefNo());
            if (!gettXUtility().isGroupLoanValid(acctBalReq.getGroupLoanRefNo()))
            {
                createGroupLoanResponse.setUpdateGroupLoanResult(invalidAcct);
                ArgosMain.bRLogger.logEvent("<Response>", gettXUtility().convertToString(createGroupLoanResponse));

                return createGroupLoanResponse;
            }
        }
        else
        {
            createGroupLoanResponse.setUpdateGroupLoanResult(invalidAcct);
            ArgosMain.bRLogger.logEvent("<Response>", gettXUtility().convertToString(createGroupLoanResponse));

            return createGroupLoanResponse;
        }
        System.err.println(" approvalParameter.getPURPOSE_CR() " + approvalParameter.getGRP_LOAN_ID());
        tXRequest.setGroupLoanId(approvalParameter.getGRP_LOAN_ID());

        tXRequest.setCustomerId(approvalParameter.getGRP_CUST_ID());
        tXRequest.setLoanProductId(approvalParameter.getLOAN_PROD_ID());

        tXRequest.setCustomerNo(approvalParameter.getGRP_CUST_NO().trim());
        getXapiCaller().setRefNumber(approvalParameter.getGRP_LOAN_REF().trim());

        tXRequest.setAccountNumber1(approvalParameter.getGRP_ACCT_NO().trim());
        getXapiCaller().setAccountNo(approvalParameter.getGRP_ACCT_NO().trim());

        tXRequest.setCreditTypeId(approvalParameter.getGRP_CR_TY());
        tXRequest.setProductCombination(approvalParameter.getGRP_PROD_COMBINATION());

        tXRequest.setLoanCrncyId(approvalParameter.getGRP_CRNCY_ID());
        tXRequest.setLoanAmount(approvalParameter.getLOAN_AMOUNT());

        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
        tXRequest.setLnStartDate(approvalParameter.getLOAN_START_DATE());

        tXRequest.setRepaymentStartDate(approvalParameter.getREPMNT_START_DATE());
        tXRequest.setTermValue(approvalParameter.getTERM_VALUE());

        tXRequest.setTermCode(approvalParameter.getTERM_CODE());
        tXRequest.setMaturityDate(approvalParameter.getMAT_DATE());

        tXRequest.setRateType(approvalParameter.getRATE_TY());
        tXRequest.setPurposeOfCredit(Long.parseLong(approvalParameter.getPURPOSE_CR()));

        tXRequest.setBankOfficerId(approvalParameter.getOFFICER_ID());
        tXRequest.setBusinessUnitId(approvalParameter.getBU_ID());

        tXRequest.setPaymentType(approvalParameter.getPMT_TY());
        tXRequest.setRepayFreqCd(approvalParameter.getREPAY_FREQ_CD());

        tXRequest.setRepayFreqValue(approvalParameter.getREPAY_FREQ_VAL());
        tXRequest.setRepaymentStartDate(approvalParameter.getREPMNT_START_DATE());

        tXRequest.setDisbursementDate(approvalParameter.getDISB_DATE());
        tXRequest.setDisbursementMethod(approvalParameter.getDISB_METHOD());

        tXRequest.setReferenceNumber(approvalParameter.getGRP_LOAN_REF());
        getXapiCaller().setTxnDescription(tXRequest.getReferenceNumber());
        tXRequest.setIndexRateId(approvalParameter.getINDEX_RATE_ID());

        tXRequest.setIndustryId(approvalParameter.getINDUSTRY_ID());
        tXRequest.setPortfolioId(approvalParameter.getPORTFOLIO_ID());

        tXRequest.setChannelId(approvalParameter.getCHANNEL_ID());
        tXRequest.setChannelCode(approvalParameter.getCHANNEL_CODE());

        tXRequest.setUserLoginId(approvalParameter.getUSER_ID());
        tXRequest.setRepaymentMethod(approvalParameter.getREPMNT_METHOD());

        tXRequest.setMultipleDisbPerMember(approvalParameter.getMULTIPLE_DISB_PM()); // 
        tXRequest.setTransmissionTime(System.currentTimeMillis());

        tXRequest.setUserLoginId("SYSTEM");
        tXRequest.setBusinessUnitId((approvalParameter.getBU_ID()));

        getXapiCaller().settXRequest(tXRequest);
        ArgosMain.bRLogger.logEvent("<TXRequest>", gettXUtility().convertToString(tXRequest));

        getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
        getXapiCaller().setCurrency(tXRequest.getLoanCrncyId());
        getXapiCaller().setTxnAmount(tXRequest.getLoanAmount());
        getXapiCaller().setRefNumber(tXRequest.getCustomerNo());
        getXapiCaller().setTxnDescription("Update Loan [" + tXRequest.getAccountNumber1() + "~" + tXRequest.getCustomerNo() + "]");
        getXapiCaller().setMainRequest(acctBalReq);

        Object response = tXClient.updateGroupLoan(tXRequest);
        if (response instanceof GroupLoanOutputData)
        {
            grpLoanId = (((GroupLoanOutputData) response).getGroupLoanId());
            respRef = String.valueOf(((GroupLoanOutputData) response).getCustomerNo());

            Long queueId = gettXUtility().getQueueId(tXRequest.getReferenceNumber());
            tXRequest.setQueueId(queueId);

            ArgosMain.bRLogger.logEvent("<TXRequestWF>", gettXUtility().convertToString(tXRequest));
            Object wfitemresponse = tXClient.getWFItem(tXRequest);
            if (wfitemresponse instanceof WFItemOutputData)
            {
                System.out.println("i have the instance " + tXRequest.getQueueId());
                for (WorkListOutputData items : ((WFItemOutputData) wfitemresponse).getWorkItemList())
                {
                    System.out.println("fetching for group loan ref " + tXRequest.getReferenceNumber());
                    if (tXRequest.getReferenceNumber().equals(items.getDescription()))
                    {
                        System.out.println(" items.getId()" + items.getWorkItemId());
                        System.out.println("getListDesc()" + items.getQueueId());
                        System.out.println("getListKey()" + items.getCustomerName());
                        System.out.println("getListKey()" + items.getDescription());
                        tXRequest.setWorkListItem(items.getWorkItemId());
                    }
                }
                System.out.println("approving loan  " + tXRequest.getWorkListItem());

                gettXUtility().updateArgosGrpLoanLog(tXRequest, successResponse);
                if (tXClient.saveGroupLoan(tXRequest).equals("00"))
                {
                    tXClient.saveGroupLoan(tXRequest);
                    ArgosMain.bRLogger.logEvent("<Approved Success>", gettXUtility().convertToString(((WFItemOutputData) wfitemresponse).getQueueList()));
                }
                else
                {
                    ArgosMain.bRLogger.logEvent("<Error Approving >", tXClient.saveGroupLoan(tXRequest));
                    ArgosMain.bRLogger.logEvent("<Approval Failed>", gettXUtility().convertToString(((WFItemOutputData) wfitemresponse).getQueueList()));
                }

            }
            else if (response instanceof XAPIException)
            {
                ArgosMain.bRLogger.logEvent(((XAPIException) response).getErrorCode());
                ArgosMain.bRLogger.logEvent("Workflow Item XapiERROR", response);
            }
            else
            {
                //  ArgosMain.bRLogger.logEvent(getTagValue(String.valueOf(response), "error-code"));
                ArgosMain.bRLogger.logEvent("Workflow Item ERROR", response);
            }
            createGroupLoanResponse.setUpdateGroupLoanResult(success);
            ArgosMain.bRLogger.logEvent("<RessetCreateGroupLoanResultponse>", gettXUtility().convertToString(createGroupLoanResponse));

        }
        else if (response instanceof XAPIException)
        {
            //ArgosMain.bRLogger.logEvent(getTagValue(String.valueOf(response), "error-code"));
            ArgosMain.bRLogger.logEvent("ERROR", response);
            createGroupLoanResponse.setUpdateGroupLoanResult(ConError);

        }
        else
        {
            System.err.println("queueId " + tXRequest.getReferenceNumber());
            Long queueId = gettXUtility().getQueueId(tXRequest.getReferenceNumber());

            tXRequest.setQueueId(queueId);
            System.err.println("queueId " + queueId);

            System.err.println("queueId " + tXRequest.getQueueId());
            createGroupLoanResponse.setUpdateGroupLoanResult(ConError);
            ArgosMain.bRLogger.logEvent("GLOBAL ERROR", response);
            //createGroupLoanResponse.setCreateGroupLoanResult(getTagValue(String.valueOf(response), "error-code"));
        }
        getXapiCaller().settXRequest(tXRequest);
        getXapiCaller().setMainResponse(createGroupLoanResponse);
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        LogEventMessage();
        return createGroupLoanResponse;
    } //getLoanDataByLoanId

    public IndividualLoanUserDataResponse createIndividualLoan(IndividualLoanUserData acctBalReq)
    {
        setXapiCaller(new XAPICaller());
        startTime = System.currentTimeMillis();
        IndividualLoanUserDataResponse loanUserDataResponse = new IndividualLoanUserDataResponse();
        try
        {
            String acct_no, account_name;

            ArgosMain.bRLogger.logEvent("queryCustomerDetailsReq", gettXUtility().convertToString(acctBalReq));
            TXRequest tXRequest = new TXRequest();
            TXClient tXClient = new TXClient(getXapiCaller());
            LoanParameter loanParameter;
            CustomerDetails customerDetails;
            loanParameter = gettXUtility().queryProductDetails(acctBalReq.getClass_code());

            tXRequest.setChannelId(BRController.ChannelID);
            tXRequest.setCustomerId(gettXUtility().getCustomerId(acctBalReq.getRim_no().trim()));
            tXRequest.setTxnAmount(acctBalReq.getLoan_amt());
            tXRequest.setLoanAmount(acctBalReq.getLoan_amt());
            tXRequest.setCurrencyId(loanParameter.getCrncyId());
            tXRequest.setTermValue(acctBalReq.getLoan_term());
            tXRequest.setTermCode(acctBalReq.getLoan_term_Code());
            tXRequest.setBusinessUnitId(gettXUtility().geBuid(acctBalReq.getBranch_no()));
            tXRequest.setUserLoginId(acctBalReq.getCredit_Officer_Code());
            tXRequest.setBankOfficerId(gettXUtility().getBankOfficerId(acctBalReq.getCredit_Officer_Code()));
            tXRequest.setCreditTypeId(loanParameter.getCreditTypeId());
            tXRequest.setCustomerNo(acctBalReq.getRim_no().trim());
            tXRequest.setAccountId(gettXUtility().getAccountId(acctBalReq.getSVAccountNumber()));
            tXRequest.setLnStartDate(acctBalReq.getContract_date());
            tXRequest.setProduct_id(gettXUtility().getProductId(acctBalReq.getClass_code()));
            tXRequest.setAccountNumber1(acctBalReq.getSVAccountNumber().trim());
            tXRequest.setPurposeOfCredit(477L);
            tXRequest.setPortfolioId(11L);
            tXRequest.setReference(acctBalReq.getSVAccountNumber().trim());
            tXRequest.setRateType(loanParameter.getRateType());
            tXRequest.setIndexRateId(loanParameter.getIndexRateId());
            tXRequest.setIndustryId(new Long(acctBalReq.getSic_Code()));
            tXRequest.setAccessCode("ArgosInterface99999");
            tXRequest.setTransmissionTime(System.currentTimeMillis());
            tXRequest.setMaturityDate(gettXUtility().getMaturityDate(acctBalReq.getContract_date(), acctBalReq.getLoan_term()));

            getXapiCaller().setAccountNo(tXRequest.getAccountNumber1());
            getXapiCaller().setCurrency(tXRequest.getCurrencyId());
            getXapiCaller().setTxnAmount(tXRequest.getTxnAmount());
            getXapiCaller().setRefNumber(tXRequest.getAccountNumber1());
            getXapiCaller().setTxnDescription("Individual Loan Creation");
            getXapiCaller().setMainRequest(acctBalReq);
            getXapiCaller().settXRequest(tXRequest);

            boolean checkIfApplExist = gettXUtility().isApplicationNew(tXRequest.getCustomerNo(), tXRequest.getLoanAmount());
            System.err.println("exists application... " + checkIfApplExist);
            if (checkIfApplExist)
            {
                System.err.println("Application Exists");
                Long maxApplId = gettXUtility().queryMaxApplID(tXRequest.getCustomerNo(), tXRequest.getLoanAmount());
                customerDetails = gettXUtility().queryCustomerDetails(tXRequest.getCustomerNo(), tXRequest.getLoanAmount(), maxApplId);
                tXRequest.setApplID(customerDetails.getApplId());
                tXRequest.setCustomerName(customerDetails.getFirstName());
                if (gettXUtility().SicCodeValid(acctBalReq.getSic_Code()))
                {
                    gettXUtility().updateCreditAppSicCode(customerDetails.getApplId(), acctBalReq.getSic_Code());
                }
                String Ln_AcctNo = createindividualLoan(tXRequest);
                loanUserDataResponse.setCreateIndividualLoanResult(Ln_AcctNo);
                gettXUtility().updateArgosIndvLoanLog(tXRequest, successResponse);
            }
            else
            {
                boolean applicationResponse = tXClient.createCreditApplication(tXRequest);

                if (applicationResponse)
                {
                    customerDetails = gettXUtility().queryCustomerDetails(tXRequest.getCustomerNo().trim(), tXRequest.getCustomerId());
                    tXRequest.setApplID(customerDetails.getApplId());
                    tXRequest.setCustomerName(customerDetails.getFirstName());

                    if (gettXUtility().SicCodeValid(acctBalReq.getSic_Code()))
                    {
                        gettXUtility().updateCreditAppSicCode(customerDetails.getApplId(), acctBalReq.getSic_Code());
                    }
                    boolean logLoanDetail = gettXUtility().LogArgosIndividualLoan(tXRequest, "01");
                    if (logLoanDetail)
                    {
                        try
                        {
                            Object response = tXClient.createAndApproveLoanAccount(tXRequest);
                            if (response instanceof LoanAccountSummaryOutputData)
                            {
                                acct_no = (((LoanAccountSummaryOutputData) response).getAccountNo());
                                account_name = String.valueOf(((LoanAccountSummaryOutputData) response).getAccountName());
                                loanUserDataResponse.setCreateIndividualLoanResult(acct_no);
                                gettXUtility().updateArgosIndvLoanLog(tXRequest, successResponse);

                            }
                            else
                            {
                                if (response instanceof XAPIException)
                                {
                                    ArgosMain.bRLogger.logEvent("Loan creation Error", "[Loan creation Failed] " + gettXUtility().convertToString(response));
                                }
                                else
                                {
                                    ArgosMain.bRLogger.logEvent("Loan creation Error", "[Loan creation Failed] " + gettXUtility().convertToString(response));
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            if (ex instanceof XAPIException)
                            {
                                ArgosMain.bRLogger.logEvent("Loan creation Exception", "[Loan creation Failed] " + ex);

                            }
                            else
                            {
                                ArgosMain.bRLogger.logEvent("Loan creation Exception", "[Loan creation Failed] " + ex);

                            }
                        }
                    }
                    else
                    {
                        ArgosMain.bRLogger.logEvent("Loan data Logging", "[Loan data Logging Failed] " + Boolean.FALSE);
                    }
                }
                else
                {
                    ArgosMain.bRLogger.logEvent("Credit Application Creation", "[Credit Application Creation] " + Boolean.FALSE);
                }
            }
        }
        catch (Exception ex)
        {
            ArgosMain.bRLogger.logEvent("Global Error", "[An error occured] " + ex);
        }
        getXapiCaller().setRespCode(loanUserDataResponse.getCreateIndividualLoanResult());
        getXapiCaller().setMainResponse(loanUserDataResponse);
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        LogEventMessage();
        return loanUserDataResponse;
    }

    public String createindividualLoan(TXRequest tXRequest)
    {
        setXapiCaller(new XAPICaller());
        startTime = System.currentTimeMillis();
        TXClient tXClient = new TXClient(getXapiCaller());
        String acct_no = "0";
        try
        {
            Object response = tXClient.createAndApproveLoanAccount(tXRequest);
            if (response instanceof LoanAccountSummaryOutputData)
            {
                acct_no = (((LoanAccountSummaryOutputData) response).getAccountNo());
                String account_name = String.valueOf(((LoanAccountSummaryOutputData) response).getAccountName());

                gettXUtility().updateArgosIndvLoanLog(tXRequest, successResponse);
            }
            else
            {
                if (response instanceof XAPIException)
                {
                    ArgosMain.bRLogger.logEvent("Loan creation Error For Existing creditApp", "[Loan creation Failed] " + gettXUtility().convertToString(response));
                }
                else
                {
                    ArgosMain.bRLogger.logEvent("Loan creation Error For Existing creditApp", "[Loan creation Failed] " + gettXUtility().convertToString(response));
                }
            }
        }
        catch (Exception ex)
        {
            if (ex instanceof XAPIException)
            {
                ex.printStackTrace();
            }
            else
            {
                ex.printStackTrace();
            }
        }
        return acct_no;
    }

    public LoanDataByLoanIdResponse getLoanDataByLoanId(LoanDataByLoanId getLoanDataByLoanId)
    {
        setXapiCaller(new XAPICaller());
        LoanDataByLoanIdResponse byLoanIdResponse;
        startTime = System.currentTimeMillis();
        ArgosMain.bRLogger.logEvent("queryLoanDataById", gettXUtility().convertToString(getLoanDataByLoanId));
        byLoanIdResponse = gettXUtility().queryLoanDataById(getLoanDataByLoanId.getLoanId());

        getXapiCaller().setAccountNo(getLoanDataByLoanId.getLoanId());
        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
        getXapiCaller().setRefNumber(getLoanDataByLoanId.getLoanId());
        getXapiCaller().setTxnDescription("Get Loan Data By Id");
        getXapiCaller().setMainRequest(gettXUtility().convertToString(getLoanDataByLoanId));
        getXapiCaller().setRefNumber(byLoanIdResponse.getLoanId());
        getXapiCaller().setRespCode("0");
        getXapiCaller().setMainResponse(gettXUtility().convertToString(byLoanIdResponse));
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        LogEventMessage();
        ArgosMain.bRLogger.logEvent("queryLoanDataById", gettXUtility().convertToString(byLoanIdResponse));
        return byLoanIdResponse;
    }

    public LoanIDByGroupLoanNumberResponse getGroupLoanDataByLoanId(LoanIDByGroupLoanNumber byGroupLoanNumber)
    {
        setXapiCaller(new XAPICaller());
        LoanIDByGroupLoanNumberResponse byGroupLoanNumberResponse;
        startTime = System.currentTimeMillis();
        ArgosMain.bRLogger.logEvent("queryLoanDataById", gettXUtility().convertToString(byGroupLoanNumber));
        byGroupLoanNumberResponse = gettXUtility().queryGroupLoanDataById(byGroupLoanNumber.getGroupLoanNumber());

        getXapiCaller().setAccountNo(byGroupLoanNumber.getGroupLoanNumber());
        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
        getXapiCaller().setRefNumber(byGroupLoanNumber.getGroupLoanNumber());
        getXapiCaller().setTxnDescription("Get Loan Data By Id");
        getXapiCaller().setMainRequest(gettXUtility().convertToString(byGroupLoanNumber));
        getXapiCaller().setRespCode("0");
        getXapiCaller().setMainResponse(byGroupLoanNumberResponse);
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        ArgosMain.bRLogger.logEvent("queryLoanDataById", gettXUtility().convertToString(byGroupLoanNumber));
        LogEventMessage();
        return byGroupLoanNumberResponse;
    }

    public CustomerDetails getLoanData(CustomerData acctBalReq)
    {
        setXapiCaller(new XAPICaller());
        startTime = System.currentTimeMillis();
        CustomerDetails customerDetails;
        ArgosMain.bRLogger.logEvent("queryCustomerDetailsReq", gettXUtility().convertToString(acctBalReq));

        customerDetails = gettXUtility().queryCustomerDetails(acctBalReq.getAcct_no());

        getXapiCaller().setAccountNo(acctBalReq.getAcct_no());
        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
        getXapiCaller().setRefNumber(acctBalReq.getAcct_no());
        getXapiCaller().setTxnDescription("Get Loan Data");
        getXapiCaller().setMainRequest(gettXUtility().convertToString(acctBalReq));
        getXapiCaller().setRespCode("0");
        getXapiCaller().setMainResponse(gettXUtility().convertToString(customerDetails));
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        ArgosMain.bRLogger.logEvent("queryCustomerDetailsRes", gettXUtility().convertToString(customerDetails));
        LogEventMessage();
        return customerDetails;
    }

    public String getTagValue(String xml, String tagName)
    {

        if (xml.contains("java.lang.NullPointerException") || xml.equalsIgnoreCase("null") || xml.isEmpty())
        {
            return invalidError;
        }
        else
        {
            return xml.split("<" + tagName + ">")[1].split("</" + tagName + ">")[0];
        }
    }

    public String convertToString(Object object)
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

    /**
     * @return the tXUtility
     */
    public TXUtility gettXUtility()
    {
        return tXUtility;
    }

    /**
     * @param tXUtility the tXUtility to set
     */
    public void settXUtility(TXUtility tXUtility)
    {
        this.tXUtility = tXUtility;
    }

}
