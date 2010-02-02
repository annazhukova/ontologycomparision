package ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetHelper;
import ru.spbu.math.ontologycomparision.zhukova.logic.wordnet.WordNetRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.builder.OntologyGraphBuilder;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.EmptySynset;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.OntologyConceptSynset;

import java.util.*;
import java.io.FileNotFoundException;

import edu.smu.tspell.wordnet.Synset;

/**
 * @author Anna Zhukova
 */
public class SynsetHelper {

    public static final SynsetHelper INSTANCE = new SynsetHelper();

    private SynsetHelper() {};

    public <C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>>
    Set<OntologyConceptSynset<C, R>> getSynsetsForOntologyGraph(IOntologyGraph<C, R> graph) {
        Set<OntologyConceptSynset<C, R>> result = new HashSet<OntologyConceptSynset<C, R>>();
        for (C concept : graph.getConcepts()) {
            Collection<? extends Synset> sp =
                    WordNetHelper.getSynsetsForWord(concept.getLabel().toLowerCase());
            boolean pFlag = true;
            boolean cFlag = false;
            for (R relation : concept.getSubjectRelations(WordNetRelation.HYPERNYM.getRelatedOntologyConcept())) {
                Collection<? extends Synset> sc =
                        WordNetHelper.getSynsetsForWord(relation.getObject().getLabel().toLowerCase());
                cFlag = true;
                for (Synset o : sc) {
                    for (Synset s : sp) {
                        if (WordNetHelper.getHypernymsForSynset(o).contains(s)) {
                            cFlag = false;
                            pFlag = false;
                            result.add(new OntologyConceptSynset<C, R>(concept, s));
                            result.add(new OntologyConceptSynset<C, R>(relation.getObject(), o));
                        }
                    }
                }
                if (cFlag) {
                    result.add(new OntologyConceptSynset<C, R>(relation.getObject(), new EmptySynset()));
                }
            }
            if (pFlag && cFlag) {
                result.add(new OntologyConceptSynset<C, R>(concept, new EmptySynset()));
            }
        }
        return result;
    }
}
