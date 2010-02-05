/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.FileChoosers;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.Main;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.modelbuilding.GraphModelBuilder;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.modelbuilding.IGraphModelBuilder;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparision.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.OntologyComparator;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class Open extends AbstractAction {
    private static final Open INSTANCE = new Open();
    private static Main main;

    public static Open getInstance() {
        return Open.INSTANCE;
    }

    private Open() {
        super("Open...");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('o'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        File firstOwl = FileChoosers.getOpenFileChooser("Select First Ontology");
        File secondOwl = FileChoosers.getOpenFileChooser("Select Second Ontology");
        if (firstOwl != null && secondOwl != null) {
            try {
                buildGraph(firstOwl, secondOwl);
                Open.main.setIsChanged(true);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(Open.main.getFrame(), "Cannot open file",
                        "Cannot open file", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(Open.main.getFrame(), e1.getMessage(),
                        "Cannot load ontology", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    private static void buildGraph(File firstOwl, File secondOwl) throws IOException {
        IOntologyGraph<OntologyConcept,OntologyRelation> firstOntologyGraph =
                OntologyGraphBuilder.build(firstOwl);
        IOntologyGraph<OntologyConcept, OntologyRelation> secondOntologyGraph =
                OntologyGraphBuilder.build(secondOwl);
        IGraphModelBuilder myGraphModelBuilder =
                new GraphModelBuilder(firstOntologyGraph, secondOntologyGraph);
        GraphModel myGraphModel = myGraphModelBuilder.buildGraphModel(main.getGraphPane());
        Open.main.setGraphModel(myGraphModel);
        /*int similarityCount = (int)(
                (new OntologyComparator<OntologyConcept, OntologyRelation>(
                        firstOntologyGraph, secondOntologyGraph)).getSimilarity() * 100);
        Open.main.updateDescriptionPanel(String.format(
                "Comparing ontology %s (blue) to %s (green). (Absolutly equal concepts are colored orange) The similarity is %d %%.",
                firstOwl.getName(), secondOwl.getName(), similarityCount)
        );*/
    }

    public static void setMain(Main main) {
        Open.main = main;
    }
}
