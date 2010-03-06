package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IMapStore;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyConcept;
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
    private final IMapStore firstMapStore;
    private final IMapStore secondMapStore;

    public OntologyMapper(Collection<OntologyConcept> firstConcepts, Collection<OntologyConcept> secondConcepts,
                          IMapStore firstMapStore, IMapStore secondMapStore) {
        this.firstConcepts = firstConcepts;
        this.secondConcepts = secondConcepts;
        this.firstMapStore = firstMapStore;
        this.secondMapStore = secondMapStore;
    }

    public Collection<OntologyConcept> map() {
        LexicalOrSynsetConceptComparator conceptComparator = new LexicalOrSynsetConceptComparator();

        Set<URI> commonUriSet = SetHelper.INSTANCE.setIntersection(firstMapStore.getConceptUris(),
                secondMapStore.getConceptUris());
        for (URI uri : commonUriSet) {
            OntologyConcept first = firstMapStore.getUriToConcept().get(uri);
            OntologyConcept second = secondMapStore.getUriToConcept().get(uri);
            bind(first, second, SAME_URI);
            secondConcepts.remove(second);
        }
        Set<Synset> commonSynsetSet = SetHelper.INSTANCE.setIntersection(firstMapStore.getSynsets(), secondMapStore.getSynsets());
        for (Synset synset : commonSynsetSet) {
            for (OntologyConcept first : firstMapStore.getSynsetToConcept().get(synset)) {
                for (OntologyConcept second : secondMapStore.getSynsetToConcept().get(synset)) {
                    bind(first, second, SAME_SYNSET);
                    secondConcepts.remove(second);
                }
            }
        }
        Set<String> commonLabelSet = SetHelper.INSTANCE.setIntersection(firstMapStore.getConceptLabels(),
                secondMapStore.getConceptLabels());
        for (String label : commonLabelSet) {
            for (OntologyConcept first : firstMapStore.getLabelToConcept().get(label)) {
                for (OntologyConcept second : secondMapStore.getLabelToConcept().get(label)) {
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
