package ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.merged;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.OntologyConceptSynset;

import java.util.*;

import edu.smu.tspell.wordnet.Synset;

/**
 * @author Anna Zhukova
 */
public class MergedOntologyConcept<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {
    private final List<C> conceptList = new ArrayList<C>();
    private final Synset synset;

    public MergedOntologyConcept(Synset synset) {
        this.synset = synset;
    }

    public MergedOntologyConcept(Synset synset, C concept) {
        this(synset);
        this.conceptList.add(concept);
    }

    public MergedOntologyConcept(OntologyConceptSynset<C, R> ontologyConceptSynset) {
        this(ontologyConceptSynset.getSynset(), ontologyConceptSynset.getConcept());
    }

    public void addConcept(C concept) {
        this.conceptList.add(concept);
    }

    public void addAllConcepts(Collection<C> concepts) {
        this.conceptList.addAll(concepts);
    }

    public Collection<C> getConcepts() {
        return this.conceptList;
    }

    public Synset getSynset() {
        return this.synset;
    }
}
