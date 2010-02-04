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

    public SynsetHelper (IOntologyGraph<C, R> graph) {
        for (C parentConcept : graph.getConcepts()) {
            Collection<? extends Synset> parentSynsetCollection =
                    WordNetHelper.getSynsetsForWord(parentConcept.getLabel().toLowerCase());
            boolean noCorrectParentSynsetsFound = true;
            boolean noCorrectChildSynsetsFound = false;
            for (R superClassRelation : parentConcept.getSubjectRelations(WordNetRelation.HYPERNYM.getRelatedOntologyConcept())) {
                C childConcept = superClassRelation.getObject();
                Collection<? extends Synset> childSynsetCollection =
                        WordNetHelper.getSynsetsForWord(childConcept.getLabel().toLowerCase());
                noCorrectChildSynsetsFound = true;
                for (Synset childSynset : childSynsetCollection) {
                    for (Synset parentSynset : parentSynsetCollection) {
                        if (WordNetHelper.getHypernymsForSynset(childSynset).contains(parentSynset)) {
                            noCorrectChildSynsetsFound = false;
                            noCorrectParentSynsetsFound = false;
                            this.getConceptToSynsetMap().put(parentConcept, parentSynset);
                            this.getConceptToSynsetMap().put(childConcept, childSynset);
                            this.getSynsetToConceptTable().insert(parentSynset, parentConcept);
                            this.getSynsetToConceptTable().insert(childSynset, childConcept);
                        }
                    }
                }
                if (noCorrectChildSynsetsFound) {
                    this.getConcepsWithNoSynset().insert(childConcept.getLabel(), childConcept);
                }
            }
            if (noCorrectParentSynsetsFound && noCorrectChildSynsetsFound) {
                this.getConcepsWithNoSynset().insert(parentConcept.getLabel(), parentConcept);
            }
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
