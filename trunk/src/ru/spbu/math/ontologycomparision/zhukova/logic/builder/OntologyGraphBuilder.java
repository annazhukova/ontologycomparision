package ru.spbu.math.ontologycomparision.zhukova.logic.builder;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.owl.OntologyLoader;
import ru.spbu.math.ontologycomparision.zhukova.logic.owl.impl.PropertyVisitor;
import ru.spbu.math.ontologycomparision.zhukova.logic.owl.impl.ClassAnnotationVisitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.net.URI;
import java.util.Map;

/**
 * @author Anna Zhukova
 */
public class OntologyGraphBuilder {

    public static IOntologyGraph<OntologyConcept, OntologyRelation> build(String ontologyPath)
            throws FileNotFoundException {
        OntologyLoader<OntologyConcept, OntologyRelation> ontologyLoader =
                new OntologyLoader<OntologyConcept, OntologyRelation>(new FileInputStream(ontologyPath));
        Map<URI, OntologyConcept> result = ontologyLoader.load(new ClassAnnotationVisitor(),
                new PropertyVisitor());
        return new OntologyGraph(result);
    }

    public static IOntologyGraph<OntologyConcept, OntologyRelation> build(File ontologyFile)
            throws FileNotFoundException {
        OntologyLoader<OntologyConcept, OntologyRelation> ontologyLoader =
                new OntologyLoader<OntologyConcept, OntologyRelation>(ontologyFile);
        Map<URI, OntologyConcept> result = ontologyLoader.load(new ClassAnnotationVisitor(),
                new PropertyVisitor());
        return new OntologyGraph(result);
    }

}
