package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree;


import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CheckRenderer extends JPanel implements TreeCellRenderer {

    private static class TreeLabel extends JLabel {
        private boolean myIsSelected;

        public void setBackground(Color color) {
            if (color instanceof ColorUIResource)
                color = null;
            super.setBackground(color);
        }

        public void paint(Graphics g) {
            String str = getText();
            if (str != null) {
                if (0 < str.length()) {
                    if (myIsSelected) {
                        g.setColor(UIManager.getColor("Tree.selectionBackground"));
                    } else {
                        g.setColor(UIManager.getColor("Tree.textBackground"));
                    }
                    Dimension d = getPreferredSize();
                    int imageOffset = 0;
                    Icon currentI = getIcon();
                    if (currentI != null) {
                        imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
                    }
                    g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
                }
            }
            super.paint(g);
        }

        private void setSelected(boolean isSelected) {
            myIsSelected = isSelected;
        }
    }

    private final JCheckBox myCheckbox;
    private final TreeLabel myLabel;

    public CheckRenderer() {
        setLayout(null);
        add(myCheckbox = new JCheckBox());
        add(myLabel = new TreeLabel());
        myCheckbox.setBackground(UIManager.getColor("Tree.textBackground"));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean isSelected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected,
                expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        boolean selected = ((CheckNode) value).isSelected();
        myCheckbox.setSelected(selected);

        myLabel.setFont(tree.getFont());
        if (selected) {
            myLabel.setForeground(Color.BLACK);
        } else {
            myLabel.setForeground(Color.LIGHT_GRAY);
        }
        myLabel.setText(stringValue);
        myLabel.setSelected(isSelected);
        return this;
    }

    public Dimension getPreferredSize() {
        Dimension checkDimention = myCheckbox.getPreferredSize();
        Dimension labelDimention = myLabel.getPreferredSize();
        return new Dimension(checkDimention.width + labelDimention.width,
                (checkDimention.height < labelDimention.height ?
                        labelDimention.height : checkDimention.height));
    }

    public void doLayout() {
        Dimension checkDimension = myCheckbox.getPreferredSize();
        Dimension labelDimension = myLabel.getPreferredSize();
        int checkY = 0;
        int labelY = 0;
        if (checkDimension.height < labelDimension.height) {
            checkY = (labelDimension.height - checkDimension.height) / 2;
        } else {
            labelY = (checkDimension.height - labelDimension.height) / 2;
        }
        myCheckbox.setLocation(0, checkY);
        myCheckbox.setBounds(0, checkY, checkDimension.width, checkDimension.height);
        myLabel.setLocation(checkDimension.width, labelY);
        myLabel.setBounds(checkDimension.width, labelY, labelDimension.width, labelDimension.height);
    }


    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
            color = null;
        super.setBackground(color);
    }
}    
