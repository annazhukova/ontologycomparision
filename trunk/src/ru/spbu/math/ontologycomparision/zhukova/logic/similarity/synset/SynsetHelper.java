package ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparision.zhukova.util.HashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.UnmodifiableHashTable;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class SynsetHelper<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {

    private IHashTable<String, C> concepsWithNoSynset = new HashTable<String, C>();
    private IHashTable<Synset, C> synsetToConceptTable = new HashTable<Synset, C>();
    private Map<C, Synset> conceptToSynsetMap = new HashMap<C, Synset>();

    public SynsetHelper(IOntologyGraph<C, R> graph) {
        System.out.printf("SYNSET HELPER FOR GRAPH: %s\n", graph);
        markAllConceptsAsHavingNoSynsets(graph);
        for (C childConcept : graph.getConcepts()) {
            Collection<? extends Synset> childSynsetCollection =
                    WordNetHelper.getSynsetsForWord(childConcept.getLabel().toLowerCase());
            System.out.printf("\tCHILD: %s %s\n", childConcept, childSynsetCollection);
            if (childSynsetCollection.isEmpty()) {
                continue;
            }
            for (C parentConcept : childConcept.getAllParents()) {
                Collection<? extends Synset> parentSynsetCollection =
                        WordNetHelper.getSynsetsForWord(parentConcept.getLabel().toLowerCase());
                System.out.printf("\tPARENT: %s %s\n", parentConcept, parentSynsetCollection);
                if (parentSynsetCollection.isEmpty()) {
                    continue;
                }
                for (Synset childSynset : childSynsetCollection) {
                    Collection<? extends Synset> childHypernyms =
                            WordNetHelper.getHypernymsForSynset(childSynset);
                    System.out.printf("\tCHILD SYNSET: %s  HYPERNYMS: %s\n", childSynset, childHypernyms);
                    for (Synset parentSynset : parentSynsetCollection) {
                        if (childHypernyms.contains(parentSynset)) {
                            bindConceptToSynset(childConcept, childSynset);
                            bindConceptToSynset(parentConcept, parentSynset);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void markAllConceptsAsHavingNoSynsets(IOntologyGraph<C, R> graph) {
        for (C childConcept : graph.getConcepts()) {
            this.concepsWithNoSynset.insert(childConcept.getLabel(), childConcept);
        }
    }

    private void bindConceptToSynset(C concept, Synset synset) {
        if (!this.conceptToSynsetMap.containsKey(concept)) {
            System.out.printf("\tBINDED %s <-> %s\n", concept, synset);
            this.conceptToSynsetMap.put(concept, synset);
            this.synsetToConceptTable.insert(synset, concept);
        }
        if (concepsWithNoSynset.deleteValue(concept.getLabel(), concept)) {
            System.out.printf("\t\tFOUND SYNSETS FOR %s\n", concept);
        }
    }

    public IHashTable<String, C> getConcepsWithNoSynset() {
        return new UnmodifiableHashTable<String, C>(concepsWithNoSynset);
    }

    public IHashTable<Synset, C> getSynsetToConceptTable() {
        return new UnmodifiableHashTable<Synset, C>(synsetToConceptTable);
    }

    public Map<C, Synset> getConceptToSynsetMap() {
        return Collections.unmodifiableMap(conceptToSynsetMap);
    }

    public Set<Synset> getSynsets() {
        return Collections.unmodifiableSet(synsetToConceptTable.keySet());
    }
}
