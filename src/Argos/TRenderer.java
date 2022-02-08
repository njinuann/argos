/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Argos;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Pecherk
 */
public class TRenderer extends DefaultTreeCellRenderer
{
    Icon txnIcon = new ImageIcon(getClass().getResource("/images/txn16.png"));
    Icon rootIcon = new ImageIcon(getClass().getResource("/images/root16.gif"));
    Icon nodeIcon = new ImageIcon(getClass().getResource("/images/node16.gif"));
    Icon myLeafIcon = new ImageIcon(getClass().getResource("/images/leaf16.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.isRoot())
        {
            setIcon(rootIcon);
        }
        else if (node.isNodeChild(node))
        {
            setIcon(txnIcon);
        }
        else if (node.isLeaf())
        {
            setIcon(myLeafIcon);
        }
        else
        {
            setIcon(nodeIcon);
        }

        return this;
    }
}
