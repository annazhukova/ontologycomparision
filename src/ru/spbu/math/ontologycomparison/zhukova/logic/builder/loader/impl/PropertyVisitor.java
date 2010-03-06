package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl;

import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLPropertyExpression;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IPropertyVisitor;

/**
 * @author Anna Zhukova
 */
public class PropertyVisitor implements IPropertyVisitor<OntologyConcept> {

    public void inRelationship(OntologyConcept node, OntologyConcept friend, OWLPropertyExpression property) {
        if (node != null && property instanceof OWLObjectProperty) {
            OWLObjectProperty objectProperty = (OWLObjectProperty) property;
            node.addSubjectRelation(new OntologyRelation(objectProperty.getURI(), objectProperty.toString(), false, node, friend));
        }
    }
}
