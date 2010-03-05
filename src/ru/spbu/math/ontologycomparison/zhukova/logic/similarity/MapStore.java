package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;

import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class MapStore {
    private Map<URI, OntologyConcept> firstUriToConcept;
    private Map<URI, OntologyConcept> secondUriToConcept;

    private IHashTable<String, OntologyConcept, Set<OntologyConcept>> firstLabelToConcept;
    private IHashTable<String, OntologyConcept, Set<OntologyConcept>> secondLabelToConcept;

    private IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> firstSynsetlToConcept;
    private IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> secondSynsetToConcept;

    public Map<URI, OntologyConcept> getFirstUriToConcept() {
        return firstUriToConcept;
    }

    public void setFirstUriToConcept(Map<URI, OntologyConcept> firstUriToConcept) {
        this.firstUriToConcept = firstUriToConcept;
    }

    public Map<URI, OntologyConcept> getSecondUriToConcept() {
        return secondUriToConcept;
    }

    public void setSecondUriToConcept(Map<URI, OntologyConcept> secondUriToConcept) {
        this.secondUriToConcept = secondUriToConcept;
    }

    public IHashTable<String, OntologyConcept, Set<OntologyConcept>> getFirstLabelToConcept() {
        return firstLabelToConcept;
    }

    public void setFirstLabelToConcept(IHashTable<String, OntologyConcept, Set<OntologyConcept>> firstLabelToConcept) {
        this.firstLabelToConcept = firstLabelToConcept;
    }

    public IHashTable<String, OntologyConcept, Set<OntologyConcept>> getSecondLabelToConcept() {
        return secondLabelToConcept;
    }

    public void setSecondLabelToConcept(IHashTable<String, OntologyConcept, Set<OntologyConcept>> secondLabelToConcept) {
        this.secondLabelToConcept = secondLabelToConcept;
    }

    public IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> getFirstSynsetlToConcept() {
        return firstSynsetlToConcept;
    }

    public void setFirstSynsetlToConcept(IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> firstSynsetlToConcept) {
        this.firstSynsetlToConcept = firstSynsetlToConcept;
    }

    public IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> getSecondSynsetToConcept() {
        return secondSynsetToConcept;
    }

    public void setSecondSynsetToConcept(IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> secondSynsetToConcept) {
        this.secondSynsetToConcept = secondSynsetToConcept;
    }
}
