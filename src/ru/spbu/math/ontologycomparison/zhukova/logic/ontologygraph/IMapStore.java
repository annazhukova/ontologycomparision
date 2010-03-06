package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyProperty;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IMapStore {
    Map<URI, OntologyConcept> getUriToConcept();

    Set<URI> getConceptUris();

    Set<URI> getPropertyUris();

    Set<String> getConceptLabels();

    Set<String> getPropertyLabels();

    Set<Synset> getSynsets();

    void setUriToConcept(Map<URI, OntologyConcept> uriToConcept);

    IHashTable<String, OntologyConcept, Set<OntologyConcept>> getLabelToConcept();

    void setLabelToConcept(IHashTable<String, OntologyConcept, Set<OntologyConcept>> labelToConcept);

    IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> getSynsetToConcept();

    void setSynsetToConcept(IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> synsetToConcept);

    Set<OntologyConcept> getRoots();

    void setRoots(Set<OntologyConcept> roots);

    Map<URI, OntologyProperty> getUriToProperty();

    void setUriToProperty(Map<URI, OntologyProperty> uriToProperty);

    IHashTable<String, OntologyProperty, Set<OntologyProperty>> getLabelToProperty();

    void setLabelToProperty(IHashTable<String, OntologyProperty, Set<OntologyProperty>> labelToProperty);

    OntologyConcept getConceptByURI(URI uri);

    Collection<OntologyConcept> getConcepts();
}
