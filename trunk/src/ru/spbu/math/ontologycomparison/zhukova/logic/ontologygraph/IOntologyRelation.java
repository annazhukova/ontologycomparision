package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import java.net.URI;

/**
 * @author Anna Zhukova
 */
public interface IOntologyRelation {

    IOntologyConcept getSubject();

    IOntologyConcept getObject();
    
    String getRelationName();

    URI getUri();

    boolean isTransitive();
}
