package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree;

import com.sun.istack.internal.NotNull;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Pair;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckRenderer;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public class TreeBuilder implements ITreeBuilder {
    private final String title;
    private final Set<IOntologyConcept> roots;

    public TreeBuilder(String title, Set<IOntologyConcept> roots) {
        this.title = title;
        this.roots = roots;
    }

    public IPair<JTree, Map<IOntologyConcept, CheckNode>> buildTree() {
        CheckNode root = new CheckNode(title, Color.BLACK);
        Map<IOntologyConcept, CheckNode> conceptToNodeMap = new HashMap<IOntologyConcept, CheckNode>();
        for (IOntologyConcept parent : this.roots) {
            CheckNode parentNode = new CheckNode(parent, getColor(parent.hasMappedConcepts(), parent.hasSynsets()));
            conceptToNodeMap.put(parent, parentNode);
            addChildrenRecursively(parent, parentNode, conceptToNodeMap);
            root.add(parentNode);
        }
        final JTree tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new CheckRenderer());         
        return new Pair<JTree, Map<IOntologyConcept, CheckNode>>(tree, conceptToNodeMap);
    }

    private Color getColor(boolean hasMappedConcepts, boolean hasSynsets) {
        if (hasMappedConcepts) {
            return hasSynsets ? Color.GREEN : Color.BLUE;
        } else {
            return hasSynsets ? Color.RED : Color.BLACK;
        }
    }

    private void addChildrenRecursively(IOntologyConcept parent, CheckNode parentNode, Map<IOntologyConcept, CheckNode> conceptToNodeMap) {
        for (IOntologyConcept child : parent.getChildren()) {
            CheckNode childNode = new CheckNode(child, getColor(child.hasMappedConcepts(), child.hasSynsets()));
            conceptToNodeMap.put(child, childNode);
            addChildrenRecursively(child, childNode, conceptToNodeMap);
            parentNode.add(childNode);
        }
    }
}
