package ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph;

import java.util.Map;
import java.util.Collection;
import java.net.URI;

/**
 * @author Anna Zhukova
 */
public interface IOntologyGraph<C extends IOntologyConcept, R extends IOntologyRelation<C>> {

    /**
     * Returns all concepts for thic ontology.
     * @return List of concepts.
     */
    public Collection<C> getConcepts();

    public Map<URI, C> getUriToConceptMap();

    public C getConceptByURI(URI uri);

    public Collection<R> getRelations();

     public Collection<R> getRelations(String relationName);
}
