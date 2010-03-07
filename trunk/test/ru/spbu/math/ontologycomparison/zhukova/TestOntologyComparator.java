package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.IOntologyComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.impl.OntologyComparator;

import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class TestOntologyComparator extends TestCase {
    IOntologyGraph ontoPLGraph;
    IOntologyGraph ontoJavaGraph;
    IOntologyGraph ontoPLFull;
    IOntologyGraph ontoJavaCSharpGraph;
    IOntologyGraph ontoDrinkGraph;

    public void setUp() throws FileNotFoundException {
        this.ontoPLGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        this.ontoDrinkGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTODRINK_URL);
        this.ontoPLFull = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPLFULL_URL);
        this.ontoJavaCSharpGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOJAVASHARP_URL);
        this.ontoJavaGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOJAVA_URL);
    }

    public void testSimilarityOfTotallyDifferentOntologies() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoDrinkGraph, ontoPLGraph);
        assertEquals(0.0, comparator.getSimilarity());
    }

    public void testSimilarityOfSameOntologies() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoDrinkGraph, ontoDrinkGraph);
        assertEquals(1.0, comparator.getSimilarity());
    }

    public void testSimilarityOfOntologies() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoJavaGraph, ontoPLGraph);
        assertEquals(0.5, comparator.getSimilarity());
    }

    public void testSimilaritySimmerty() {
        IOntologyComparator comparator1 =
                new OntologyComparator(ontoJavaGraph, ontoPLGraph);
        IOntologyComparator comparator2 =
                new OntologyComparator(ontoPLGraph, ontoJavaGraph);
        assertEquals(comparator2.getSimilarity(), comparator1.getSimilarity());
    }

    public void testSimilarityOfOntologiesWithEmptySynsets() {
        IOntologyComparator comparator =
                new OntologyComparator(ontoJavaCSharpGraph, ontoPLFull);
        assertEquals(37, (int)(comparator.getSimilarity() * 100));
    }
}
