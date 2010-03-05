package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import java.net.URI;

/**
 * @author Anna Zhukova
 */
public interface IOntologyRelation<C extends IOntologyConcept> {

    C getSubject();

    C getObject();
    
    String getRelationName();

    URI getUri();
}
