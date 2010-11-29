/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.OntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.PropertyVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.IOntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.impl.OntologyComparator;
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
        onOpenCalled();
        final File firstOwl = FileChoosers.getOpenFileChooser("Select First Ontology");
        if (firstOwl == null) {
            onOpenDone();
            return;
        }
        this.main.log("Select second ontology");
        final File secondOwl = FileChoosers.getOpenFileChooser("Select Second Ontology");
        if (secondOwl == null) {
            this.main.log("Press \"Open\" to select ontologies to compare");
            onOpenDone();
            return;
        }
        this.main.clear();
        this.main.log("Loading ontologies");
        onOntologiesChosen();
        main.showProgressBar();
        new Thread(new Runnable() {
            public void run() {
                try {
                    buildGraph(firstOwl, secondOwl);
                    Open.this.main.setIsChanged(true);
                } catch (Throwable e1) {
                    handleException(e1);
                } finally {
                    onOpenDone();
                }
            }
        }).start();
    }

    private void onOpenDone() {
        setEnabled(true);
        for (IListener listener : listeners) {
            listener.openDone();
        }
    }

    private void onOpenCalled() {
        setEnabled(false);
        for (IListener listener : listeners) {
            listener.openCalled();
        }
    }

    private void onOntologiesChosen() {
        for (IListener listener : listeners) {
            listener.ontologiesChosen();
        }
    }

    private void handleException(Throwable e1) {
        this.main.hideProgressBar();
        JOptionPane.showMessageDialog(this.main.getFrame(), String.format("Cannot open file: %s", e1.getMessage()),
                "Cannot open file", JOptionPane.ERROR_MESSAGE);
        e1.printStackTrace();
    }

    private void buildGraph(final File firstFile, final File secondFile) throws IOException {
        try {
            this.main.log(String.format("Loading %s...", firstFile.getName()));
            final OntologyManager firstOntologyManager = new OntologyManager(firstFile, this.main);
            final IOntologyGraph[] firstOntologyGraph = {null};
            Thread firstGraphThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        firstOntologyGraph[0] = firstOntologyManager.load(new ClassAnnotationVisitor(), new PropertyVisitor());
                    } catch (Throwable e1) {
                        handleException(e1);
                    }
                }
            });
            firstGraphThread.start();
            final OntologyManager secondOntologyManager = new OntologyManager(secondFile, this.main);
            Open.this.main.log(String.format("Loading %s...", secondFile.getName()));
            IOntologyGraph secondOntologyGraph = secondOntologyManager.load(new ClassAnnotationVisitor(), new PropertyVisitor());
            try {
                firstGraphThread.join();
            } catch (InterruptedException e) {
                // ignore;
            }
            if (firstOntologyGraph[0] == null) {
                return;
            }
            this.main.log("Merging ontologies...");
            IOntologyComparator ontologyComparator = new OntologyComparator(firstOntologyGraph[0], secondOntologyGraph, this.main);
            IOntologyGraph ontologyGraph = ontologyComparator.mapOntologies().getFirst();
            int similarity = (int) (ontologyComparator.getSimilarity() * 100);
            final IGraphModelBuilder myGraphModelBuilder =
                    new GraphModelBuilder(firstOntologyGraph[0], secondOntologyGraph, ontologyGraph, similarity, this.main);
            this.main.log("Visualising ontologies...");
            GraphModel graphModel = myGraphModelBuilder.buildGraphModel(main.getGraphPane(), main.areUnmappedConceptsVisible(), main.areUnmappedConceptsWithSynsetsVisible());
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology result = OntologyManager.saveOntologies(manager, firstOntologyManager.getOntology(), secondOntologyManager.getOntology());
            onGraphModelBuilt(manager, result);
            this.main.setGraphModel(graphModel);
            ITreeBuilder firstTreeBuilder = new TreeBuilder(firstFile.getName(), firstOntologyGraph[0].getRoots());
            ITreeBuilder secondTreeBuilder = new TreeBuilder(secondFile.getName(), secondOntologyGraph.getRoots());
            this.main.setTrees(firstTreeBuilder.buildTree(main.areUnmappedConceptsVisible(), main.areUnmappedConceptsWithSynsetsVisible()), secondTreeBuilder.buildTree(main.areUnmappedConceptsVisible(), main.areUnmappedConceptsWithSynsetsVisible()));
            int similarityCount = myGraphModelBuilder.getSimilarity();
            this.main.info(String.format(
                    "Comparing ontology %s (blue) to %s (green). (Absolutely equal concepts are colored orange) The similarity is %d %%.",
                    firstFile.getName(), secondFile.getName(), similarityCount)
            );
            this.main.hideProgressBar();
        } catch (Throwable e) {
            handleException(e);
        }
    }

    private void onGraphModelBuilt(OWLOntologyManager manager, OWLOntology result) {
        for (IListener listener : listeners) {
            listener.graphModelBuilt(manager, result);
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public static interface IListener {

        void openCalled();

        void ontologiesChosen();

        void graphModelBuilt(OWLOntologyManager manager, OWLOntology ontology);

        void openDone();
    }
}
