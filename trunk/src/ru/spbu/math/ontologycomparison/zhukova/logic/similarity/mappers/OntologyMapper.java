package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.LexicalComparisonHelper;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.LexicalOrSynsetConceptComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.SynsetComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.Pair;
import ru.spbu.math.ontologycomparison.zhukova.util.Triple;

import java.util.Collection;
import java.util.HashSet;

import static ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.BindingReasonConstants.*;

/**
 * @author Anna Zhukova
 */
public class OntologyMapper extends Mapper<OntologyConcept, OntologyConcept, WordNetRelation> {
    private Collection<OntologyConcept> firstConcepts;
    private Collection<OntologyConcept> secondConcepts;

    public OntologyMapper(Collection<OntologyConcept> firstConcepts, Collection<OntologyConcept> secondConcepts) {
        this.firstConcepts = firstConcepts;
        this.secondConcepts = secondConcepts;
    }

    public Collection<OntologyConcept> map() {
        LexicalOrSynsetConceptComparator conceptComparator = new LexicalOrSynsetConceptComparator();
        SynsetComparator synsetComparator = new SynsetComparator();
        Collection<OntologyConcept> secondConceptsToRemove = new HashSet<OntologyConcept>();
        for (OntologyConcept first : firstConcepts) {
            /* to use less time use iterator, break after first match found and remove: it.remove() =>
            *  the method will become faster but not all reasons of similarity will be found.*/
            for (OntologyConcept second : secondConcepts) {
                if (first.getUri().equals(second.getUri())) {
                    bind(first, second, SAME_URI);
                    //it.remove();
                    secondConceptsToRemove.add(second);
                    //break;
                }
                Pair<OntologyConcept, OntologyConcept> conceptPair = synsetComparator.areSimilar(first, second, null);
                if (conceptPair != null) {
                    bind(first, second, SAME_SYNSET);
                    //it.remove();
                    secondConceptsToRemove.add(second);
                    //break;
                }
                if (!LexicalComparisonHelper.areSimilar(first, second)) {
                    continue;
                }
                if (tryToBind(conceptComparator, first, second, getBindFactors())) {
                    //it.remove();
                    secondConceptsToRemove.add(second);
                    //break;
                }
            }
        }
        secondConcepts.removeAll(secondConceptsToRemove);
        firstConcepts.addAll(secondConcepts);
        return firstConcepts;
    }

    public ITriple<WordNetRelation, String, String>[] getBindFactors() {
        return new Triple[]{
                new Triple<WordNetRelation, String, String>(WordNetRelation.HYPONYM, SAME_PARENTS, SAME_CHILDREN),
                new Triple<WordNetRelation, String, String>(WordNetRelation.MERONYM, SAME_PARTS, SAME_WHOLE)
        };
    }

    public void bind(OntologyConcept first, OntologyConcept second, String reason) {
        first.addConcept(second, reason);
    }
}
