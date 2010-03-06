package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;

import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class TestGraphBuilder extends TestCase {

    public void testItBuldsAnything() throws FileNotFoundException {
        OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
    }

    public void testConceptsNumber() throws FileNotFoundException {
        IOntologyGraph graph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        assertEquals(OntologyTestConstants.ONTO_PL_CONCEPTS_COUNT, graph.getConcepts().size());
    }

    public void testConceptsContent() throws FileNotFoundException {
        IOntologyGraph graph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        for (OntologyConcept concept : graph.getConcepts()) {
            if (concept.getLabelCollection().contains(OntologyTestConstants.JAVA)) {
                return;
            }
        }
        fail();
    }

    public void testChildParentRelation() throws FileNotFoundException {
        IOntologyGraph graph = OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        for (OntologyConcept concept : graph.getConcepts()) {
            if (concept.getLabelCollection().contains(OntologyTestConstants.JAVA)) {
                for (OntologyConcept parent : concept.getParents()) {
                    if (parent.getLabelCollection().contains(OntologyTestConstants.PROGRAMMING_LANGUAGE)) {
                        return;
                    }
                }
            }
        }
        fail();
    }
}
