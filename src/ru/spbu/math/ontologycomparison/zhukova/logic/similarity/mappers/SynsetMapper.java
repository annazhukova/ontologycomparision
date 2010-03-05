package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.ConceptToSynsetComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.HashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.Triple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.BindingReasonConstants.*;

/**
 * @author Anna Zhukova
 */
public class SynsetMapper extends Mapper<OntologyConcept, Synset, WordNetRelation> {
    private final Collection<OntologyConcept> conceptCollection;
    private final IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> synsetToConcept = new HashTable<Synset, OntologyConcept, Set<OntologyConcept>>() {
        @Override
        public Set<OntologyConcept> newCollection() {
            return new HashSet<OntologyConcept>();
        }
    };

    public SynsetMapper(Collection<OntologyConcept> conceptCollection) {
        this.conceptCollection = conceptCollection;
    }

    public Collection<OntologyConcept> map() {
        ConceptToSynsetComparator conceptToSynsetComparator = new ConceptToSynsetComparator();
        /*System.out.printf("SYNSET HELPER FOR GRAPH: %s\n", graph);*/
        for (OntologyConcept childConcept : conceptCollection) {
            for (Synset childSynset : getSynsets(childConcept)) {
                tryToBind(conceptToSynsetComparator, childConcept, childSynset, getBindFactors());
            }
        }
        System.out.println("binded synsets");
        return conceptCollection;
    }

    public ITriple<WordNetRelation, String, String>[] getBindFactors() {
        return new Triple[]{
                new Triple<WordNetRelation, String, String>(WordNetRelation.HYPONYM, SAME_PARENTS, SAME_CHILDREN),
                new Triple<WordNetRelation, String, String>(WordNetRelation.MERONYM, SAME_PARTS, SAME_WHOLE)
        };
    }

    private Collection<Synset> getSynsets(OntologyConcept concept) {
        Collection<Synset> result = new HashSet<Synset>();
        for (String label : concept.getLabels()) {
            result.addAll(WordNetHelper.getSynsetsForWord(label.toLowerCase()));
        }
        return result;
    }

    public void bind(OntologyConcept concept, Synset synset, String reason) {
        /*System.out.printf("\tBINDED %s <-> %s\n", concept, synset);*/
        concept.addSynset(synset, reason);
        synsetToConcept.insert(synset, concept);
    }

    public IHashTable<Synset, OntologyConcept, Set<OntologyConcept>> getSynsetToConceptTable() {
        return synsetToConcept;
    }
}
