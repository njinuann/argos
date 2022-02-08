package Argos;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Pecherk
 */
public class IDisplay extends JTextPane implements DocumentListener
{
    StyledDocument doc = (StyledDocument) getDocument();
    SimpleAttributeSet attributeSet = new SimpleAttributeSet();

    public IDisplay()
    {
        addListener();
    }

    private void addListener()
    {
        getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        SwingUtilities.invokeLater(this::removeLines);
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
    }

    public void removeLines()
    {
        Element root = getDocument().getDefaultRootElement();

        while (root.getElementCount() > BRController.DisplayLines)
        {
            Element firstLine = root.getElement(0);

            try
            {
                getDocument().remove(0, firstLine.getEndOffset());
            }
            catch (Exception ble)
            {
                ble = null;
                break;
            }
        }
    }

    public void append(String str, Color color)
    {
        SwingUtilities.invokeLater(() ->
        {
            try
            {
                StyleConstants.setForeground(attributeSet, color);
                doc.insertString(doc.getLength(), str, attributeSet);
            }
            catch (Exception ex)
            {
                ex = null;
            }
        });
    }
}
