package ru.spbu.math.ontologycomparision.zhukova.logic.similarity;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.EmptySynset;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.SynsetHelper;
import ru.spbu.math.ontologycomparision.zhukova.util.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class OntologyComparator<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {
    private SynsetHelper<C, R> firstSynsetHelper;
    private SynsetHelper<C, R> secondSynsetHelper;

    public OntologyComparator(IOntologyGraph<C, R> firstGraph, IOntologyGraph<C, R> secondGraph) {
        this.firstSynsetHelper = new SynsetHelper<C, R>(firstGraph);
        this.secondSynsetHelper = new SynsetHelper<C, R>(secondGraph);
    }

    public double getSimilarity() {
        Set<Synset> firstSynsets = this.firstSynsetHelper.getSynsets();
        Set<Synset> secondSynsets = this.secondSynsetHelper.getSynsets();
        int firstNotSecondSynsetCount = SetHelper.INSTANCE.setIntersection(firstSynsets, secondSynsets).size();
        int firstAndSecondSynsetCount = SetHelper.INSTANCE.setUnion(firstSynsets, secondSynsets).size();
        IPair<IHashTable<Synset, C>, Integer> mergedNoSynsetConcepts = this.mergeNoSynsetConcepts();
        return ((double) firstNotSecondSynsetCount + mergedNoSynsetConcepts.getSecond())
                / (firstAndSecondSynsetCount + this.firstSynsetHelper.getConcepsWithNoSynset().size() +
        this.secondSynsetHelper.getConcepsWithNoSynset().size());
    }

    public IHashTable<Synset, C> merge() {
        IHashTable<Synset, C> firstSynsetToConcept = this.firstSynsetHelper.getSynsetToConceptTable();
        IHashTable<Synset, C> secondSynsetToConcept = this.secondSynsetHelper.getSynsetToConceptTable();
        IHashTable<Synset, C> result = new HashTable<Synset, C>(firstSynsetToConcept);
        result.insertAll(secondSynsetToConcept);
        result.insertAll(mergeNoSynsetConcepts().getFirst());
        return result;
    }

    public IHashTable<String, C> noSynsetConceptUnion() {
        IHashTable<String, C> result = new HashTable<String, C>(this.firstSynsetHelper.getConcepsWithNoSynset());
        result.insertAll(this.secondSynsetHelper.getConcepsWithNoSynset());
        return result;
    }

    public IPair<IHashTable<Synset, C>, Integer> mergeNoSynsetConcepts() {
        int merged = 0;
        Map<C, Synset> firstConceptToSynsetMap = this.firstSynsetHelper.getConceptToSynsetMap();
        Map<C, Synset> secondConceptToSynsetMap = this.secondSynsetHelper.getConceptToSynsetMap();
        IHashTable<String, C> allNoSynsetConcets = this.noSynsetConceptUnion();
        IHashTable<Synset, C> result = new HashTable<Synset, C>();
        for (Map.Entry<String, Set<C>> entry : allNoSynsetConcets.entrySet()) {
            Set<C> values = entry.getValue();
            if (values.size() == 1) {
                result.insert(new EmptySynset(), values.iterator().next());
            } else {
                Iterator<C> it = values.iterator();
                C first = it.next();
                C second = it.next();
                Set<Synset> firstParentSynsets = getParentSynsets(firstConceptToSynsetMap, first);
                Set<Synset> secondParentSynsets = getParentSynsets(secondConceptToSynsetMap, second);
                if (SetHelper.INSTANCE.setIntersection(firstParentSynsets, secondParentSynsets).size() > 0) {
                    Synset empty = new EmptySynset();
                    result.insert(empty, first);
                    result.insert(empty, second);
                    merged++;
                } else {
                    result.insert(new EmptySynset(), first);
                    result.insert(new EmptySynset(), second);
                }
            }
        }
        return new Pair<IHashTable<Synset,C>, Integer>(result, merged);
    }

    private Set<Synset> getParentSynsets(Map<C, Synset> conceptToSynsetMap, C concept) {
        Set<Synset> result = new HashSet<Synset>();
        for (C parent : concept.getAllParents()) {
            Synset synset = conceptToSynsetMap.get(parent);
            if (synset != null) {
                result.add(synset);
            }
        }
        return result;
    }

}
