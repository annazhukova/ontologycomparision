package ru.spbu.math.ontologycomparision.zhukova.logic.owl.impl;

import ru.spbu.math.ontologycomparision.zhukova.logic.owl.IPropertyVisitor;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;

/**
 * @author Anna Zhukova
 */
public class PropertyVisitor implements IPropertyVisitor<OntologyConcept> {

    public void inRelationship(OntologyConcept node, OntologyConcept friend, String relationName) { 
          node.addSubjectRelation(new OntologyRelation(relationName, node, friend));
    }
}
