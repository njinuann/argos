/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import AGInterfaceWS.BRMeter;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author NJINU
 */
public class ArgosMain
{

    public static ArgosMainUI uiFrame = null;
    public static final javax.swing.JDialog consoleDialog = new javax.swing.JDialog();
    public static final IDisplay displayArea = new IDisplay();
    public static Color infoColor = new Color(0, 0, 128);
    public static Color errorColor = new Color(192, 0, 0);
    public static BRMeter brMeter = null;
    public static BRLogger bRLogger = new BRLogger("ArgosService", "logs");

    public static void main(String[] args)
    {
        // TODO code application logic here
        setOutput();
        setLookAndFeel();
        new ArgosMain().execute();

    }

    private void execute()
    {
        BRController.initialize();
        startServices();
        showUI();
    }

    public void startServices()
    {
        bRLogger.logEvent("=======<Starting Services>=======");
        new AGConService().startSoap();
        new Thread(this::cleanUp).start();

    }

    public static void stopServices() throws Exception
    {
        bRLogger.logEvent("=======<Stopping Services>=======");
    }

    public void showUI()
    {
        SwingUtilities.invokeLater(() ->
        {
            uiFrame = new ArgosMainUI();
            if (brMeter != null)
            {
                uiFrame.setBrMeter(brMeter);
            }
            else
            {
                brMeter = uiFrame.getBrMeter();
            }
            uiFrame.setLocationRelativeTo(null);
            consoleDialog.setVisible(false);
            uiFrame.setVisible(true);
        });

        new Thread(() ->
        {
            while (true)
            {
                try
                {
                    System.gc();
                    Thread.sleep(900000);
                }
                catch (Exception ex)
                {
                    ex = null;
                }
            }
        }).start();
    }

    private static void setLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(new PlasticLookAndFeel());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void setOutput()
    {
        setLookAndFeel();

        System.setOut(new EIPrint(new EIStream(infoColor)));
        System.setErr(new EIPrint(new EIStream(errorColor)));

        consoleDialog.setTitle("Argos Interface");
        CPanel panel = new CPanel();

        javax.swing.GroupLayout consoleDialogLayout = new javax.swing.GroupLayout(consoleDialog.getContentPane());
        consoleDialog.getContentPane().setLayout(consoleDialogLayout);

        consoleDialogLayout.setHorizontalGroup(
                consoleDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        consoleDialogLayout.setVerticalGroup(
                consoleDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        consoleDialog.setSize(500, 500);
        consoleDialog.setUndecorated(true);

        consoleDialog.setLocationRelativeTo(null);
        consoleDialog.setVisible(true);
    }

    public void cleanUp()
    {
        while (true)
        {
            try
            {
                System.gc();
                Thread.sleep(900000);

            }
            catch (Exception ex)
            {
                ex = null;
            }
        }
    }

    public static void shutdown()
    {
        exit();
    }

    private static void exit()
    {
        System.exit(0);
    }
}
