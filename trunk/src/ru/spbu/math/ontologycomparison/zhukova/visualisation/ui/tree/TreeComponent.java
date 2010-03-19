package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArcFilter;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.popupmenu.PopUpMenu;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * @author Anna Zhukova
 */
public class TreeComponent extends JSplitPane {
    private TreePane left = new TreePane();
    private TreePane right = new TreePane();

    public TreeComponent() {
        super(JSplitPane.HORIZONTAL_SPLIT);
        setLeftComponent(left.getTreeScrollPane());
        setRightComponent(right.getTreeScrollPane());
        setDividerLocation(0.5);
    }

    public void setGraphPane(final GraphPane graphPane) {
        left.setGraphPane(graphPane);
        right.setGraphPane(graphPane);
        setDividerLocation(0.5);
    }

    public void setTrees(JTree firstTree, JTree secondTree) {
        left.setTree(firstTree);
        right.setTree(secondTree);
    }

    private static class TreePane extends JScrollPane {

        //tree panel
        private final JPanel panel = new JPanel(new BorderLayout());
        private final JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // tree
        private JTree tree;
        private PopUpMenu popUpMenu = new PopUpMenu();

        public void setTree(final JTree tree) {
            if (this.tree != null) {
                panel.remove(this.tree);
            }
            this.tree = tree;
            panel.add(this.tree, BorderLayout.CENTER);
            popUpMenu.setTree(this.tree, panel);
            tree.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
                    popUpMenu.setRow(tree.getRowForLocation(e.getX(), e.getY()));
                    if (treePath != null) {
                        CheckNode node = (CheckNode) treePath.getLastPathComponent();
                        boolean leaf = node.isLeaf();
                        if (!node.isSelected()) {
                            if (leaf) {
                                popUpMenu.getLeafShowInstance().show(e.getComponent(),
                                        e.getX(), e.getY());
                            } else {
                                popUpMenu.getNotLeafShowInstance().show(e.getComponent(),
                                        e.getX(), e.getY());
                            }
                        } else {
                            if (leaf) {
                                popUpMenu.getLeafHideInstance().show(e.getComponent(),
                                        e.getX(), e.getY());
                            } else {
                                popUpMenu.getNotLeafHideInstance().show(e.getComponent(),
                                        e.getX(), e.getY());
                            }
                        }
                    }
                }
            });
            panel.repaint();
        }

        public void setGraphPane(final GraphPane graphPane) {
            popUpMenu.setGraphPane(graphPane);
        }

        private void updateChildren(IArcFilter filter, CheckNode parent, String namePrefix) {
            for (Enumeration nodes = parent.children(); nodes.hasMoreElements();) {
                CheckNode currentNode = (CheckNode) nodes.nextElement();
                String name = ((namePrefix.length() > 0) ? namePrefix + ": " : "") + currentNode.toString();
                if (filter.isABarrier(name)) {
                    currentNode.setSelected(false);
                } else {
                    currentNode.setSelected(true);
                }
                updateChildren(filter, currentNode, name);
            }
        }

        public JScrollPane getTreeScrollPane() {
            return scrollPane;
        }
    }
}
