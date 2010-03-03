package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.OntologyMapper;

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
        OntologyMapper comparator =
                new OntologyMapper(ontoDrinkGraph, ontoPLGraph);
        assertEquals(0.0, comparator.getSimilarity());
    }

    public void testSimilarityOfSameOntologies() {
        OntologyMapper comparator =
                new OntologyMapper(ontoDrinkGraph, ontoDrinkGraph);
        assertEquals(1.0, comparator.getSimilarity());
    }

    public void testSimilarityOfOntologies() {
        OntologyMapper comparator =
                new OntologyMapper(ontoJavaGraph, ontoPLGraph);
        assertEquals(0.5, comparator.getSimilarity());
    }

    public void testSimilaritySimmerty() {
        OntologyMapper comparator1 =
                new OntologyMapper(ontoJavaGraph, ontoPLGraph);
        OntologyMapper comparator2 =
                new OntologyMapper(ontoPLGraph, ontoJavaGraph);
        assertEquals(comparator2.getSimilarity(), comparator1.getSimilarity());
    }

    public void testSimilarityOfOntologiesWithEmptySynsets() {
        OntologyMapper comparator =
                new OntologyMapper(ontoJavaCSharpGraph, ontoPLFull);
        assertEquals(37, (int)(comparator.getSimilarity() * 100));
    }
}
