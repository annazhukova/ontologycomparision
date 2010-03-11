package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl.ConceptToSynsetComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHashTable;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Triple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.BindingReasonConstants.*;

/**
 * @author Anna Zhukova
 */
public class SynsetMapper extends Mapper<IOntologyConcept, Synset, WordNetRelation> {
    private final Collection<IOntologyConcept> conceptCollection;
    private final IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> synsetToConcept = new SetHashTable<Synset, IOntologyConcept>();

    public SynsetMapper(Collection<IOntologyConcept> conceptCollection) {
        this.conceptCollection = conceptCollection;
    }

    public Collection<IOntologyConcept> map() {
        ConceptToSynsetComparator conceptToSynsetComparator = new ConceptToSynsetComparator();
        /*System.out.printf("SYNSET HELPER FOR GRAPH: %s\n", graph);*/
        for (IOntologyConcept concept : conceptCollection) {
            Collection<Synset> synsetCollection = getSynsets(concept);
            for (Synset synset : synsetCollection) {
                if (tryToBind(conceptToSynsetComparator, concept, synset, getBindFactors())) {
                    break;
                }
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

    private Collection<Synset> getSynsets(IOntologyConcept concept) {
        Collection<Synset> result = new HashSet<Synset>();
        for (String label : concept.getLabels()) {
            result.addAll(WordNetHelper.getSynsetsForWord(label));
        }
        return result;
    }

    public void bind(IOntologyConcept concept, Synset synset, String reason, int count) {
        /*System.out.printf("\tBINDED %s <-> %s\n", concept, synset);*/
        concept.addSynset(synset, reason, count);
        synsetToConcept.insert(synset, concept);
    }

    public void bind(IOntologyConcept first, Synset synset, String reason) {
        bind(first, synset, reason, 1);
    }

    public IHashTable<Synset, IOntologyConcept, Set<IOntologyConcept>> getSynsetToConceptTable() {
        return synsetToConcept;
    }
}
