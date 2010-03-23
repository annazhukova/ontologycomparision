package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import edu.smu.tspell.wordnet.Synset;
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

    IHashTable<String, IOntologyConcept, Set<IOntologyConcept>> getLabelToConcept();

    IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> getSynsetToConcept();

    void setSynsetToConcept(IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept);

    Set<IOntologyConcept> getRoots();

    Map<URI, IOntologyProperty> getUriToProperty();

    IHashTable<String, IOntologyProperty, Set<IOntologyProperty>> getLabelToProperty();

    IOntologyConcept getConceptByURI(URI uri);

    Collection<IOntologyConcept> getConcepts();

    Collection<IOntologyProperty> getProperties();
}
