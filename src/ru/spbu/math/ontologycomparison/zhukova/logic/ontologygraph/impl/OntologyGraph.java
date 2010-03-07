package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyGraph implements IOntologyGraph {
    private Map<URI, IOntologyConcept> uriToConcept;

    private IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept;

    private IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept;

    private Set<IOntologyConcept> roots;

    private Map<URI, IOntologyProperty> uriToProperty;

    private IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty;

    public OntologyGraph(Set<IOntologyConcept> roots, Map<URI, IOntologyConcept> uriToConcept, IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept,
                    Map<URI, IOntologyProperty> uriToProperty, IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty) {
        this.uriToConcept = uriToConcept;
        this.labelToConcept = labelToConcept;
        this.roots = roots;
        this.uriToProperty = uriToProperty;
        this.labelToProperty = labelToProperty;
    }

    public Map<URI, IOntologyConcept> getUriToConcept() {
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

    public void setUriToConcept(Map<URI, IOntologyConcept> uriToConcept) {
        this.uriToConcept = uriToConcept;
    }

    public IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> getLabelToConcept() {
        return labelToConcept;
    }

    public void setLabelToConcept(IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept) {
        this.labelToConcept = labelToConcept;
    }

    public IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> getSynsetToConcept() {
        return synsetToConcept;
    }

    public void setSynsetToConcept(IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept) {
        this.synsetToConcept = synsetToConcept;
    }

    public Set<IOntologyConcept> getRoots() {
        return roots;
    }

    public void setRoots(Set<IOntologyConcept> roots) {
        this.roots = roots;
    }

    public Map<URI, IOntologyProperty> getUriToProperty() {
        return uriToProperty;
    }

    public void setUriToProperty(Map<URI, IOntologyProperty> uriToProperty) {
        this.uriToProperty = uriToProperty;
    }

    public IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> getLabelToProperty() {
        return labelToProperty;
    }

    public void setLabelToProperty(IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty) {
        this.labelToProperty = labelToProperty;
    }

    public IOntologyConcept getConceptByURI(URI uri) {
        return this.uriToConcept != null ? this.uriToConcept.get(uri) : null;
    }

    public Collection<IOntologyProperty> getProperties() {
        return uriToProperty.values();
    }

    public Collection<IOntologyConcept> getConcepts() {
        return uriToConcept.values();
    }
}
