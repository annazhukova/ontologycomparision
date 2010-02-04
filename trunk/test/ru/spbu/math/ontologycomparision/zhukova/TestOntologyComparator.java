package ru.spbu.math.ontologycomparision.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.OntologyComparator;

import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class TestOntologyComparator extends TestCase {
    IOntologyGraph<OntologyConcept, OntologyRelation> ontoPLGraph;
    IOntologyGraph<OntologyConcept, OntologyRelation> ontoJavaGraph;
    IOntologyGraph<OntologyConcept, OntologyRelation> ontoDrinkGraph;

    public void setUp() throws FileNotFoundException {
        this.ontoPLGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        this.ontoDrinkGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTODRINK_URL);
        this.ontoJavaGraph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOJAVA_URL);
    }

    public void testSimilarityOfTotallyDifferentOntologies() {
        OntologyComparator<OntologyConcept, OntologyRelation> comparator =
                new OntologyComparator<OntologyConcept, OntologyRelation>(ontoDrinkGraph, ontoPLGraph);
        assertEquals(0.0, comparator.getSimilarity());
    }

    public void testSimilarityOfSameOntologies() {
        OntologyComparator<OntologyConcept, OntologyRelation> comparator =
                new OntologyComparator<OntologyConcept, OntologyRelation>(ontoDrinkGraph, ontoDrinkGraph);
        assertEquals(1.0, comparator.getSimilarity());
    }

    public void testSimilarityOfOntologies() {
        OntologyComparator<OntologyConcept, OntologyRelation> comparator =
                new OntologyComparator<OntologyConcept, OntologyRelation>(ontoJavaGraph, ontoPLGraph);
        assertEquals(0.5, comparator.getSimilarity());
    }
}
