/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions;

import org.apache.log4j.Logger;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.GraphModelBuilder;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.IGraphModelBuilder;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.FileChoosers;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class Open extends AbstractAction {
    private static final Open INSTANCE = new Open();
    private static final Logger LOG = Logger.getLogger(Open.class);
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
        final File firstOwl = FileChoosers.getOpenFileChooser("Select First Ontology");
        if (firstOwl == null) {
            return;
        }
        Open.main.getGraphModel().clear();
        Open.main.getGraphModel().update();
        Open.main.updateDescriptionPanel("Select second ontology");
        final File secondOwl = FileChoosers.getOpenFileChooser("Select Second Ontology");
        if (secondOwl == null) {
            Open.main.updateDescriptionPanel("Press \"Open\" to select ontologies to compare");
            return;
        }
        Open.main.updateDescriptionPanel("Loading ontologies");
        Open.main.showProgressBar();
        SwingWorker<Void, Void> wrkr = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    buildGraph(firstOwl, secondOwl);
                    Open.main.setIsChanged(true);
                } catch (IOException e1) {
                    Open.main.hideProgressBar();
                    JOptionPane.showMessageDialog(Open.main.getFrame(), "Cannot open file",
                            "Cannot open file", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (Exception e1) {
                    Open.main.hideProgressBar();
                    JOptionPane.showMessageDialog(Open.main.getFrame(), e1.getMessage(),
                            "Cannot load ontology", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
                return null;
            }
        };
        wrkr.execute();
    }

    private void buildGraph(final File firstOwl, final File secondOwl) throws IOException {
        try {
            SwingWorker<IOntologyGraph, Void> firstOntologyLoader = new SwingWorker<IOntologyGraph, Void>() {
                @Override
                protected IOntologyGraph doInBackground() throws Exception {
                    Open.main.updateDescriptionPanel(String.format("Loading %s...", firstOwl.getName()));
                    return OntologyGraphBuilder.build(firstOwl);
                }
            };
            firstOntologyLoader.execute();
            Open.main.updateDescriptionPanel(String.format("Loading %s...", secondOwl.getName()));
            IOntologyGraph secondGraph = OntologyGraphBuilder.build(secondOwl);
            final IGraphModelBuilder myGraphModelBuilder =
                    new GraphModelBuilder(firstOntologyLoader.get(), secondGraph);
            System.out.printf("Merging\n");
            Open.main.updateDescriptionPanel("Merging ontologies...");
            GraphModel graphModel = myGraphModelBuilder.buildGraphModel(main.getGraphPane(), main.areUnmappedConceptsVisible(), main.areUnmappedConceptsWithSynsetsVisible());
            Open.main.setGraphModel(graphModel);
            int similarityCount = myGraphModelBuilder.getSimilarity();
            Open.main.updateDescriptionPanel(String.format(
                    "Comparing ontology %s (blue) to %s (green). (Absolutly equal concepts are colored orange) The similarity is %d %%.",
                    firstOwl.getName(), secondOwl.getName(), similarityCount)
            );
            System.out.println(similarityCount);
            Open.main.hideProgressBar();
        } catch (Exception e) {
            Open.main.hideProgressBar();
            e.printStackTrace();
            JOptionPane.showMessageDialog(Open.main.getFrame(), e.getMessage(), "Cannot load ontologies", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void setMain(Main main) {
        Open.main = main;
    }
}
