package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.popupmenu;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.ConceptVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public class PopUpMenu extends JPopupMenu implements IRepaintListener, IGraphModel.IVertexListener {
    private JPanel treePanel = new JPanel();
    private GraphPane graphPane;
    private JTree tree;
    private int row;
    private final PopUpAction show;
    private final PopUpAction hideAll;
    private final PopUpAction showAll;
    private final PopUpAction hide;
    private SetHashTable<IOntologyConcept, CheckNode> conceptToCheckNodeMap;
    private IRepaintListener listener;

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
        graphPane.getGraphModel().addListener(this);
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

    public void updateTreePanel() {
        treePanel.repaint();
    }

    public void visitRow(boolean select, int selectionMode, boolean hide) {
        TreePath treePath = tree.getPathForRow(row);
        if (treePath != null && graphPane != null) {
            CheckNode node = (CheckNode) treePath.getLastPathComponent();
            ArrayList<CheckNode> nodes = new ArrayList<CheckNode>();
            node.setSelected(select, selectionMode, nodes);
            IGraphModel graphModel = graphPane.getGraphModel();
            for (CheckNode checkNode : nodes) {
                Object object = checkNode.getUserObject();
                if (!(object instanceof IOntologyConcept)) {
                    continue;
                }
                IOntologyConcept ontologyConcept = (IOntologyConcept) object;
                SimpleVertex simpleVertex = graphModel.getVertexByConcept(ontologyConcept);
                if (simpleVertex != null) {
                    SuperVertex superVertex = simpleVertex.getSuperVertex();
                    IVertex vertex = superVertex == null ? simpleVertex : superVertex;
                    if (hide) {
                        graphModel.removeVertex(vertex);
                    } else {
                        graphModel.addVertex(vertex);
                    }
                    for (IOntologyConcept similarConcept : ontologyConcept.getSimilarConcepts()) {
                        Set<CheckNode> similarNodes = conceptToCheckNodeMap.get(similarConcept);
                        if (similarNodes != null) {
                            for (CheckNode similar : similarNodes) {
                                similar.setSelected(!hide);
                            }
                        }
                    }
                }
            }
            getGraphPane().repaint();
            updateTreePanel();
            if (listener != null) {
                listener.update();
            }
        }
    }

    public void setTable(SetHashTable<IOntologyConcept, CheckNode> conceptToCheckNodeMap) {
        this.conceptToCheckNodeMap = conceptToCheckNodeMap;
    }


    public void setListener(IRepaintListener listener) {
        this.listener = listener;
        listener.update();
    }

    public void update() {
        updateTreePanel();
    }

    public void vertexAdded(IVertex... vertices) {
        for (IVertex vertex : vertices) {
            if (vertex instanceof ConceptVertex) {
                IOntologyConcept ontologyConcept = ((ConceptVertex) vertex).getConcept();
                Set<CheckNode> checkNodes = conceptToCheckNodeMap.get(ontologyConcept);
                if (checkNodes != null) {
                    for (CheckNode node : checkNodes) {
                        node.setSelected(true);
                        if (listener != null) {
                            listener.update();
                        }
                    }
                }
            }
        }
    }

    public void vertexRemoved(IVertex... vertices) {
        for (IVertex vertex : vertices) {
            if (vertex instanceof ConceptVertex) {
                IOntologyConcept ontologyConcept = ((ConceptVertex) vertex).getConcept();
                Set<CheckNode> checkNodes = conceptToCheckNodeMap.get(ontologyConcept);
                if (checkNodes != null) {
                    for (CheckNode node : checkNodes) {
                        node.setSelected(false);
                        if (listener != null) {
                            listener.update();
                        }
                    }
                }
            }
        }
    }
}
