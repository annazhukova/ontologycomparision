package ru.spbu.math.ontologycomparison.zhukova.logic.owl.impl;

import org.semanticweb.owl.model.OWLPropertyExpression;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.logic.owl.IPropertyVisitor;

/**
 * @author Anna Zhukova
 */
public class PropertyVisitor implements IPropertyVisitor<OntologyConcept> {

    public void inRelationship(OntologyConcept node, OntologyConcept friend, OWLPropertyExpression property) {
        if (node != null) {
          node.addSubjectRelation(new OntologyRelation(property.toString(), false, node, friend));
        }
    }
}
