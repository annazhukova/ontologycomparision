package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IOntologyGraph {

    /**
     * Returns all concepts for thic ontology.
     *
     * @return List of concepts.
     */
    Collection<OntologyConcept> getConcepts();

    Map<URI, OntologyConcept> getUriToConceptMap();

    IHashTable<String, OntologyConcept, Set<OntologyConcept>> getLabelToConceptTable();

    OntologyConcept getConceptByURI(URI uri);

    Collection<OntologyRelation> getRelations();

    Collection<OntologyRelation> getRelations(String relationName);

    Map<URI, OntologyProperty> getUriToPropertyMap();

    Collection<OntologyProperty> getProperties();
}
