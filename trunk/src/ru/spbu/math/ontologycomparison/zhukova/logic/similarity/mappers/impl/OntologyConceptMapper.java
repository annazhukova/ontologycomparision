package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl.LexicalOrSynsetConceptComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHelper;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Triple;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import static ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.BindingReasonConstants.*;

/**
 * @author Anna Zhukova
 */
public class OntologyConceptMapper extends Mapper<IOntologyConcept, IOntologyConcept, WordNetRelation> {
    private Collection<IOntologyConcept> firstConcepts;
    private Collection<IOntologyConcept> secondConcepts;
    private final IOntologyGraph firstGraph;
    private final IOntologyGraph secondGraph;

    public OntologyConceptMapper(Collection<IOntologyConcept> firstConcepts, Collection<IOntologyConcept> secondConcepts,
                          IOntologyGraph firstGraph, IOntologyGraph secondGraph) {
        this.firstConcepts = firstConcepts;
        this.secondConcepts = secondConcepts;
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
    }

    public Collection<IOntologyConcept> map() {
        LexicalOrSynsetConceptComparator conceptComparator = new LexicalOrSynsetConceptComparator();

        Set<URI> commonUriSet = SetHelper.INSTANCE.setIntersection(firstGraph.getConceptUris(),
                secondGraph.getConceptUris());
        for (URI uri : commonUriSet) {
            IOntologyConcept first = firstGraph.getUriToConcept().get(uri);
            IOntologyConcept second = secondGraph.getUriToConcept().get(uri);
            bind(first, second, SAME_URI);
        }
        Set<Synset> commonSynsetSet = SetHelper.INSTANCE.setIntersection(firstGraph.getSynsets(), secondGraph.getSynsets());
        for (Synset synset : commonSynsetSet) {
            for (IOntologyConcept first : firstGraph.getSynsetToConcept().get(synset)) {
                for (IOntologyConcept second : secondGraph.getSynsetToConcept().get(synset)) {
                    bind(first, second, SAME_SYNSET);
                }
            }
        }
        Set<String> commonLabelSet = SetHelper.INSTANCE.setIntersection(firstGraph.getConceptLabels(),
                secondGraph.getConceptLabels());
        for (String label : commonLabelSet) {
            for (IOntologyConcept first : firstGraph.getLabelToConcept().get(label)) {
                for (IOntologyConcept second : secondGraph.getLabelToConcept().get(label)) {
                    if (tryToBind(conceptComparator, first, second, getBindFactors())) {
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

    public void bind(IOntologyConcept first, IOntologyConcept second, String reason, int count) {
        first.addConcept(second, reason, count);
        secondConcepts.remove(second);
    }

    public void bind(IOntologyConcept first, IOntologyConcept second, String reason) {
        bind(first, second, reason, 1);
    }
}
