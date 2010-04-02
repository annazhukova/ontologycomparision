/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.OntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.GraphModelBuilder;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.IGraphModelBuilder;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.ITreeBuilder;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.TreeBuilder;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.FileChoosers;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class Open extends AbstractAction {
    private Main main;
    private Set<IListener> listeners = new LinkedHashSet<IListener>();

    public Open() {
        super("Open...");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('o'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
    }

    public void addListener(IListener listener) {
        listeners.add(listener);
    }

    public void actionPerformed(ActionEvent e) {
        final File firstOwl = FileChoosers.getOpenFileChooser("Select First Ontology");
        if (firstOwl == null) {
            return;
        }
        this.main.getGraphModel().clear();
        this.main.getGraphModel().update();
        this.main.log("Select second ontology");
        final File secondOwl = FileChoosers.getOpenFileChooser("Select Second Ontology");
        if (secondOwl == null) {
            this.main.log("Press \"Open\" to select ontologies to compare");
            return;
        }
        this.main.log("Loading ontologies");
        for (IListener listener : listeners) {
            listener.openCalled();
        }


        main.showProgressBar();
        new Thread(new Runnable() {
            public void run() {
                try {
                    buildGraph(firstOwl, secondOwl);
                    Open.this.main.setIsChanged(true);
                } catch (IOException e1) {
                    Open.this.main.hideProgressBar();
                    JOptionPane.showMessageDialog(Open.this.main.getFrame(), "Cannot open file",
                            "Cannot open file", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (Exception e1) {
                    Open.this.main.hideProgressBar();
                    JOptionPane.showMessageDialog(Open.this.main.getFrame(), e1.getMessage(),
                            "Cannot load ontology", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    private void buildGraph(final File firstOwl, final File secondOwl) throws IOException {
        try {
            final OntologyGraphBuilder firstBuilder = new OntologyGraphBuilder();
            /*SwingWorker<IOntologyGraph, Void> firstOntologyLoader = new SwingWorker<IOntologyGraph, Void>() {
                @Override
                protected IOntologyGraph doInBackground() throws Exception {
                    Open.this.main.log(String.format("Loading %s...", firstOwl.getName()));
                    return firstBuilder.build(firstOwl);
                }
            };
            firstOntologyLoader.execute();*/
            Open.this.main.log(String.format("Loading %s...", firstOwl.getName()));
            IOntologyGraph firstGraph = firstBuilder.build(firstOwl);//firstOntologyLoader.get();
            this.main.log(String.format("Loading %s...", secondOwl.getName()));
            OntologyGraphBuilder secondBuilder = new OntologyGraphBuilder();
            IOntologyGraph secondGraph = secondBuilder.build(secondOwl);
            this.main.log("Merging ontologies...");
            final IGraphModelBuilder myGraphModelBuilder =
                    new GraphModelBuilder(firstGraph, secondGraph, this.main);
            this.main.log("Visualising ontologies...");
            GraphModel graphModel = myGraphModelBuilder.buildGraphModel(main.getGraphPane(), main.areUnmappedConceptsVisible(), main.areUnmappedConceptsWithSynsetsVisible());
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology result = OntologyManager.saveOntologies(manager, firstBuilder.getOntology(), secondBuilder.getOntology());
            for (IListener listener : listeners) {
                listener.openDone(manager, result);
            }
            this.main.setGraphModel(graphModel);
            ITreeBuilder firstTreeBuilder = new TreeBuilder(firstOwl.getName(), firstGraph.getRoots());
            ITreeBuilder secondTreeBuilder = new TreeBuilder(secondOwl.getName(), secondGraph.getRoots());
            this.main.setTrees(firstTreeBuilder.buildTree(main.areUnmappedConceptsVisible(), main.areUnmappedConceptsWithSynsetsVisible()), secondTreeBuilder.buildTree(main.areUnmappedConceptsVisible(), main.areUnmappedConceptsWithSynsetsVisible()));
            int similarityCount = myGraphModelBuilder.getSimilarity();
            this.main.log(String.format(
                    "Comparing ontology %s (blue) to %s (green).<br>(Absolutely equal concepts are colored orange)<br>The similarity is %d %%.",
                    firstOwl.getName(), secondOwl.getName(), similarityCount)
            );
            this.main.hideProgressBar();
        } catch (Exception e) {
            this.main.hideProgressBar();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.main.getFrame(), e.getMessage(), "Cannot load ontologies", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public static interface IListener {

        void openCalled();

        void openDone(OWLOntologyManager manager, OWLOntology ontology);
    }
}
