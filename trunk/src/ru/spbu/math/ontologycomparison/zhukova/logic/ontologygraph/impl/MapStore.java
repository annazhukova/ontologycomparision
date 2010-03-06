package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IMapStore;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class MapStore implements IMapStore {
    private Map<URI, OntologyConcept> uriToConcept;

    private IHashTable<String, OntologyConcept, Set<OntologyConcept>> labelToConcept;

    private IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> synsetToConcept;

    private Set<OntologyConcept> roots;

    private Map<URI, OntologyProperty> uriToProperty;

    private IHashTable<String, OntologyProperty, Set<OntologyProperty>> labelToProperty;

    public MapStore(Set<OntologyConcept> roots, Map<URI, OntologyConcept> uriToConcept, IHashTable<String, OntologyConcept, Set<OntologyConcept>> labelToConcept,
                    Map<URI, OntologyProperty> uriToProperty, IHashTable<String, OntologyProperty, Set<OntologyProperty>> labelToProperty) {
        this.uriToConcept = uriToConcept;
        this.labelToConcept = labelToConcept;
        this.roots = roots;
        this.uriToProperty = uriToProperty;
        this.labelToProperty = labelToProperty;
    }

    public Map<URI, OntologyConcept> getUriToConcept() {
        return uriToConcept;
    }

    public Set<URI> getConceptUris() {
        return uriToConcept.keySet();
    }

    public Set<URI> getPropertyUris() {
        return uriToProperty.keySet();
    }

    public Set<String> getConceptLabels() {
        return labelToConcept.keySet();
    }

    public Set<String> getPropertyLabels() {
        return labelToProperty.keySet();
    }

    public Set<Synset> getSynsets() {
        return synsetToConcept.keySet();
    }

    public void setUriToConcept(Map<URI, OntologyConcept> uriToConcept) {
        this.uriToConcept = uriToConcept;
    }

    public IHashTable<String, OntologyConcept, Set<OntologyConcept>> getLabelToConcept() {
        return labelToConcept;
    }

    public void setLabelToConcept(IHashTable<String, OntologyConcept, Set<OntologyConcept>> labelToConcept) {
        this.labelToConcept = labelToConcept;
    }

    public IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> getSynsetToConcept() {
        return synsetToConcept;
    }

    public void setSynsetToConcept(IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> synsetToConcept) {
        this.synsetToConcept = synsetToConcept;
    }

    public Set<OntologyConcept> getRoots() {
        return roots;
    }

    public void setRoots(Set<OntologyConcept> roots) {
        this.roots = roots;
    }

    public Map<URI, OntologyProperty> getUriToProperty() {
        return uriToProperty;
    }

    public void setUriToProperty(Map<URI, OntologyProperty> uriToProperty) {
        this.uriToProperty = uriToProperty;
    }

    public IHashTable<String, OntologyProperty, Set<OntologyProperty>> getLabelToProperty() {
        return labelToProperty;
    }

    public void setLabelToProperty(IHashTable<String, OntologyProperty, Set<OntologyProperty>> labelToProperty) {
        this.labelToProperty = labelToProperty;
    }

    public OntologyConcept getConceptByURI(URI uri) {
        return this.uriToConcept != null ? this.uriToConcept.get(uri) : null;
    }

    public Collection<OntologyConcept> getConcepts() {
        return uriToConcept.values();
    }
}
