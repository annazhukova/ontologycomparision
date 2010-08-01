package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader;


import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;

/**
 * @author Anna Zhukova
 * Visits annotations of the ontology class, stores usefull information and then is able to create appropriate node.
 */
public interface IClassAnnotationVisitor<C extends IOntologyConcept> extends OWLAnnotationObjectVisitor {

    /**
     * Given the uri of the ontology class constructs IOntologyConcept.
     * @param uri IRI of the ontology class given.
     * @return IOntologyConcept constructed.
     */
    C getOntologyConcept(IRI uri);

    void start();
}

