package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl;

import org.semanticweb.owlapi.model.*;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationImpl;

import java.net.URI;

/**
 * @author Anna Zhukova
 */
public class ClassAnnotationVisitor implements IClassAnnotationVisitor<IOntologyConcept> {
    private String label;
    private String comment;

    public void start() {
        label = null;
        comment = null;
    }

    public IOntologyConcept getOntologyConcept(IRI uri) {
        return new OntologyConcept(uri, this.getLabel());
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

    public void visit(IRI iri) {}

    public void visit(OWLAnonymousIndividual individual) {}

    public void visit(OWLTypedLiteral literal) {}

    public void visit(OWLStringLiteral literal) {}

    public void visit(OWLAnnotation annotation) {
            if (annotation instanceof OWLAnnotationImpl) {
                OWLAnnotationImpl owlAnnotation = (OWLAnnotationImpl) annotation;
                if (owlAnnotation.isLabel()) {
                    OWLAnnotationValue value = owlAnnotation.getValue();
                    if (value instanceof OWLLiteral) {
                        this.setLabel(((OWLLiteral) value).getLiteral());
                    }
                }
                if (owlAnnotation.isComment()) {
                    OWLAnnotationValue value = owlAnnotation.getValue();
                    if (value instanceof OWLLiteral) {
                        this.setComment(((OWLLiteral) value).getLiteral());
                    }
                }
            }
    }

    public void visit(OWLAnnotationAssertionAxiom axiom) {}

    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {}

    public void visit(OWLAnnotationPropertyDomainAxiom axiom) {}

    public void visit(OWLAnnotationPropertyRangeAxiom axiom) {}
}
