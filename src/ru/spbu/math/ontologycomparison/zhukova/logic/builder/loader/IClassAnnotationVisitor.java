package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader;

import org.semanticweb.owl.model.OWLAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;

import java.net.URI;

/**
 * @author Anna Zhukova
 * Visits annotations of the ontology class, stores usefull information and then is able to create appropriate node.
 */
public interface IClassAnnotationVisitor<C extends IOntologyConcept> extends OWLAnnotationVisitor {

    /**
     * Given the uri of the ontology class constructs IOntologyConcept.
     * @param uri URI of the ontology class given.
     * @return IOntologyConcept constructed.
     */
    C getOntologyConcept(URI uri);

    void start();
}

