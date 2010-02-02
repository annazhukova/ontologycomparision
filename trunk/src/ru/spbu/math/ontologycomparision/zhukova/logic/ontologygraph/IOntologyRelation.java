package ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph;

/**
 * @author Anna Zhukova
 */
public interface IOntologyRelation<C extends IOntologyConcept> {

    C getSubject();

    C getObject();
    
    String getRelationName();
}
