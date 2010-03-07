package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl;

import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLPropertyExpression;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IPropertyVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;

/**
 * @author Anna Zhukova
 */
public class PropertyVisitor implements IPropertyVisitor<IOntologyConcept> {

    public void inRelationship(IOntologyConcept node, IOntologyConcept friend, OWLPropertyExpression property) {
        if (node != null && property instanceof OWLObjectProperty) {
            OWLObjectProperty objectProperty = (OWLObjectProperty) property;
            node.addSubjectRelation(new OntologyRelation(objectProperty.getURI(), objectProperty.toString(), false, node, friend));
        }
    }
}
