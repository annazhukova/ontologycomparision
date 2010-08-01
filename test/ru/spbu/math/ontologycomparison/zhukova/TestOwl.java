package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
//import net.sourceforge.fluxion.utils.OWLTransformationException;
import org.semanticweb.owlapi.reasoner.OWLReasonerException;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IOntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.OntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public class TestOwl extends TestCase {
    private IOntologyManager loader;

    public void setUp() throws FileNotFoundException {
        this.loader = new OntologyManager(new FileInputStream(OntologyTestConstants.ONTOPL_URL));
    }

    public void testItLoadsAnything() throws /*OWLTransformationException,*/ OWLReasonerException {
        loader.load(new ClassAnnotationVisitor());
    }

    public void testLoadedConceptCount() throws /*OWLTransformationException,*/ OWLReasonerException {
        Collection<IOntologyConcept> result = loader.load(new ClassAnnotationVisitor()).getConcepts();
        assertEquals(OntologyTestConstants.ONTO_PL_CONCEPTS_COUNT, result.size());
    }

    public void testLoadedContent() throws /*OWLTransformationException,*/ OWLReasonerException {
        Collection<IOntologyConcept> result = loader.load(new ClassAnnotationVisitor()).getConcepts();
        for (IOntologyConcept concept : result) {
            if (concept.getLabelCollection().contains(OntologyTestConstants.JAVA)) {
                return;
            }
        }
        fail();
    }
}
