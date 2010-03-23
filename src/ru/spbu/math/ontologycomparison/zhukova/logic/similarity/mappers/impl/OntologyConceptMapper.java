package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers.impl;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.logic.ILogger;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyGraph;
import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.impl.LexicalOrSynsetConceptComparator;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.SetHelper;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.Triple;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
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
    private final ILogger logger;

    public OntologyConceptMapper(Collection<IOntologyConcept> firstConcepts, Collection<IOntologyConcept> secondConcepts,
                          IOntologyGraph firstGraph, IOntologyGraph secondGraph, ILogger logger) {
        this.firstConcepts = firstConcepts;
        this.secondConcepts = secondConcepts;
        this.firstGraph = firstGraph;
        this.secondGraph = secondGraph;
        this.logger = logger;
    }

    public Collection<IOntologyConcept> map() {
        return null;
    }

    public IOntologyGraph mapp() {
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

        logger.log("binded ontologies");
        Set<IOntologyConcept> roots = SetHelper.INSTANCE.setUnion(firstGraph.getRoots(), SetHelper.INSTANCE.setIntersection(secondGraph.getRoots(), secondConcepts));
        HashMap<URI, IOntologyConcept> uriToConcept = new HashMap<URI, IOntologyConcept>(firstGraph.getUriToConcept());
        for (IOntologyConcept second : secondConcepts) {
            uriToConcept.put(second.getUri(), second);
        }
        return new OntologyGraph(roots, uriToConcept, null, null, null);
    }

    public ITriple<WordNetRelation, String, String>[] getBindFactors() {
        return new Triple[]{
                new Triple<WordNetRelation, String, String>(WordNetRelation.HYPONYM, SAME_PARENTS, SAME_CHILDREN),
                new Triple<WordNetRelation, String, String>(WordNetRelation.MERONYM, SAME_PARTS, SAME_WHOLE)
        };
    }

    public void bind(IOntologyConcept first, IOntologyConcept second, String reason, int count) {
        first.addConcept(second, reason, count);
        second.addConcept(first, reason, count);
        secondConcepts.remove(second);
    }

    public void bind(IOntologyConcept first, IOntologyConcept second, String reason) {
        bind(first, second, reason, 1);
    }
}
