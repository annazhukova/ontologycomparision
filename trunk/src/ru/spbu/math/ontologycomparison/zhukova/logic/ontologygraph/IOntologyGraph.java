package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IOntologyGraph {
    Map<URI, IOntologyConcept> getUriToConcept();

    Set<URI> getConceptUris();

    Set<URI> getPropertyUris();

    Set<String> getConceptLabels();

    Set<String> getPropertyLabels();

    Set<Synset> getSynsets();

    void setUriToConcept(Map<URI, IOntologyConcept> uriToConcept);

    IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> getLabelToConcept();

    void setLabelToConcept(IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> labelToConcept);

    IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> getSynsetToConcept();

    void setSynsetToConcept(IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept);

    Set<IOntologyConcept> getRoots();

    void setRoots(Set<IOntologyConcept> roots);

    Map<URI, IOntologyProperty> getUriToProperty();

    void setUriToProperty(Map<URI, IOntologyProperty> uriToProperty);

    IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> getLabelToProperty();

    void setLabelToProperty(IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> labelToProperty);

    IOntologyConcept getConceptByURI(URI uri);

    Collection<IOntologyConcept> getConcepts();

    Collection<IOntologyProperty> getProperties();
}
