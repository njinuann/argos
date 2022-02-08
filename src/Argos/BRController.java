/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

/**

 @author Pecherk
 */
//import static RubyExtPay.BRController.xapiUrlMap;
import com.neptunesoftware.supernova.ws.client.AccountWebService;
import com.neptunesoftware.supernova.ws.client.security.BasicHTTPAuthenticator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Authenticator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

public class BRController
{

    public static long ChannelID = 8L, SystemUserID = -99L;
    private static Properties settings;
    public static final Properties isoCodes = new Properties();
    private static final Properties xapicodes = new Properties();
    public static String XapiUser, XapiPassword /*, LoanRepayAccount*/;
    private static final HashMap<String, String> currencyMap = new HashMap<>();
    public static final HashMap<Integer, String> xapiUrlMap = new HashMap<>();
    public static String JdbcDriverName = "oracle.jdbc.driver.OracleDriver";
    public static String CoreSchemaName, CoreWsdlURL, CMSchemaJdbcUrl, CMSchemaName;
    public static String EtaxUrl, BankName, BankToken, EtaxHostName, STTFtpUser, STTFtpPass, ReconFilePath, FilePath, ReconServer, ReconUploadTime, TaxValidationFg;
    public static String PrimaryCurrency, AllowedProductCodes, CMSchemaPassword;
    public static String EnableDebug = "N", ChannelCode = "POS";
    public static CallableStatement[] logTxnStatement = null;
    public static ArrayList<Object> CoreNodes = new ArrayList<>();
    public static int lastPortIndex = 0;
    public static AccountWebService accountWebService;

    public static int[] CONTXNCount = null;
    public static String[] IDController = null;   
    public static boolean serviceSuspended = false;
    public static Connection[] DBConnections = null;
    public static final int WAIT_FOR_CONNECTION = -1;
    public static final int NO_XAPI_CONNECTIONS = -96;
    public static final String confDir = "conf", logsDir = "logs";
    private static boolean DisplayConsole = true;
    public static int DisplayLines = 500;
    public static BRLogger bRLogger = new BRLogger("ArgosService", "logs");
    public static ArrayList<CBNode> CoreBankingNodes = new ArrayList<>();
    private static ArrayList<CBNode> XapiNodes = new ArrayList<>();

    public static void initialize()
    {
        configure();
        loadLibraries();       
        openConnections();
    }

    public static void loadLibraries()
    {
        try
        {
            Class.forName(JdbcDriverName);
        }
        catch (Exception ex)
        {
            System.err.println("" + ex);
        }
        Authenticator.setDefault(new BasicHTTPAuthenticator(XapiUser, XapiPassword));
        System.setProperty("javax.xml.rpc.ServiceFactory", "weblogic.webservice.core.rpc.ServiceFactoryImpl");
        System.setProperty("javax.xml.soap.MessageFactory", "weblogic.webservice.core.soap.MessageFactoryImpl");
        System.setProperty("java.net.useSystemProxies", "true");
    }

