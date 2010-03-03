package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.util.Pair;
import ru.spbu.math.ontologycomparison.zhukova.util.SetHelper;

import java.util.Collections;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public class SynsetComparator extends SameClassComparator<OntologyConcept, Object> {

    @Override
    public Pair<OntologyConcept, OntologyConcept> areSimilar(OntologyConcept first, OntologyConcept second, Object property) {
        if (areSimilar(first, second)) {
            return new Pair<OntologyConcept, OntologyConcept>(first, second);
        }
        return null;
    }

    @Override
    public Set<OntologyConcept> getByProperty(OntologyConcept concept, Object property) {
        return Collections.emptySet();
    }

    public boolean areSimilar(OntologyConcept first, OntologyConcept second) {
        Set<Synset> firstSynset = first.getSynsetToReason().keySet();
        Set<Synset> secondSynset = second.getSynsetToReason().keySet();
        if (firstSynset != null && !firstSynset.isEmpty() && secondSynset != null && !secondSynset.isEmpty()) {
            return !SetHelper.INSTANCE.setIntersection(firstSynset, secondSynset).isEmpty();
        }
        return false;
    }
}
