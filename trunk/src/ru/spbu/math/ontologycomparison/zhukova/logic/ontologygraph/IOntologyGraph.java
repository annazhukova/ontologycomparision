package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

/**
 * @author Anna Zhukova
 */
public interface IOntologyGraph {

    /**
     * Returns all concepts for thic ontology.
     * @return List of concepts.
     */
    public Collection<OntologyConcept> getConcepts();

    public Map<URI, OntologyConcept> getUriToConceptMap();

    public OntologyConcept getConceptByURI(URI uri);

    public Collection<OntologyRelation> getRelations();

     public Collection<OntologyRelation> getRelations(String relationName);
}