    public static void openConnections()
    {
        new Thread(() ->
        {
            CONTXNCount = new int[xapiUrlMap.size()];
            IDController = new String[xapiUrlMap.size()];
            DBConnections = new Connection[xapiUrlMap.size()];
            logTxnStatement = new CallableStatement[xapiUrlMap.size()];

            for (int i = 0; i < xapiUrlMap.size(); i++)
            {
                openConnection(i);
            }
            new Thread(new CONWatch()).start();
        }).start();
    }

//    public static void selectCoreNode() {
//        boolean skipInvalidNodes = true;
//        for (int i = lastPortIndex; i < CoreBankingNodes.length && !isConnected(); i++) {
//            if (Objects.equals(CoreBankingNodes[i][1], Boolean.TRUE) || !skipInvalidNodes) {
//                BRController.CoreBankingNodes[i][1] = Boolean.FALSE;
//                BRController.CoreBankingNodes[i][1] = connectToNode(String.valueOf(BRController.CoreBankingNodes[i][0]));
//                lastPortIndex = isConnected() ? i : 0;
//            }
//            if (!isConnected() && i == (BRController.CoreBankingNodes.length - 1) && skipInvalidNodes) {
//                skipInvalidNodes = false;
//                lastPortIndex = 0;
//                i = 0;
//            }
//        }
//    }
//    public static boolean connectToNode(String webServiceContextURL)
//    {
//        //   setCoreBankingNodes(webServiceContextURL);
//        try
//        {
//            System.err.println("connect to nodes " + webServiceContextURL);
//            accountWebService = new AccountWebServiceEndPointPort_Impl(webServiceContextURL + "AccountWebServiceBean?wsdl").getAccountWebServiceSoapPort();
////            transactionsWebService = new TransactionsWebServiceEndPointPort_Impl(webServiceContextURL + "TransactionsWebServiceBean?wsdl").getTransactionsWebServiceSoapPort();
////            fundsTransferWebService = new FundsTransferWebServiceEndPointPort_Impl(webServiceContextURL + "FundsTransferWebServiceBean?wsdl").getFundsTransferWebServiceSoapPort();
////            txnProcessWebService = new TxnProcessWebServiceEndPointPort_Impl(webServiceContextURL + "TxnProcessWebServiceBean?wsdl").getTxnProcessWebServiceSoapPort();
//        }
//        catch (Exception ex)
//        {
//            bRLogger.logError("Exception", ex);
//        }
//        return isConnected();
//    }
//
//    private static boolean isConnected()
//    {
//        return accountWebService != null;
//    }

//    public static void checkConnection() {
//        if (!isConnected()) {
//            selectCoreNode();
//        }
//    }
    public static void configure()
    {
        FileInputStream in;
        settings = new Properties();

        try
        {
            new File(logsDir).mkdirs();

            new File(confDir).mkdirs();
            File propsFile = new File(confDir, "settings.prp");
            if (!propsFile.exists())
            {
                System.out.println("Missing bridge configuration file. Unable to load bridge settings...");
                bRLogger.logDebug("Missing bridge configuration file. Unable to load bridge settings...");
            }
            in = new FileInputStream(propsFile);
            settings.loadFromXML(in);

            ChannelCode = settings.getProperty("ChannelCode");
            CoreSchemaName = settings.getProperty("CoreSchemaName");
            CMSchemaPassword = BRCrypt.decrypt(settings.getProperty("CMSchemaPassword"));

            CMSchemaJdbcUrl = settings.getProperty("CMSchemaJdbcUrl");
            CoreWsdlURL = settings.getProperty("CoreWsdlURL");

            PrimaryCurrency = settings.getProperty("PrimaryCurrency");
            AllowedProductCodes = settings.getProperty("AllowedProductCodes", "0");

//            WalletToBankGL = settings.getProperty("WalletToBankGL");
//            BankToWalletGL = settings.getProperty("BankToWalletGL");
//
//            UtilityCollectionGL = settings.getProperty("UtilityCollectionGL");
            DisplayConsole = "Y".equalsIgnoreCase(settings.getProperty("DisplayConsole"));

            EtaxUrl = settings.getProperty("EtaxUrl");

            BankName = settings.getProperty("BankName");
            BankToken = settings.getProperty("BankToken");

            EtaxHostName = settings.getProperty("EtaxHostName");
            STTFtpUser = settings.getProperty("STTFtpUser");

            STTFtpPass = settings.getProperty("STTFtpPass");
            ReconFilePath = settings.getProperty("ReconFilePath");

            FilePath = settings.getProperty("FilePath");
            ReconServer = settings.getProperty("ReconServer");

            ReconUploadTime = settings.getProperty("ReconUploadTime");
            TaxValidationFg = settings.getProperty("TaxValidationFg");
            setXapiNodes();
            try
            {
                DisplayLines = Integer.parseInt(settings.getProperty("DisplayLines"));
            }
            catch (Exception ex)
            {
                DisplayLines = 500;

                System.err.println("" + ex);

            }
            if (!"0".equals(AllowedProductCodes.trim()))
            {
                StringBuilder buffer = new StringBuilder();
                AllowedProductCodes = AllowedProductCodes.trim().replaceAll(";", ",");
                StringTokenizer tokenizer = new StringTokenizer(AllowedProductCodes, ",");
                while (tokenizer.hasMoreTokens())
                {
                    buffer.append(buffer.length() > 0 ? "," : "").append(tokenizer.nextToken());
                }
                AllowedProductCodes = buffer.toString();
            }

            EnableDebug = settings.getProperty("EnableDebug", "N");
            CMSchemaName = settings.getProperty("CMSchemaName");

            JdbcDriverName = settings.getProperty("JdbcDriverName");
            serviceSuspended = "Y".equalsIgnoreCase(settings.getProperty("SuspendService"));

            XapiUser = BRCrypt.decrypt(settings.getProperty("XapiUser"));
            XapiPassword = BRCrypt.decrypt(settings.getProperty("XapiPassword"));

            try
            {
                ChannelID = Long.parseLong(settings.getProperty("ChannelID"));
            }
            catch (Exception ex)
            {

                System.err.println("" + ex);

            }
            in.close();
        }
        catch (Exception ex)
        {
            bRLogger.logError("Exception", ex);
            System.err.println("" + ex);

        }
        try
        {
            File propsFile = new File(confDir, "xapicodes.prp");
            in = new FileInputStream(propsFile);
            xapicodes.loadFromXML(in);
            in.close();
        }
        catch (Exception ex)
        {
            bRLogger.logError("Exception", ex);
            System.err.println("" + ex);

        }
        try
        {
            File isoPropsFile = new File(confDir, "isocodes.prp");
            in = new FileInputStream(isoPropsFile);
            isoCodes.loadFromXML(in);
            in.close();
        }
        catch (Exception ex)
        {
            bRLogger.logError("Exception", ex);
            System.err.println("" + ex);

        }
        //    ExtPayMain.consoleDialog.setVisible(DisplayConsole);

    }

//    }
//    private static void setCoreBankingNodes()
//    {
//        StringTokenizer tokenizer = new StringTokenizer(CoreWsdlURL, "|");
//        while (tokenizer.hasMoreTokens())
//        {
//            CBNode cBNode = new CBNode();
//            cBNode.setWsContextURL(tokenizer.nextToken());
//            CoreBankingNodes.add(cBNode);
//        }
//    }
    public static void saveSettings()
    {
        try
        {
            if (settings != null)
            {
                new File(confDir).mkdirs();
                File propsFile = new File(confDir, "settings.prp");
                settings.put("EnableDebug", EnableDebug);
                settings.put("StartMobile", serviceSuspended ? "N" : "Y");
                settings.storeToXML(new FileOutputStream(propsFile), "Bridge Properties");
            }
        }
        catch (Exception ex)
        {
            bRLogger.logError("Exception", ex);
            System.err.println("" + ex);

        }
    }

//    public static void closeConnections()
//    {
//        for (int i = 0; i < xapiUrlMap.size(); i++)
//        {
//            closeConnection(i);
//        }
//    }

//    public static void connectToEDB(int index)
//    {
//        try
//        {
//            long t = System.currentTimeMillis();
//            DBConnections[index] = DriverManager.getConnection(CMSchemaJdbcUrl, CMSchemaName, CMSchemaPassword);
//            logTxnStatement[index] = DBConnections[index].prepareCall("{call PSP_EX_LOG_BILLS_TXN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
//
//            if (DBConnections[index] != null)
//            {
//                System.out.println("EDBConnection ID [ " + (index + 1) + " ] reset in approximately [ " + new BigDecimal(System.currentTimeMillis() - t).setScale(2, RoundingMode.UP) + " ] ms");
//            }
//        }
//        catch (Exception ex)
//        {
//            DBConnections[index] = null;
//            logTxnStatement[index] = null;
//
//            System.err.println("" + ex);
//
//        }
//    }

//    public static synchronized int getConnection()
//    {
//        int returnCode = NO_XAPI_CONNECTIONS;
//        for (int i = 0; i < IDController.length; i++)
//        {
//            switch (IDController[i])
//            {
//                case "N":
//                    IDController[i] = "Y";
//                    return i;
//                case "Y":
//                    returnCode = WAIT_FOR_CONNECTION;
//                    break;
//            }
//        }
//
//        return returnCode;
//    }

//    public static void releaseConnection(int index)
//    {
//        if (index >= 0)
//        {
//            CONTXNCount[index] = ((CONTXNCount[index] + 1) % 15);
//            if (CONTXNCount[index] == 0)
//            {
//                closeConnection(index);
//                openConnection(index);
//            }
//            IDController[index] = "N";
//        }
//    }
//
//    public static void withdrawConnection(int index)
//    {
//        if (index >= 0)
//        {
//            closeConnection(index);
//            CONTXNCount[index] = 0;
//            IDController[index] = "U";
//        }
//    }

//    public static void disconnectEDB(int index)
//    {
//        try
//        {
//            if (logTxnStatement[index] != null)
//            {
//                logTxnStatement[index].close();
//            }
//            logTxnStatement[index] = null;
//            if (DBConnections[index] != null)
//            {
//                DBConnections[index].close();
//            }
//            DBConnections[index] = null;
//        }
//        catch (Exception ex)
//        {
//            DBConnections[index] = null;
//            logTxnStatement[index] = null;
//        }
//    }

//    public static void disconnectXAPI(int index)
//    {
//        //XAPIConnections[index] = null;
//
//    }

