/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2010 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General 
 public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General 
 public License for more details.
 *
 * You should have received a copy of the GNU Affero General 
 public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package AGInterfaceWS;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * ISOMsgPanel Swing based GUI to ISOMsg
 *
 * @author apr@cs.com.uy
 * @author Kris Leite <kleite at imcsoftware.com>
 * @see org.jpos.iso.ISOMsg
 */
public final class BRMeter extends JComponent implements Runnable
{

    Image im;
    Timer ti;
    Graphics img;
    String positiveText = "";
    String negativeText = "";
    Font fontBig, fontSmall, capFont;
    Color color = new Color(255, 255, 255);
    /**
     * handle ISOMeter's counters outside of this class in order to reduce 'int'
     * to 'String' conversions.
     *
     * @serial
     */
    boolean connected;
    final static int width = 180;
    final static int height = 67;
    final static int MAX_VALUE = 1000;

    public final static int mass = height / 2;
    int[] yPoints, xPoints;
    int lastPositive, lastNegative;
    String positiveCounter, negativeCounter;
    /**
     * counter to keep the scrolling active
     */
    int continueScroll;
    /**
     * used to determine if to scroll mark to end of graph
     */
    boolean scroll = true;
    /**
     * Refresh panel in miliseconds
     */
    private Image imb;
    int refreshPanel = 50;
    private Thread repaintThread;
    private String caption = "", status = "SUSPENDED";
    private static final long serialVersionUID = -1770533267122111538L;

    public BRMeter(String caption)
    {
        super();
        fontBig = new Font("Helvetica", Font.ITALIC, mass * 3 / 5);
        fontSmall = new Font("Helvetica", Font.PLAIN, 10);
        capFont = new Font("Helvetica", Font.BOLD, 10);
        yPoints = new int[width];
        xPoints = new int[width];
        for (int i = 0;i < width;i++)
        {
            xPoints[i] = i;
            yPoints[i] = mass;
        }
        positiveCounter = "";
        negativeCounter = "";
        connected = false;
        setCaption(caption);
    }

    public synchronized void start()
    {
        if (repaintThread == null)
        {
            repaintThread = new Thread(this, "ISOMeter");
            repaintThread.setPriority(Thread.MIN_PRIORITY);
            repaintThread.start();
        }
    }

    public void setValue(int val)
    {
        int y = mass - ((val % 1000) * height / 2000);
        yPoints[width - 1] = y;
        continueScroll = width;
        scroll();
    }

    public void setScroll(boolean scroll)
    {
        this.scroll = scroll;
    }

    public void setRefresh(int refreshPanel)
    {
        if (refreshPanel > 0)
        {
            this.refreshPanel = refreshPanel;
        }
    }

    public void setConnected(boolean isConnected)
    {
        if (this.connected != isConnected)
        {
            if (!scroll)
            {
                if (isConnected)
                {
                    continueScroll = width;
                }
                else
                {
                    continueScroll = 1;
                }
            }
            if (isConnected)
            {
                setStatus("ACTIVE");
            }
            else
            {
                setStatus("SUSPENDED");
            }
            repaint();
        }

        this.connected = isConnected;
    }

    public void setPositiveCounter(int s)
    {
        positiveCounter = (s <= 0 ? "" : String.format("%03d", s));
    }

    public void setNegativeCounter(int s)
    {
        negativeCounter = (s <= 0 ? "" : String.format("%03d", s));
    }

    public void setValue(int val, String textString)
    {
        setValue(val);

        if (val >= 0)
        {
            positiveText = (textString != null) ? textString : "";
            lastPositive = 0;
        }
        else
        {
            negativeText = (textString != null) ? textString : "";
            lastNegative = 0;
        }
    }

    @Override
    public void paint(Graphics g)
    {
        if (repaintThread == null)
        {
            start();
        }
        plot();
        g.drawImage(im, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }

    private void scroll()
    {
        System.arraycopy(yPoints, 1, yPoints, 0, width - 1);
        if (continueScroll > 0)
        {
            continueScroll--;
        }
        yPoints[width - 1] = mass;
    }

    public void plot()
    {
        if (im == null)
        {
            im = createImage(width, height);
            img = im.getGraphics();
            img.setColor(Color.black);
            img.fillRect(0, 0, width, height);
            img.clipRect(0, 0, width, height);
            plotGrid();

            /* save a copy of the image */
            imb = createImage(width, height);
            Graphics imbCopy = imb.getGraphics();
            imbCopy.drawImage(im, 0, 0, this);
        }
        img.drawImage(imb, 0, 0, this);
        if (continueScroll > 0)
        {
            scroll();
        }

        plotText(positiveText, lastPositive++, 3, mass - 10);
        plotText(negativeText, lastNegative++, 3, height - 10);
        plotCounters(positiveCounter, negativeCounter);
        img.setColor(connected ? Color.green : Color.red);
        img.drawPolyline(xPoints, yPoints, width);
        plotCaption();
        plotStatus();
    }

    private void plotGrid()
    {
        img.setColor(Color.blue);
        for (int i = 0;i < width;i++)
        {
            if (i % 20 == 0)
            {
                img.drawLine(i, 0, i, height);
            }
        }
        for (int i = -1000;i < 1000;i += 200)
        {
            int y = mass + (i * height / 2000);
            img.drawLine(0, y, width, y);
        }
    }

    private void plotText(String t, int l, int x, int y)
    {
        if (t != null && continueScroll > 0)
        {
            img.setColor(Color.lightGray);
            img.setFont(fontBig);
            img.drawString(t, x, y);
        }
    }

    public void plotCaption()
    {
        img.setColor(Color.CYAN);
        img.setFont(capFont);
        int capLen = Math.round(getCaption().length() * 6.4f);
        int x = (width - capLen) / 2;
        if (connected)
        {
            img.setColor(Color.GREEN);
        }
        else
        {
            img.setColor(Color.RED);
        }
        img.drawString(getCaption(), x, 8);
    }

    public void plotStatus()
    {
        img.setColor(Color.CYAN);
        img.setFont(capFont);
        int capLen = Math.round(getStatus().length() * 6.4f);
        int x = (width - capLen) / 2;
        if (connected)
        {
            img.setColor(Color.GREEN);
        }
        else
        {
            img.setColor(Color.RED);
        }
        img.drawString(getStatus(), x, height - 1);
    }

    private void plotCounters(String p, String n)
    {
        img.setColor(Color.lightGray);
        img.setFont(fontSmall);
        img.drawString(p, width - 25, 13);
        img.drawString(n, width - 25, height - 3);
        try
        {
            int con = Integer.parseInt(p.trim());
        }
        catch (Exception ex)
        {
            ex = null;
        }
    }

    public void run()
    {
        while (true)
        {
            if (continueScroll > 0 || !positiveText.equals("") || !negativeText.equals(""))
            {
                try
                {
                    repaint();
                }
                catch (Exception ex)
                {
                    ex = null;
                }
                scroll();
            }
            try
            {
                Thread.sleep(refreshPanel);
            }
            catch (InterruptedException e)
            {
                // OK to ignore
            }
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    /**
     * @return the caption
     */
    public String getCaption()
    {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    /**
     * @return the status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status)
    {
        this.status = status.toUpperCase();
    }
}
