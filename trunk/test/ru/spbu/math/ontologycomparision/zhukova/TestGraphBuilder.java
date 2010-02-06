package ru.spbu.math.ontologycomparision.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparision.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;

import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class TestGraphBuilder extends TestCase {

    public void testItBuldsAnything() throws FileNotFoundException {
        OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
    }

    public void testConceptsNumber() throws FileNotFoundException {
        IOntologyGraph<OntologyConcept, OntologyRelation> graph =
                OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        assertEquals(OntologyTestConstants.ONTO_PL_CONCEPTS_COUNT, graph.getConcepts().size());
    }

    public void testConceptsContent() throws FileNotFoundException {
        IOntologyGraph<OntologyConcept, OntologyRelation> graph =
                OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        for (OntologyConcept concept : graph.getConcepts()) {
            if (concept.getLabel().equalsIgnoreCase(OntologyTestConstants.JAVA)) {
                return;
            }
        }
        fail();
    }

    public void testChildParentRelation() throws FileNotFoundException {
        IOntologyGraph<OntologyConcept, OntologyRelation> graph =
                OntologyGraphBuilder.build(OntologyTestConstants.ONTOPL_URL);
        for (OntologyConcept concept : graph.getConcepts()) {
            if (concept.getLabel().equalsIgnoreCase(OntologyTestConstants.JAVA)) {
                for (OntologyConcept parent : concept.getParents()) {
                    if (parent.getLabel().equalsIgnoreCase(OntologyTestConstants.PROGRAMMING_LANGUAGE)) {
                        return;
                    }
                }
            }
        }
        fail();
    }
}
