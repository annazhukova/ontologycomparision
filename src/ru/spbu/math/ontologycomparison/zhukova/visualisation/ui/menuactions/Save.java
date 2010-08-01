package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions;

import org.semanticweb.owlapi.model.*;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.OntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.ConceptVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.FileChoosers;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class Save extends AbstractAction implements Open.IListener {
    private OWLOntology ontology;
    private OWLOntologyManager manager;
    private Main main;

    public Save() {
        super("Save...");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('s'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        final File file = FileChoosers.getSaveFileChooser();
        if (file == null) {
            return;
        }
        this.main.log("Saving ontology");
        this.main.showProgressBar();
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (SuperVertex vertex : main.getGraphModel().getSuperVertices()) {
                            Set<OWLClass> owlClasses = new HashSet<OWLClass>();
                            for (SimpleVertex simple : vertex.getSimpleVertices()) {
                                ConceptVertex conceptVertex = (ConceptVertex) simple;
                                owlClasses.add(conceptVertex.getConcept().getOWLClass());
                            }
                            OntologyManager.addEquivalentClasses(manager, ontology, owlClasses);
                    }
                    OntologyManager.saveResult(manager, ontology, file);
                    Save.this.main.info("Ontology saved");
                    Save.this.main.hideProgressBar();
                } catch (OWLOntologyStorageException e1) {
                    Save.this.main.hideProgressBar();
                    JOptionPane.showMessageDialog(Save.this.main.getFrame(), e1.getMessage(),
                            "Cannot save ontology", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (OWLOntologyChangeException e1) {
                    Save.this.main.hideProgressBar();
                    JOptionPane.showMessageDialog(Save.this.main.getFrame(), e1.getMessage(),
                            "Cannot save ontology", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    Save.this.main.hideProgressBar();
                    JOptionPane.showMessageDialog(Save.this.main.getFrame(), e1.getMessage(),
                            "Cannot save ontology", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void openCalled() {
        this.manager = null;
        this.ontology = null;
    }

    public void openDone(OWLOntologyManager manager, OWLOntology ontology) {
        this.manager = manager;
        this.ontology = ontology;
    }
}
