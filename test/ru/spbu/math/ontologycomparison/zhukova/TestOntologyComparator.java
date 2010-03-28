package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.IOntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.impl.OntologyComparator;

import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class TestOntologyComparator extends TestCase {
    private static ILogger LOGGER = new ILogger() {
        public void log(String log) {
            System.out.println(log);
        }
    };
    private IOntologyGraph ontoPLGraph;
    private IOntologyGraph ontoJavaGraph;
    private IOntologyGraph ontoPLFull;
    private IOntologyGraph ontoJavaCSharpGraph;
    private IOntologyGraph ontoDrinkGraph;

    public void setUp() throws FileNotFoundException {
        OntologyGraphBuilder builder = new OntologyGraphBuilder();
        this.ontoPLGraph = builder.build(OntologyTestConstants.ONTOPL_URL);
        this.ontoDrinkGraph = builder.build(OntologyTestConstants.ONTODRINK_URL);
        this.ontoPLFull = builder.build(OntologyTestConstants.ONTOPLFULL_URL);
        this.ontoJavaCSharpGraph = builder.build(OntologyTestConstants.ONTOJAVASHARP_URL);
        this.ontoJavaGraph = builder.build(OntologyTestConstants.ONTOJAVA_URL);
    }

    public void testSimilarityOfTotallyDifferentOntologies() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoDrinkGraph, ontoPLGraph, LOGGER);
        assertEquals(0.0, comparator.getSimilarity());
    }

    public void testSimilarityOfSameOntologies() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoDrinkGraph, ontoDrinkGraph, LOGGER);
        assertEquals(1.0, comparator.getSimilarity());
    }

    public void testSimilarityOfOntologies() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoJavaGraph, ontoPLGraph, LOGGER);
        assertEquals(0.5, comparator.getSimilarity());
    }

    public void testSimilaritySimmerty() {
        IOntologyComparator comparator1 =
                new OntologyComparator(ontoJavaGraph, ontoPLGraph, LOGGER);
        IOntologyComparator comparator2 =
                new OntologyComparator(ontoPLGraph, ontoJavaGraph, LOGGER);
        assertEquals(comparator2.getSimilarity(), comparator1.getSimilarity());
    }

    public void testSimilarityOfOntologiesWithEmptySynsets() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoJavaCSharpGraph, ontoPLFull, LOGGER);
        assertEquals(37, (int)(comparator.getSimilarity() * 100));
    }
}
