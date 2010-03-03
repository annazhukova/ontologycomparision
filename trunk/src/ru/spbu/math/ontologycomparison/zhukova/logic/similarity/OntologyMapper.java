package ru.spbu.math.ontologycomparison.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.LexicalComparisonHelper;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.LexicalOrSynsetConceptComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.SameClassComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.SynsetComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.synset.SynsetMapper;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.Pair;
import ru.spbu.math.ontologycomparison.zhukova.util.Triple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Anna Zhukova
 */
public class OntologyMapper {
    private IOntologyGraph firstGraph;
    private IOntologyGraph secondGraph;
    private Integer intersectionSize;
    private Integer unionSize;

    public OntologyMapper(IOntologyGraph firstGraph, IOntologyGraph secondGraph) {
        (new SynsetMapper(firstGraph)).map();
        (new SynsetMapper(secondGraph)).map();
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
    }

    public double getSimilarity() {
        if (this.intersectionSize == null) {
            this.map();
        }
        return intersectionSize / (double)unionSize;
    }

    public Collection<OntologyConcept> map() {
        LexicalOrSynsetConceptComparator conceptComparator = new LexicalOrSynsetConceptComparator();
        SynsetComparator synsetComparator = new SynsetComparator();
        Collection<OntologyConcept> firstConcepts = new HashSet<OntologyConcept>(firstGraph.getConcepts());
        Collection<OntologyConcept> secondConcepts = new HashSet<OntologyConcept>(secondGraph.getConcepts());
        for (OntologyConcept first : firstConcepts) {
            for (Iterator<OntologyConcept> it = secondConcepts.iterator(); it.hasNext(); ) {
                OntologyConcept second = it.next();
                Pair<OntologyConcept, OntologyConcept> conceptPair = synsetComparator.areSimilar(first, second, null);
                if (conceptPair != null) {
                    bindConcepts(first, second, "same synset");
                    it.remove();
                    break;
                }
                if (!LexicalComparisonHelper.areSimilar(first, second)) {
                    continue;
                }
                if (tryToBindConceptToConcept(conceptComparator, first, second, getBindFactors())) {
                    it.remove();
                    break;
                }
            }
        }
        this.intersectionSize = secondGraph.getConcepts().size() - secondConcepts.size();
        firstConcepts.addAll(secondConcepts);
        this.unionSize = firstConcepts.size();
        return firstConcepts;
    }

    private boolean tryToBindConceptToConcept(SameClassComparator<OntologyConcept, WordNetRelation> conceptComparator, OntologyConcept first, OntologyConcept second, ITriple<WordNetRelation, String, String>... bindFactors) {
        for (ITriple<WordNetRelation, String, String> bind : bindFactors) {
            if (tryToBindConceptToConcept(conceptComparator, first, second, bind.getFirst(), bind.getSecond(), bind.getThird())) {
                return true;
            }
        }
        return false;
    }

    private boolean tryToBindConceptToConcept(SameClassComparator<OntologyConcept, WordNetRelation> conceptComparator, OntologyConcept first, OntologyConcept second, WordNetRelation relation, String reasonForOrigin, String reasonForFound) {
        Pair<OntologyConcept, OntologyConcept> conceptPair = conceptComparator.areSimilar(first, second, relation);
        if (conceptPair != null) {
            bindConcepts(first, second, reasonForOrigin);
            bindConcepts(conceptPair.getFirst(), conceptPair.getSecond(), reasonForFound);
            return true;
        }
        return false;
    }

    private ITriple<WordNetRelation, String, String>[] getBindFactors() {
        return new Triple[] {
                new Triple<WordNetRelation, String, String>(WordNetRelation.HYPONYM, "same parents", "same children"),
                new Triple<WordNetRelation, String, String>(WordNetRelation.MERONYM,  "same parts", "same whole")
        };
    }

    private void bindConcepts(OntologyConcept first, OntologyConcept second, String reason) {
        first.addConcept(second, reason);
    }
}
