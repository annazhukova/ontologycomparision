package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;

import javax.swing.*;
import java.util.Map;

/**
 * @author Anna R. Zhukova
 */
public interface ITreeBuilder {

    IPair<JTree, Map<IOntologyConcept, CheckNode>> buildTree();
}
