/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import com.sun.net.httpserver.HttpServer;
//import com.sun.xml.internal.ws.spi.ProviderImpl;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Pecherk
 */
public class AGConService
{

    HttpServer httpServer;
    Endpoint endpoint = null;

    public void startSoap()
    {
        try
        {
            httpServer = HttpServer.create(new InetSocketAddress(3560), 100);
            httpServer.setExecutor(new ThreadPoolExecutor(2, 100, 2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100)));
            
            new com.sun.xml.ws.spi.ProviderImpl().createEndpoint(null, new ARGService()).publish(httpServer.createContext("/ruby/ArgosService"));
            httpServer.start();
            
            ArgosMain.bRLogger.logEvent("***********Argos Service started***********");
        } 
        catch (IOException ex)
        {
            Logger.getLogger(AGConService.class.getName()).log(Level.SEVERE, null, ex);
            ArgosMain.bRLogger.logError("SOAP SERVICE-ERROR", ex);
        }
    }

    public void stop() throws Exception
    {
        if (endpoint != null)
        {
            endpoint.stop();
        }
        httpServer.stop(5);
    }
}
