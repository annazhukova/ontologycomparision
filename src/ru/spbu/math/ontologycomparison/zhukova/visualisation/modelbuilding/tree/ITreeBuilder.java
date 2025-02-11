package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IPair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.tree.CheckNode;

import javax.swing.*;
import javax.swing.tree.TreeModel;

/**
 * @author Anna R. Zhukova
 */
public interface ITreeBuilder {

    IPair<TreeModel, SetHashTable<IOntologyConcept, CheckNode>> buildTree(boolean unmappedNoSynsetVisible, boolean unmappedWithSynsetVisible);
}
