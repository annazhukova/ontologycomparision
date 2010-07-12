package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class ConceptToSynsetComparator extends Comparator<IOntologyConcept, Synset, WordNetRelation> {
    public Set<IOntologyConcept> getFirstByProperty(IOntologyConcept concept, WordNetRelation property) {
        if (property.equals(WordNetRelation.HYPONYM)) {
            return concept.getParentsRecursively();
        }
        return LexicalComparisonHelper.getConceptSetByConceptAndProperty(concept, property);
    }

    public Set<Synset> getSecondByProperty(Synset synset, WordNetRelation property) {
        switch (property) {
            case HYPONYM:
                return new LinkedHashSet<Synset>(WordNetHelper.getHypernymsForSynsetRecursively(synset));
            case HYPERNYM:
                return new LinkedHashSet<Synset>(WordNetHelper.getHyponymsForSynsetRecursively(synset));
            case HOLONYM:
                return Collections.emptySet();
            case MERONYM:
                return new LinkedHashSet<Synset>(WordNetHelper.getPartHolonymsForSynset(synset));
        }
        return Collections.emptySet();
    }

    public boolean areSimilar(IOntologyConcept first, Synset second) {
        for (String label : first.getLabels()) {
            if (WordNetHelper.getSynsetsForWord(LexicalComparisonHelper.normalizeString(label)).contains(second)) {
                return true;
            }
        }
        return false;
    }
}
