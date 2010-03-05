package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyGraph implements IOntologyGraph {
    private final Map<URI, OntologyConcept> uriToConcept;
    private final Map<URI, OntologyProperty> uriToProperty;
    private final IHashTable<String, OntologyConcept, Set<OntologyConcept>> labelToConcept;

    public OntologyGraph(Map<URI, OntologyConcept> uriToConcept, Map<URI, OntologyProperty> uriToProperty, 
                         IHashTable<String, OntologyConcept, Set<OntologyConcept>> labelToConcept){
        this.uriToConcept = uriToConcept;
        this.uriToProperty = uriToProperty;
        this.labelToConcept = labelToConcept;
    }

    public IHashTable<String, OntologyConcept, Set<OntologyConcept>> getLabelToConceptTable() {
        return this.labelToConcept;
    }

    public Map<URI, OntologyConcept> getUriToConceptMap() {
        return uriToConcept;
    }

    public OntologyConcept getConceptByURI(URI uri) {
        return getUriToConceptMap().get(uri);
    }

    public Collection<OntologyRelation> getRelations(String relationName) {
        Set<OntologyRelation> result = new HashSet<OntologyRelation>();
        for (OntologyConcept concept : getConcepts()) {
            result.addAll(concept.getSubjectRelations(relationName));
        }
        return result;
    }

    public Collection<OntologyRelation> getRelations() {
        Set<OntologyRelation> result = new HashSet<OntologyRelation>();
        for (OntologyConcept concept : getConcepts()) {
            result.addAll(concept.getSubjectRelations());
        }
        return result;
    }

    public Collection<OntologyConcept> getConcepts() {
        return getUriToConceptMap().values();
    }

    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(": [");
        for (OntologyConcept concept : getConcepts()) {
            result.append(concept).append(", ");
        }
        return result.append("]").toString();
    }

    public Collection<OntologyProperty> getProperties() {
        return uriToProperty.values();
    }

    public Map<URI, OntologyProperty> getUriToPropertyMap() {
        return uriToProperty;
    }
}