    public static void openConnection(int index)
    {
        // connectToEDB(index);
        // connectToXAPI(index, 1);
        CONTXNCount[index] = 0;
        IDController[index] = DBConnections[index] != null ? "N" : "U";
    }

//    private static void closeConnection(int index)
//    {
//        if (DBConnections != null)
//        {
//            disconnectEDB(index);
//        }
////        if (XAPIConnections != null) {
////            disconnectXAPI(index);
////        }
//        if (IDController != null)
//        {
//            IDController[index] = "U";
//        }
//    }

    public static void CoreNodes(String urlProperty)
    {
        // System.err.println("urlProperty + " + urlProperty);
        int counter = 1;
        StringTokenizer tokenizer = new StringTokenizer(urlProperty, "|");
        while (tokenizer.hasMoreTokens())
        {
            //System.err.println("nodes +++++ " + counter);
            CoreNodes.add(counter);
            counter++;
            break;
        }

    }

//    public static void setCoreBankingNodes(String urlProperty) {
//        int counter = 0;
//        StringTokenizer tokenizer = new StringTokenizer(urlProperty, "|");
//        CoreBankingNodes = new Object[tokenizer.countTokens()][2];
//        while (tokenizer.hasMoreTokens()) {
//            CoreBankingNodes[counter][0] = tokenizer.nextToken();
//            CoreBankingNodes[counter][1] = Boolean.TRUE;
//            counter++;
//        }
//        //System.err.println("connection setcounter " + counter);
//    }
    private static void setCurrencyMap()
    {
        currencyMap.clear();
        currencyMap.put("036", "AUD");
        currencyMap.put("AUD", "036");
        currencyMap.put("124", "CAD");
        currencyMap.put("CAD", "124");
        currencyMap.put("230", "ETB");
        currencyMap.put("ETB", "230");
        currencyMap.put("IRR", "364");
        currencyMap.put("364", "IRR");
        currencyMap.put("JPY", "392");
        currencyMap.put("392", "JPY");
        currencyMap.put("KES", "404");
        currencyMap.put("404", "KES");
        currencyMap.put("454", "MWK");
        currencyMap.put("MWK", "454");
        currencyMap.put("566", "NGN");
        currencyMap.put("NGN", "566");
        currencyMap.put("643", "RUB");
        currencyMap.put("RUB", "643");
        currencyMap.put("756", "CHF");
        currencyMap.put("800", "UGX");
        currencyMap.put("UGX", "800");
        currencyMap.put("834", "TZS");
        currencyMap.put("TZS", "834");
        currencyMap.put("840", "USD");
        currencyMap.put("USD", "840");
        currencyMap.put("710", "ZAR");
        currencyMap.put("ZAR", "710");
        currencyMap.put("894", "ZMK");
        currencyMap.put("ZMK", "894");
        currencyMap.put("932", "ZWL");
        currencyMap.put("ZWL", "932");
        currencyMap.put("978", "EUR");
        currencyMap.put("EUR", "978");
        currencyMap.put("826", "GBP");
        currencyMap.put("GBP", "826");
        currencyMap.put("646", "RWF");
        currencyMap.put("RWF", "646");
    }

