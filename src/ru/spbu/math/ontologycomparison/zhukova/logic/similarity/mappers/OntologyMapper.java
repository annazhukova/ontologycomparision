package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.MapStore;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.LexicalOrSynsetConceptComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.SetHelper;
import ru.spbu.math.ontologycomparison.zhukova.util.Triple;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import static ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.BindingReasonConstants.*;

/**
 * @author Anna Zhukova
 */
public class OntologyMapper extends Mapper<OntologyConcept, OntologyConcept, WordNetRelation> {
    private Collection<OntologyConcept> firstConcepts;
    private Collection<OntologyConcept> secondConcepts;
    private final MapStore mapStore;

    public OntologyMapper(Collection<OntologyConcept> firstConcepts, Collection<OntologyConcept> secondConcepts, MapStore mapStore) {
        this.firstConcepts = firstConcepts;
        this.secondConcepts = secondConcepts;
        this.mapStore = mapStore;
    }

    public Collection<OntologyConcept> map() {
        LexicalOrSynsetConceptComparator conceptComparator = new LexicalOrSynsetConceptComparator();

        Set<URI> commonUriSet = SetHelper.INSTANCE.setIntersection(mapStore.getFirstUriToConcept().keySet(), mapStore.getSecondUriToConcept().keySet());
        for (URI uri : commonUriSet) {
            OntologyConcept first = mapStore.getFirstUriToConcept().get(uri);
            OntologyConcept second = mapStore.getSecondUriToConcept().get(uri);
            bind(first, second, SAME_URI);
            secondConcepts.remove(second);
        }
        Set<Synset> commonSynsetSet = SetHelper.INSTANCE.setIntersection(mapStore.getFirstSynsetlToConcept().keySet(), mapStore.getSecondSynsetToConcept().keySet());
        for (Synset synset : commonSynsetSet) {
            for (OntologyConcept first : mapStore.getFirstSynsetlToConcept().get(synset)) {
                for (OntologyConcept second : mapStore.getSecondSynsetToConcept().get(synset)) {
                    bind(first, second, SAME_SYNSET);
                    secondConcepts.remove(second);
                }
            }
        }
        Set<String> commonLabelSet = SetHelper.INSTANCE.setIntersection(mapStore.getFirstLabelToConcept().keySet(), mapStore.getSecondLabelToConcept().keySet());
        for (String label : commonLabelSet) {
            for (OntologyConcept first : mapStore.getFirstLabelToConcept().get(label)) {
                for (OntologyConcept second : mapStore.getSecondLabelToConcept().get(label)) {
                    if (tryToBind(conceptComparator, first, second, getBindFactors())) {
                        secondConcepts.remove(second);
                        break;
                    }
                }
            }
        }
        firstConcepts.addAll(secondConcepts);
        System.out.println("binded ontologies");
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
