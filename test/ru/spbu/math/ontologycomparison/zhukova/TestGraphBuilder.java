package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import net.sourceforge.fluxion.utils.OWLTransformationException;
import org.semanticweb.owl.inference.OWLReasonerException;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;

import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class TestGraphBuilder extends TestCase {

    public void testItBuildsAnything() throws FileNotFoundException, OWLTransformationException, OWLReasonerException {
        new OntologyGraphBuilder().build(OntologyTestConstants.ONTOPL_URL);
    }

    public void testConceptsNumber() throws FileNotFoundException, OWLTransformationException, OWLReasonerException {
        IOntologyGraph graph = new OntologyGraphBuilder().build(OntologyTestConstants.ONTOPL_URL);
        assertEquals(OntologyTestConstants.ONTO_PL_CONCEPTS_COUNT, graph.getConcepts().size());
    }

    public void testConceptsContent() throws FileNotFoundException, OWLTransformationException, OWLReasonerException {
        IOntologyGraph graph = new OntologyGraphBuilder().build(OntologyTestConstants.ONTOPL_URL);
        for (IOntologyConcept concept : graph.getConcepts()) {
            if (concept.getLabelCollection().contains(OntologyTestConstants.JAVA)) {
                return;
            }
        }
        fail();
    }

    public void testChildParentRelation() throws FileNotFoundException, OWLTransformationException, OWLReasonerException {
        IOntologyGraph graph = new OntologyGraphBuilder().build(OntologyTestConstants.ONTOPL_URL);
        for (IOntologyConcept concept : graph.getConcepts()) {
            if (concept.getLabelCollection().contains(OntologyTestConstants.JAVA)) {
                for (IOntologyConcept parent : concept.getParents()) {
                    if (parent.getLabelCollection().contains(OntologyTestConstants.PROGRAMMING_LANGUAGE)) {
                        return;
                    }
                }
            }
        }
        fail();
    }
}
