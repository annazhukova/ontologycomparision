package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;


public class CheckNode extends DefaultMutableTreeNode {
    public static final int OUR_SINGLE_SELECTION = 0;
    public static final int OUR_DIG_IN_SELECTION = 2;

    private boolean myIsSelected = true;
    private final Color color;

    public CheckNode(Object userObject, boolean allowsChildren, boolean isSelected, Color color) {
        super(userObject, allowsChildren);
        myIsSelected = isSelected;
        this.color = color;
    }

    public CheckNode(Object userObject, boolean isSelected, Color color) {
         this(userObject, true, isSelected, color);
    }

    public void setSelected(boolean isSelected, int selectionMode, ArrayList<CheckNode> nodes) {
        setSelected(isSelected);
        nodes.add(this);
        if ((selectionMode == OUR_DIG_IN_SELECTION) && (children != null)) {
            Enumeration en = children.elements();
            while (en.hasMoreElements()) {
                CheckNode node = (CheckNode) en.nextElement();
                node.setSelected(isSelected, selectionMode, nodes);
            }
        }
    }

    public boolean isSelected() {
        return myIsSelected;
    }

    public void setSelected(boolean b) {
        myIsSelected = b;
    }

    public Color getColor() {
        return color;
    }
}


