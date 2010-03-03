package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class ConceptToSynsetComparator extends Comparator<OntologyConcept, Synset, WordNetRelation> {
    @Override
    public Set<OntologyConcept> getByFirstProperty(OntologyConcept concept, WordNetRelation property) {
        return LexicalComparisonHelper.getConceptSetByConceptAndProperty(concept, property);
    }

    @Override
    public Set<Synset> getBySecondProperty(Synset concept, WordNetRelation property) {
        switch (property) {
            case HYPONYM:
                return new LinkedHashSet<Synset>(WordNetHelper.getHypernymsForSynset(concept));
            case HYPERNYM:
                return new LinkedHashSet<Synset>(WordNetHelper.getHyponymsForSynset(concept));
            case HOLONYM:
                return Collections.emptySet();
            case MERONYM:
                return new LinkedHashSet<Synset>(WordNetHelper.getPartHolonymsForSynset(concept));
        }
        return Collections.emptySet();
    }

    @Override
    public boolean areSimilar(OntologyConcept first, Synset second) {
        for (String label : first.getLabels()) {
            if (WordNetHelper.getSynsetsForWord(LexicalComparisonHelper.normalizeString(label)).contains(second)) {
                return true;
            }
        }
        return false;
    }
}
