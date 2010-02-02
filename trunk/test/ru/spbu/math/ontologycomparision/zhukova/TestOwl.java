package ru.spbu.math.ontologycomparision.zhukova;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Map;

import ru.spbu.math.ontologycomparision.zhukova.logic.owl.OntologyLoader;
import ru.spbu.math.ontologycomparision.zhukova.logic.owl.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import junit.framework.TestCase;

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
            if (concept.getLabel().equalsIgnoreCase(OntologyTestConstants.JAVA)) {
                return;
            }
        }
        fail();
    }
}