    public synchronized static String getCurrency(String currencyID)
    {
        Object code = currencyMap.get(currencyID);
        return (code == null ? currencyID : String.valueOf(code));
    }

    public static synchronized String getXapiMessage(String xapiRespCode)
    {
        return xapicodes.getProperty(xapiRespCode, "Undefined error");
    }

    public static synchronized String mapToIsoCode(String xapiRespCode)
    {
        return isoCodes.getProperty(xapiRespCode, "91");
    }

    private Object gettXProcessor()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static class CONWatch implements Runnable
    {

        @Override
        public void run()
        {
            while (!serviceSuspended)
            {
                try
                {
                    Thread.sleep(10000);
                    for (int i = 0; i < IDController.length; i++)
                    {
                        if (IDController[i].equals("U"))
                        {
                            System.err.println("oprning connections " + i);
                            openConnection(i);
                        }
                    }
                }
                catch (Exception ex)
                {
                    ex = null;
                }
            }
        }
    }

    /**
     @return the XapiNodes
     */
    public static ArrayList<CBNode> getXapiNodes()
    {
        return XapiNodes;
    }

    /**
     @param aXapiNodes the XapiNodes to set
     */
    private static void setXapiNodes()
    {
        getXapiNodes().clear();
        StringTokenizer tokenizer = new StringTokenizer(CoreWsdlURL, "|");
        while (tokenizer.hasMoreTokens())
        {
            getXapiNodes().add(new CBNode(tokenizer.nextToken()));
        }
    }
}
