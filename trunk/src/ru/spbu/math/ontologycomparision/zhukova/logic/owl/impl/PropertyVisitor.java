package ru.spbu.math.ontologycomparision.zhukova.logic.owl.impl;

import org.semanticweb.owl.model.OWLPropertyExpression;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.owl.IPropertyVisitor;

/**
 * @author Anna Zhukova
 */
public class PropertyVisitor implements IPropertyVisitor<OntologyConcept> {

    public void inRelationship(OntologyConcept node, OntologyConcept friend, OWLPropertyExpression property) {
        if (node != null) {
/*
          if (property.toString().contains("part_of")) {
              System.out.println(property.getClass());
              System.out.println(property.getSignature());
              System.out.println(property.getDataPropertiesInSignature());
              System.out.println(property.getIndividualsInSignature());
              System.out.println(property.getObjectPropertiesInSignature());
              OWLObjectPropertyImpl objprop = (OWLObjectPropertyImpl) property;
              System.out.println(objprop.asOWLObjectProperty().getURI());
              System.out.println(objprop.isAnonymous());
              System.out.println(objprop.isBuiltIn());
              System.exit(0);
          }
          node.addSubjectRelation(new OntologyRelation(property.toString(), node, friend));*/
        }
    }
}
