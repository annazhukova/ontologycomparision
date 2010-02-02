package ru.spbu.math.ontologycomparision.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.merged.MergedOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.OntologyConceptSynset;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.SynsetHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.smu.tspell.wordnet.Synset;

/**
 * @author Anna Zhukova
 */
public class OntologyComparator<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {

    public double similar(IOntologyGraph<C, R> o1, IOntologyGraph<C, R> o2) {
        Set<OntologyConceptSynset<C, R>> rss1 = SynsetHelper.INSTANCE.getSynsetsForOntologyGraph(o1);
        Set<OntologyConceptSynset<C, R>> rss2 = SynsetHelper.INSTANCE.getSynsetsForOntologyGraph(o2);
        Set<OntologyConceptSynset<C, R>> rss1NOTrss2 = new HashSet<OntologyConceptSynset<C, R>>(rss1);
        rss1NOTrss2.removeAll(rss2);
        Set<OntologyConceptSynset<C, R>> rss1ANDrss2 = new HashSet<OntologyConceptSynset<C, R>>(rss1);
        rss1ANDrss2.removeAll(rss1NOTrss2);
        Set<OntologyConceptSynset<C, R>> rss1ORrss2 = new HashSet<OntologyConceptSynset<C, R>>(rss1);
        rss1ORrss2.addAll(rss2);
        return ((double) rss1ANDrss2.size()) / rss1ORrss2.size();
    }

    public Map<Synset, MergedOntologyConcept<C, R>> merge(IOntologyGraph<C, R> o1, IOntologyGraph<C, R> o2) {
        Set<OntologyConceptSynset<C, R>> rss1 = SynsetHelper.INSTANCE.getSynsetsForOntologyGraph(o1);
        Set<OntologyConceptSynset<C, R>> rss2 = SynsetHelper.INSTANCE.getSynsetsForOntologyGraph(o2);
        final HashMap<Synset, MergedOntologyConcept<C, R>> result =
                new HashMap<Synset, MergedOntologyConcept<C, R>>() {
            public MergedOntologyConcept<C, R> put(Synset synset, MergedOntologyConcept<C, R> concept) {
                MergedOntologyConcept<C, R> old = this.get(synset);
                if (old == null) {
                    super.put(synset, concept);
                } else {
                    old.addAllConcepts(concept.getConcepts());
                }
                return old;
            }
        };
        for (OntologyConceptSynset<C, R> ontologyConceptSynset : rss1) {
            result.put(ontologyConceptSynset.getSynset(),
                    new MergedOntologyConcept<C, R>(ontologyConceptSynset));
        }
        for (OntologyConceptSynset<C, R> ontologyConceptSynset : rss2) {
            result.put(ontologyConceptSynset.getSynset(),
                    new MergedOntologyConcept<C, R>(ontologyConceptSynset));
        }
        return result;
    }

}
