package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;

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

    public OntologyGraph(Map<URI, OntologyConcept> uriToConcept){
        this.uriToConcept = uriToConcept;
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
}
