package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArcFilter;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.popupmenu.PopUpMenu;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeModel;
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

    public void setTrees(IPair<TreeModel, SetHashTable<IOntologyConcept, CheckNode>> firstPair, IPair<TreeModel, SetHashTable<IOntologyConcept, CheckNode>> secondPair) {
        left.setTree(firstPair.getFirst(), secondPair.getSecond());
        right.setTree(secondPair.getFirst(), firstPair.getSecond());
        left.popUpMenu.setListener(right.popUpMenu);
        right.popUpMenu.setListener(left.popUpMenu);
        getLeftComponent().repaint();
        getRightComponent().repaint();
        repaint();
    }

    private static class TreePane extends JScrollPane {

        //tree panel
        private final JPanel panel = new JPanel(new BorderLayout());
        private final JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // tree
        private JTree tree = new JTree();
        private PopUpMenu popUpMenu = new PopUpMenu();

        public TreePane() {
            tree.setModel(new DefaultTreeModel(new CheckNode("", false, Color.WHITE)));
            tree.setVisible(false);
            panel.add(this.tree, BorderLayout.CENTER);
            this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            tree.setCellRenderer(new CheckRenderer());
            popUpMenu.setTree(this.tree, panel);
            this.tree.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
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
                }
            });
        }

        public void setTree(final TreeModel tree, final SetHashTable<IOntologyConcept, CheckNode> table) {
            this.tree.setModel(tree);
            this.tree.setVisible(true);
            popUpMenu.setTable(table);
            panel.repaint();
            this.tree.repaint();
            scrollPane.repaint();
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
