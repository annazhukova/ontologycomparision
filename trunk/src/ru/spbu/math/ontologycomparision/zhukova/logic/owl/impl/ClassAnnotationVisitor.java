package ru.spbu.math.ontologycomparision.zhukova.logic.owl.impl;

import org.semanticweb.owl.model.OWLConstantAnnotation;
import org.semanticweb.owl.model.OWLObjectAnnotation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.owl.IClassAnnotationVisitor;

import java.net.URI;

/**
 * @author Anna Zhukova
 */
public class ClassAnnotationVisitor implements IClassAnnotationVisitor<OntologyConcept> {
    private String label;
    private String comment;

    public OntologyConcept getOntologyConcept(URI uri) {
        return new OntologyConcept(uri, this.label, this.comment);
    }

    public void visit(OWLObjectAnnotation owlObjectAnnotation) {
    }

    public void visit(OWLConstantAnnotation owlConstantAnnotation) {
        if (owlConstantAnnotation.isLabel()) {
            this.label = owlConstantAnnotation.getAnnotationValue().getLiteral();
        }
        if (owlConstantAnnotation.isComment()) {
            this.comment = owlConstantAnnotation.getAnnotationValue().getLiteral();
        }
    }
}
