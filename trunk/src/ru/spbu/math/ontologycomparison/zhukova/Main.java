package ru.spbu.math.ontologycomparison.zhukova;

import org.semanticweb.owlapi.reasoner.OWLReasonerException;
import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.OntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.PropertyVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.IOntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.impl.OntologyComparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * @author anna
 *         7/20/11 12:19 AM
 */
public class Main implements ILogger {

    public static void main(String[] args) {
        Main main = new Main();
        if (args == null || args.length == 0) {
            try {
                ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.Main.main(null);
            } catch (FileNotFoundException e) {
                main.handleException(e);
            }
        } else if (args.length < 3) {
            main.displayError();
        } else {
            PrintWriter writer = null;
            try {
                File firstOwl = new File(args[0]);
                File secondOwl = new File(args[1]);
                File output = new File(args[2]);
                IOntologyGraph ontologyGraph = main.buildGraph(firstOwl, secondOwl);
                main.log("Saving alignment");
                output.createNewFile();
                writer = new PrintWriter(output);
                Collection<IOntologyConcept> concepts = ontologyGraph.getConcepts();
                    for (IOntologyConcept concept : concepts) {
                        Collection<IOntologyConcept> similarConcepts = concept.getSimilarConcepts();
                        if (similarConcepts != null && !similarConcepts.isEmpty()) {
                            writer.println(String.format("%s %s", concept, similarConcepts.iterator().next()));
                        }
                    }
                writer.flush();
            } catch (FileNotFoundException e) {
                main.displayError();
            } catch (IOException e) {
                main.handleException(e);
            } catch (OWLReasonerException e) {
                main.handleException(e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    private IOntologyGraph buildGraph(final File firstFile, final File secondFile) throws IOException, OWLReasonerException {

        log(String.format("Loading %s...", firstFile.getName()));
        final OntologyManager firstOntologyManager = new OntologyManager(firstFile, this);
        final IOntologyGraph[] firstOntologyGraph = {null};
        Thread firstGraphThread = new Thread(new Runnable() {
            public void run() {
                try {
                    firstOntologyGraph[0] = firstOntologyManager.load(new ClassAnnotationVisitor(), new PropertyVisitor());
                } catch (Throwable e1) {
                    Main.this.handleException(e1);
                }
            }
        });
        firstGraphThread.start();
        final OntologyManager secondOntologyManager = new OntologyManager(secondFile, this);
        log(String.format("Loading %s...", secondFile.getName()));
        IOntologyGraph secondOntologyGraph = secondOntologyManager.load(new ClassAnnotationVisitor(), new PropertyVisitor());
        try {
            firstGraphThread.join();
        } catch (InterruptedException e) {
            // ignore;
        }
        if (firstOntologyGraph[0] == null) {
            return null;
        }
        log("Merging ontologies...");
        IOntologyComparator ontologyComparator = new OntologyComparator(firstOntologyGraph[0], secondOntologyGraph, this);
        return ontologyComparator.mapOntologies().getFirst();
    }

    private void handleException(Throwable e1) {
        e1.printStackTrace();
    }

    public void log(String log) {
        System.out.println(log);
    }

    public void info(String log) {
        System.out.println(log);
    }

    private void displayError() {
        handleException(new IllegalArgumentException("Please, specify arguments, e.g. first_ontology.owl second_ontology.owl result.owl"));
    }
}
