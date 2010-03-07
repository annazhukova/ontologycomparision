package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Pair;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHelper;

import java.util.Collections;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class SynsetComparator extends SameClassComparator<IOntologyConcept, Object> {

    @Override
    public Pair<IOntologyConcept, IOntologyConcept> areSimilar(IOntologyConcept first, IOntologyConcept second, Object property) {
        if (areSimilar(first, second)) {
            return new Pair<IOntologyConcept, IOntologyConcept>(first, second);
        }
        return null;
    }

    public Set<IOntologyConcept> getByProperty(IOntologyConcept concept, Object property) {
        return Collections.emptySet();
    }

    public boolean areSimilar(IOntologyConcept first, IOntologyConcept second) {
        Set<Synset> firstSynset = first.getSynsetToReason().keySet();
        Set<Synset> secondSynset = second.getSynsetToReason().keySet();
        if (firstSynset != null && !firstSynset.isEmpty() && secondSynset != null && !secondSynset.isEmpty()) {
            return !SetHelper.INSTANCE.setIntersection(firstSynset, secondSynset).isEmpty();
        }
        return false;
    }
}
