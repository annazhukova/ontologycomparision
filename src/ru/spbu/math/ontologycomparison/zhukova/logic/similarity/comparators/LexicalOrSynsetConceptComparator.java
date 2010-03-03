package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.SetHelper;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class LexicalOrSynsetConceptComparator extends LexicalConceptComparator {    

    public boolean areSimilar(OntologyConcept first, OntologyConcept second) {
        if (first.getConceptToReason().containsKey(second)) {
            return true;
        }
        Set<Synset> firstSynset = first.getSynsetToReason().keySet();
        Set<Synset> secondSynset = second.getSynsetToReason().keySet();
        if (firstSynset != null && !firstSynset.isEmpty() && secondSynset != null && !secondSynset.isEmpty()) {
            return !SetHelper.INSTANCE.setIntersection(firstSynset, secondSynset).isEmpty();
        }
        return LexicalComparisonHelper.areSimilar(first, second);
    }
}
