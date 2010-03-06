package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
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
    private final IOntologyGraph firstGraph;
    private final IOntologyGraph secondGraph;

    public OntologyMapper(Collection<OntologyConcept> firstConcepts, Collection<OntologyConcept> secondConcepts,
                          IOntologyGraph firstGraph, IOntologyGraph secondGraph) {
        this.firstConcepts = firstConcepts;
        this.secondConcepts = secondConcepts;
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
    }

    public Collection<OntologyConcept> map() {
        LexicalOrSynsetConceptComparator conceptComparator = new LexicalOrSynsetConceptComparator();

        Set<URI> commonUriSet = SetHelper.INSTANCE.setIntersection(firstGraph.getConceptUris(),
                secondGraph.getConceptUris());
        for (URI uri : commonUriSet) {
            OntologyConcept first = firstGraph.getUriToConcept().get(uri);
            OntologyConcept second = secondGraph.getUriToConcept().get(uri);
            bind(first, second, SAME_URI);
            secondConcepts.remove(second);
        }
        Set<Synset> commonSynsetSet = SetHelper.INSTANCE.setIntersection(firstGraph.getSynsets(), secondGraph.getSynsets());
        for (Synset synset : commonSynsetSet) {
            for (OntologyConcept first : firstGraph.getSynsetToConcept().get(synset)) {
                for (OntologyConcept second : secondGraph.getSynsetToConcept().get(synset)) {
                    bind(first, second, SAME_SYNSET);
                    secondConcepts.remove(second);
                }
            }
        }
        Set<String> commonLabelSet = SetHelper.INSTANCE.setIntersection(firstGraph.getConceptLabels(),
                secondGraph.getConceptLabels());
        for (String label : commonLabelSet) {
            for (OntologyConcept first : firstGraph.getLabelToConcept().get(label)) {
                for (OntologyConcept second : secondGraph.getLabelToConcept().get(label)) {
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
