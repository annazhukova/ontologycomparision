package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IMapStore;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.OntologyComparator;

import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class TestOntologyComparator extends TestCase {
    IMapStore ontoPLGraph;
    IMapStore ontoJavaGraph;
    IMapStore ontoPLFull;
    IMapStore ontoJavaCSharpGraph;
    IMapStore ontoDrinkGraph;

    public void setUp() throws FileNotFoundException {
        this.ontoPLGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        this.ontoDrinkGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTODRINK_URL);
        this.ontoPLFull = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPLFULL_URL);
        this.ontoJavaCSharpGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOJAVASHARP_URL);
        this.ontoJavaGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOJAVA_URL);
    }

    public void testSimilarityOfTotallyDifferentOntologies() {
        OntologyComparator comparator =
                new OntologyComparator(ontoDrinkGraph, ontoPLGraph);
        assertEquals(0.0, comparator.getSimilarity());
    }

    public void testSimilarityOfSameOntologies() {
        OntologyComparator comparator =
                new OntologyComparator(ontoDrinkGraph, ontoDrinkGraph);
        assertEquals(1.0, comparator.getSimilarity());
    }

    public void testSimilarityOfOntologies() {
        OntologyComparator comparator =
                new OntologyComparator(ontoJavaGraph, ontoPLGraph);
        assertEquals(0.5, comparator.getSimilarity());
    }

    public void testSimilaritySimmerty() {
        OntologyComparator comparator1 =
                new OntologyComparator(ontoJavaGraph, ontoPLGraph);
        OntologyComparator comparator2 =
                new OntologyComparator(ontoPLGraph, ontoJavaGraph);
        assertEquals(comparator2.getSimilarity(), comparator1.getSimilarity());
    }

    public void testSimilarityOfOntologiesWithEmptySynsets() {
        OntologyComparator comparator =
                new OntologyComparator(ontoJavaCSharpGraph, ontoPLFull);
        assertEquals(37, (int)(comparator.getSimilarity() * 100));
    }
}
