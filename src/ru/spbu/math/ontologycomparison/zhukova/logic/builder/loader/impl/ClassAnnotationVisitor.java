package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl;

import org.semanticweb.owl.model.OWLConstantAnnotation;
import org.semanticweb.owl.model.OWLObjectAnnotation;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;

import java.net.URI;

/**
 * @author Anna Zhukova
 */
public class ClassAnnotationVisitor implements IClassAnnotationVisitor<IOntologyConcept> {
    private String label;
    private String comment;

    public IOntologyConcept getOntologyConcept(URI uri) {
        return new OntologyConcept(uri, this.getLabel());
    }

    public void visit(OWLObjectAnnotation owlObjectAnnotation) {
    }

    public void visit(OWLConstantAnnotation owlConstantAnnotation) {
        if (owlConstantAnnotation.isLabel()) {
            this.setLabel(owlConstantAnnotation.getAnnotationValue().getLiteral());
        }
        if (owlConstantAnnotation.isComment()) {
            this.setComment(owlConstantAnnotation.getAnnotationValue().getLiteral());
        }
    }

    public String getLabel() {
        return label;
    }

    public String getComment() {
        return comment;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
