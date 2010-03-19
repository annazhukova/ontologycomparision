package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.popupmenu;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.Vertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;

/**
 * @author Anna R. Zhukova
 */
public class PopUpMenu extends JPopupMenu {
    private JPanel treePanel = new JPanel();
    private GraphPane graphPane;
    private JTree tree;
    private int row;
    private final PopUpAction show;
    private final PopUpAction hideAll;
    private final PopUpAction showAll;
    private final PopUpAction hide;

    public PopUpMenu() {
        super();
        show = new PopUpAction("show", this, true, CheckNode.OUR_SINGLE_SELECTION, false);
        add(show);
        showAll = new PopUpAction("show all", this, true, CheckNode.OUR_DIG_IN_SELECTION, false);
        add(showAll);
        addSeparator();
        hide = new PopUpAction("hide", this, false, CheckNode.OUR_SINGLE_SELECTION, true);
        add(hide);
        hideAll = new PopUpAction("hide all", this, false, CheckNode.OUR_DIG_IN_SELECTION, true);
        add(hideAll);
    }

    public void setGraphPane(GraphPane graphPane) {
        this.graphPane = graphPane;
    }

    public GraphPane getGraphPane() {
        return graphPane;
    }

    private void enable(boolean enableShow, boolean enableShowAll, boolean enableHide, boolean enableHideAll) {
        show.setEnabled(enableShow);
        hide.setEnabled(enableHide);
        showAll.setEnabled(enableShowAll);
        hideAll.setEnabled(enableHideAll);
    }

    public PopUpMenu getLeafShowInstance() {
        enable(true, false, false, false);
        return this;
    }

    public PopUpMenu getLeafHideInstance() {
        enable(false, false, true, false);
        return this;
    }

    public PopUpMenu getNotLeafShowInstance() {
        enable(true, true, false, true);
        return this;
    }

    public PopUpMenu getNotLeafHideInstance() {
        enable(false, true, true, true);
        return this;
    }

    public void setTree(JTree tree, JPanel treePanel) {
        this.treePanel = treePanel;
        this.tree = tree;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public JTree getTree() {
        return tree;
    }

    public int getRow() {
        return row;
    }

    public void updateTreePanel() {
        treePanel.repaint();
    }

    public void visitRow(boolean select, int selectionMode, boolean hide) {
        TreePath treePath = tree.getPathForRow(row);
        if (treePath != null) {
            CheckNode node = (CheckNode) treePath.getLastPathComponent();
            ArrayList<CheckNode> nodes = new ArrayList<CheckNode>();
            node.setSelected(select, selectionMode, nodes);
            IGraphModel gm = graphPane.getGraphModel();
            for (CheckNode checkNode : nodes) {
                boolean leaf = checkNode.isLeaf();
                String name = checkNode.toString();
                Vertex v;
                if (leaf) {
                    if (checkNode.getParent() != null) {
                        name = checkNode.getParent().toString() + "." + name;
                    }
                    v = gm.getNameToSimpleVertexMap().get(name);
                } else {
                    v = gm.getKeyToSuperVertexMap().get(name);
                }
                if (v != null) {
                    if (hide) {
                        gm.removeVertex(v);
                    } else {
                        gm.addVertex(v);
                    }
                }
            }
            ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
            // if node is root
            if (row == 0) {
                tree.revalidate();
                tree.repaint();
            }
            getGraphPane().repaint();
            updateTreePanel();
        }
    }
}
