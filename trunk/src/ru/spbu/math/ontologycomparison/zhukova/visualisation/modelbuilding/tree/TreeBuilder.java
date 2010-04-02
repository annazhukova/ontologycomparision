package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Pair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckRenderer;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
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

    public IPair<JTree, SetHashTable<IOntologyConcept,CheckNode>> buildTree(boolean unmappedNoSynsetVisible, boolean unmappedWithSynsetVisible) {
        CheckNode root = new CheckNode(title, true, Color.BLACK);
        SetHashTable<IOntologyConcept, CheckNode> conceptToNodeMap = new SetHashTable<IOntologyConcept, CheckNode>();
        for (IOntologyConcept parent : this.roots) {
            CheckNode parentNode = new CheckNode(parent, isSelected(parent.hasMappedConcepts(), parent.hasSynsets(), unmappedNoSynsetVisible, unmappedWithSynsetVisible), getColor(parent.hasMappedConcepts(), parent.hasSynsets()));
            conceptToNodeMap.insert(parent, parentNode);
            addChildrenRecursively(parent, parentNode, conceptToNodeMap, unmappedNoSynsetVisible, unmappedWithSynsetVisible);
            root.add(parentNode);
        }
        final JTree tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new CheckRenderer());         
        return new Pair<JTree, SetHashTable<IOntologyConcept, CheckNode>>(tree, conceptToNodeMap);
    }

    private boolean isSelected(boolean isMapped, boolean hasSynsets, boolean unmappedNoSynsetVisible, boolean unmappedWithSynsetVisible) {
        return isMapped || hasSynsets && unmappedWithSynsetVisible || !hasSynsets && unmappedNoSynsetVisible;
    }

    private Color getColor(boolean hasMappedConcepts, boolean hasSynsets) {
        if (hasMappedConcepts) {
            return hasSynsets ? Color.GREEN : Color.BLUE;
        } else {
            return hasSynsets ? Color.RED : Color.BLACK;
        }
    }

    private void addChildrenRecursively(IOntologyConcept parent, CheckNode parentNode, SetHashTable<IOntologyConcept, CheckNode> conceptToNodeMap, boolean unmappedNoSynsetVisible, boolean unmappedWithSynsetVisible) {
        for (IOntologyConcept child : parent.getChildren()) {
            CheckNode childNode = new CheckNode(child, isSelected(child.hasMappedConcepts(), child.hasSynsets(), unmappedNoSynsetVisible, unmappedWithSynsetVisible), getColor(child.hasMappedConcepts(), child.hasSynsets()));
            conceptToNodeMap.insert(child, childNode);
            addChildrenRecursively(child, childNode, conceptToNodeMap, unmappedNoSynsetVisible, unmappedWithSynsetVisible);
            parentNode.add(childNode);
        }
    }
}
