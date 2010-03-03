package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.owl.OntologyLoader;
import ru.spbu.math.ontologycomparison.zhukova.logic.owl.impl.ClassAnnotationVisitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Map;

/**
 * @author Anna Zhukova
 */
public class TestOwl extends TestCase {
    private OntologyLoader<OntologyConcept, OntologyRelation> loader;

    public void setUp() throws FileNotFoundException {
        this.loader =
                new OntologyLoader<OntologyConcept, OntologyRelation>(
                        new FileInputStream(OntologyTestConstants.ONTOPL_URL));
    }

    public void testItLoadsAnything() {
        loader.load(new ClassAnnotationVisitor());
    }

    public void testLoadedConceptCount() {
        Map<URI, OntologyConcept> result = loader.load(new ClassAnnotationVisitor());
        assertEquals(OntologyTestConstants.ONTO_PL_CONCEPTS_COUNT, result.size());
    }

    public void testLoadedContent() {
        Map<URI, OntologyConcept> result = loader.load(new ClassAnnotationVisitor());
        for (OntologyConcept concept : result.values()) {
            if (concept.getLabelCollection().contains(OntologyTestConstants.JAVA)) {
                return;
            }
        }
        fail();
    }
}
