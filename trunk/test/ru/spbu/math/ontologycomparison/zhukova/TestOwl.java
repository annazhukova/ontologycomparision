package ru.spbu.math.ontologycomparison.zhukova;

import junit.framework.TestCase;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.OntologyLoader;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public class TestOwl extends TestCase {
    private OntologyLoader loader;

    public void setUp() throws FileNotFoundException {
        this.loader = new OntologyLoader(new FileInputStream(OntologyTestConstants.ONTOPL_URL));
    }

    public void testItLoadsAnything() {
        loader.load(new ClassAnnotationVisitor());
    }

    public void testLoadedConceptCount() {
        Collection<OntologyConcept> result = loader.load(new ClassAnnotationVisitor()).getConcepts();
        assertEquals(OntologyTestConstants.ONTO_PL_CONCEPTS_COUNT, result.size());
    }

    public void testLoadedContent() {
        Collection<OntologyConcept> result = loader.load(new ClassAnnotationVisitor()).getConcepts();
        for (OntologyConcept concept : result) {
            if (concept.getLabelCollection().contains(OntologyTestConstants.JAVA)) {
                return;
            }
        }
        fail();
    }
}
