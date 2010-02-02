package ru.spbu.math.ontologycomparision.zhukova.logic.owl.impl;

import ru.spbu.math.ontologycomparision.zhukova.logic.owl.IClassAnnotationVisitor;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;

import java.net.URI;

import org.semanticweb.owl.model.OWLObjectAnnotation;
import org.semanticweb.owl.model.OWLConstantAnnotation;

/**
 * @author Anna Zhukova
 */
public class ClassAnnotationVisitor implements IClassAnnotationVisitor<OntologyConcept> {
    private String label = "";

    public OntologyConcept getOntologyConcept(URI uri) {
        return new OntologyConcept(uri, this.label);
    }

    public void visit(OWLObjectAnnotation owlObjectAnnotation) {
    }

    public void visit(OWLConstantAnnotation owlConstantAnnotation) {
        if (owlConstantAnnotation.isLabel()) {
            this.label = owlConstantAnnotation.getAnnotationValue().getLiteral();
        }
    }
}
