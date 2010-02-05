package ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparision.zhukova.util.HashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.IHashTable;

import java.util.*;

import edu.smu.tspell.wordnet.Synset;

/**
 * @author Anna Zhukova
 */
public class SynsetHelper<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {

    private IHashTable<String, C> concepsWithNoSynset = new HashTable<String, C>();
    private IHashTable<Synset, C> synsetToConceptTable = new HashTable<Synset, C>();
    private Map<C, Synset> conceptToSynsetMap = new HashMap<C, Synset>();

    public SynsetHelper(IOntologyGraph<C, R> graph) {
        System.out.println("Graph: " + graph);
        for (C parentConcept : graph.getConcepts()) {
            System.out.println("\tparent concept: " + parentConcept);
            Collection<? extends Synset> parentSynsetCollection =
                    WordNetHelper.getSynsetsForWord(parentConcept.getLabel().toLowerCase());
            System.out.println("\t\tsynsets: " + parentSynsetCollection);
            boolean noParentToSynsetBinding = true;
            boolean noChildToSynsetBinding = false;
            for (R superClassRelation : parentConcept.getSubjectRelations(WordNetRelation.HYPERNYM.getRelatedOntologyConcept())) {
                C childConcept = superClassRelation.getObject();
                System.out.println("\t\tchild concept: " + childConcept);
                Collection<? extends Synset> childSynsetCollection =
                        WordNetHelper.getSynsetsForWord(childConcept.getLabel().toLowerCase());
                noChildToSynsetBinding = true;
                for (Synset childSynset : childSynsetCollection) {
                    for (Synset parentSynset : parentSynsetCollection) {
                        if (WordNetHelper.getHypernymsForSynset(childSynset).contains(parentSynset)) {
                            bindConceptToSynset(parentConcept, parentSynset);
                            bindConceptToSynset(childConcept, childSynset);
                            noChildToSynsetBinding = false;
                            noParentToSynsetBinding = false;
                        }
                    }
                }
                if (noChildToSynsetBinding) {
                    this.getConcepsWithNoSynset().insert(childConcept.getLabel(), childConcept);
                    System.out.println("\t\t\tno child");
                }
            }
            /* if (noParentToSynsetBinding == true && noChildToSynsetBinding == false)
               it means parent has no children at all.
               We don't mark it as no synset concept as
               synsets for it can be found when regarding it as a child.
            */
            if (noParentToSynsetBinding && noChildToSynsetBinding) {
                System.out.println("\t\t\tno parent");
                this.getConcepsWithNoSynset().insert(parentConcept.getLabel(), parentConcept);
            }
        }
    }

    private void bindConceptToSynset(C concept, Synset synset) {
        if (!this.getConceptToSynsetMap().containsKey(concept)) {
            this.getConceptToSynsetMap().put(concept, synset);
            this.getSynsetToConceptTable().insert(synset, concept);
        }
        if (this.getConcepsWithNoSynset().containsKey(concept.getLabel())) {
            getConcepsWithNoSynset().deleteValue(concept.getLabel(), concept);
        }
    }

    public IHashTable<String, C> getConcepsWithNoSynset() {
        return concepsWithNoSynset;
    }

    public IHashTable<Synset, C> getSynsetToConceptTable() {
        return synsetToConceptTable;
    }

    public Map<C, Synset> getConceptToSynsetMap() {
        return conceptToSynsetMap;
    }

    public Set<Synset> getSynsets() {
        return synsetToConceptTable.keySet();
    }
}
