package ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparision.zhukova.util.HashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.UnmodifiableHashTable;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class SynsetHelper<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {

    private IHashTable<String[], C> concepsWithNoSynset = new HashTable<String[], C>();
    private IHashTable<Synset, C> synsetToConceptTable = new HashTable<Synset, C>();
    private IHashTable<C, Synset> conceptToSynsetTable = new HashTable<C, Synset>();

    public SynsetHelper(IOntologyGraph<C, R> graph) {
        /*System.out.printf("SYNSET HELPER FOR GRAPH: %s\n", graph);*/
        markAllConceptsAsHavingNoSynsets(graph);
        for (C childConcept : graph.getConcepts()) {
            Collection<Synset> childSynsetCollection = new HashSet<Synset>();
            for (String label : childConcept.getLabels()) {
                childSynsetCollection.addAll(WordNetHelper.getSynsetsForWord(label.toLowerCase()));
            }
            /*System.out.printf("\tCHILD: %s %s\n", childConcept, childSynsetCollection);*/
            if (childSynsetCollection.isEmpty()) {
                continue;
            }
            for (Synset childSynset : childSynsetCollection) {
                Collection<? extends Synset> partSynsetCollection = WordNetHelper.getPartHolonymsForSynset(childSynset);
                for (R relation : childConcept.getSubjectRelations(WordNetRelation.MERONYM.getRelatedOntologyConcept())) {
                    C part = relation.getObject();
                    Collection<Synset> partSynsets = new HashSet<Synset>();
                    for (String label : part.getLabels()) {
                        partSynsets.addAll(WordNetHelper.getSynsetsForWord(label.toLowerCase()));
                    }
                    for (Synset partSynset : partSynsets) {
                        if (partSynsetCollection.contains(partSynset)) {
                            bindConceptToSynset(part, partSynset);
                            bindConceptToSynset(childConcept, childSynset);
                        }
                    }
                }
            }
            for (C parentConcept : childConcept.getAllParents()) {
                Collection<Synset> parentSynsetCollection = new HashSet<Synset>();
                for (String label : parentConcept.getLabels()) {
                    parentSynsetCollection.addAll(WordNetHelper.getSynsetsForWord(label.toLowerCase()));
                }
                /*System.out.printf("\tPARENT: %s %s\n", parentConcept, parentSynsetCollection);*/
                if (parentSynsetCollection.isEmpty()) {
                    continue;
                }
                for (Synset childSynset : childSynsetCollection) {
                    Collection<? extends Synset> childHypernyms = WordNetHelper.getHypernymsForSynset(childSynset);
                    /*System.out.printf("\tCHILD SYNSET: %s  HYPERNYMS: %s\n", childSynset, childHypernyms);*/
                    for (Synset parentSynset : parentSynsetCollection) {
                        if (childHypernyms.contains(parentSynset)) {
                            bindConceptToSynset(childConcept, childSynset);
                            bindConceptToSynset(parentConcept, parentSynset);
                        }
                    }
                }
            }

        }
    }

    private void markAllConceptsAsHavingNoSynsets(IOntologyGraph<C, R> graph) {
        for (C childConcept : graph.getConcepts()) {
            this.concepsWithNoSynset.insert(childConcept.getLabels(), childConcept);
        }
    }

    private void bindConceptToSynset(C concept, Synset synset) {
        /*System.out.printf("\tBINDED %s <-> %s\n", concept, synset);*/
        this.conceptToSynsetTable.insert(concept, synset);
        this.synsetToConceptTable.insert(synset, concept);
        if (concepsWithNoSynset.deleteValue(concept.getLabels(), concept)) {
            /*System.out.printf("\t\tFOUND SYNSETS FOR %s\n", concept);*/
        }
    }

    public IHashTable<String[], C> getConcepsWithNoSynset() {
        return new UnmodifiableHashTable<String[], C>(concepsWithNoSynset);
    }

    public IHashTable<Synset, C> getSynsetToConceptTable() {
        return new UnmodifiableHashTable<Synset, C>(synsetToConceptTable);
    }

    public IHashTable<C, Synset> getConceptToSynsetTable() {
        return new UnmodifiableHashTable<C, Synset>(conceptToSynsetTable);
    }

    public Set<Synset> getSynsets() {
        return Collections.unmodifiableSet(synsetToConceptTable.keySet());
    }
}
