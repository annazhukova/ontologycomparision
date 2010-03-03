package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.synset;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.ConceptToSynsetComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.Pair;
import ru.spbu.math.ontologycomparison.zhukova.util.Triple;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Anna Zhukova
 */
public class SynsetMapper {

    private IOntologyGraph graph;

    public SynsetMapper(IOntologyGraph graph) {
        this.graph = graph;
    }

    public void map() {
        ConceptToSynsetComparator conceptToSynsetComparator = new ConceptToSynsetComparator();
        /*System.out.printf("SYNSET HELPER FOR GRAPH: %s\n", graph);*/
        for (OntologyConcept childConcept : this.graph.getConcepts()) {
            for (Synset childSynset : getChildSynsets(childConcept)) {
                tryToBindConceptToSynset(conceptToSynsetComparator, childConcept, childSynset, getBindFactors());
            }
        }
    }

    private ITriple<WordNetRelation, String, String>[] getBindFactors() {
        return new Triple[] {
                new Triple<WordNetRelation, String, String>(WordNetRelation.HYPONYM, "same parents", "same children"),
                new Triple<WordNetRelation, String, String>(WordNetRelation.MERONYM,  "same parts", "same whole")
        };
    }

    private void tryToBindConceptToSynset(ConceptToSynsetComparator conceptToSynsetComparator, OntologyConcept concept, Synset synset, ITriple<WordNetRelation, String, String>... howToBind) {
        for (ITriple<WordNetRelation, String, String> bind : howToBind) {
            tryToBindConceptToSynset(conceptToSynsetComparator, concept, synset, bind.getFirst(), bind.getSecond(), bind.getThird());
        }
    }

    private void tryToBindConceptToSynset(ConceptToSynsetComparator conceptToSynsetComparator, OntologyConcept concept,
                                           Synset synset, WordNetRelation relation, String reasonForOrigin, String reasonForFound) {
        Pair<OntologyConcept, Synset> parentToSynsetPair =
                conceptToSynsetComparator.areSimilar(concept, synset, relation);
        if (parentToSynsetPair != null) {
            bindConceptToSynset(concept, synset, reasonForOrigin);
            bindConceptToSynset(parentToSynsetPair.getFirst(), parentToSynsetPair.getSecond(), reasonForFound);
        }
    }

    private Collection<Synset> getChildSynsets(OntologyConcept childConcept) {
        Collection<Synset> childSynsetCollection = new HashSet<Synset>();
        for (String label : childConcept.getLabels()) {
            childSynsetCollection.addAll(WordNetHelper.getSynsetsForWord(label.toLowerCase()));
        }
        return childSynsetCollection;
    }

    private void bindConceptToSynset(OntologyConcept concept, Synset synset, String reason) {
        /*System.out.printf("\tBINDED %s <-> %s\n", concept, synset);*/
        concept.addSynset(synset, reason);
    }
}
