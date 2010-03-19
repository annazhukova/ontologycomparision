package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckRenderer;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
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

    public JTree buildTree() {
        CheckNode root = new CheckNode(title);
        for (IOntologyConcept parent : this.roots) {
            CheckNode parentNode = new CheckNode(parent);
            addChildrenRecursively(parent, parentNode);
            root.add(parentNode);
        }
        final JTree tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new CheckRenderer());         
        return tree;
    }

    private void addChildrenRecursively(IOntologyConcept parent, CheckNode parentNode) {
        for (IOntologyConcept child : parent.getChildren()) {
            CheckNode childNode = new CheckNode(child);
            addChildrenRecursively(child, childNode);
            parentNode.add(childNode);
        }
    }
}
