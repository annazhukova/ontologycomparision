package ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.impl.OntologyRelation;

import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.net.URI;

/**
 * @author Anna Zhukova
 */
public class OntologyGraph implements IOntologyGraph<OntologyConcept, OntologyRelation> {
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
            result.addAll(concept.getRelations(relationName));
        }
        return result;
    }

    public Collection<OntologyRelation> getRelations() {
        Set<OntologyRelation> result = new HashSet<OntologyRelation>();
        for (OntologyConcept concept : getConcepts()) {
            result.addAll(concept.getRelations());
        }
        return result;
    }

    public Collection<OntologyConcept> getConcepts() {
        return getUriToConceptMap().values();
    }

    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(":\n");
        for (OntologyConcept concept : getConcepts()) {
            result.append(concept);
        }
        return result.toString();
    }
}
