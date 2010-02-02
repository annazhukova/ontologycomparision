package ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordSense;
import edu.smu.tspell.wordnet.WordNetException;

import java.util.Arrays;

/**
 * @author Anna Zhukova
 */
public class OntologyConceptSynset<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>>
        implements Synset {

    private final C concept;
    private final Synset synset;

    public OntologyConceptSynset(C concept, Synset synset) {
        this.concept = concept;
        this.synset = synset;
    }

    public SynsetType getType() {
        if (getSynset() == null) {
            return null;
        }
        return getSynset().getType();
    }

    public String[] getWordForms() {
        if (getSynset() == null) {
            return new String[0];
        }
        return getSynset().getWordForms();
    }

    public WordSense[] getAntonyms(String wordForm) throws WordNetException {
        if (getSynset() == null) {
            return new WordSense[0];
        }
        return getSynset().getAntonyms(wordForm);
    }

    public WordSense[] getDerivationallyRelatedForms(String wordForm) throws WordNetException {
        if (getSynset() == null) {
            return new WordSense[0];
        }
        return getSynset().getDerivationallyRelatedForms(wordForm);
    }

    public int getTagCount(String wordForm) {
        if (getSynset() == null) {
            return 0;
        }
        return getSynset().getTagCount(wordForm);
    }

    public String getDefinition() {
        if (getSynset() == null) {
            return "";
        }
        return getSynset().getDefinition();
    }

    public String[] getUsageExamples() {
        if (getSynset() == null) {
            return new String[0];
        }
        return getSynset().getUsageExamples();
    }

    public C getConcept() {
        return this.concept;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof OntologyConceptSynset)) {
            return false;
        }
        OntologyConceptSynset that = (OntologyConceptSynset) o;
        if (this.getSynset() == null || that.getSynset() == null) {
            return false;
        }
        return this.getSynset().equals(that.getSynset());
    }

    public int hashCode() {
        if (this.getSynset() == null) {
            return super.hashCode();
        }
        return this.getSynset().hashCode();
    }

    public String toString() {
        return String.format("%s : %s", this.getSynset() != null ?
                Arrays.asList(this.getSynset().getWordForms()) : "OntologyConceptSynset",
                this.getConcept().getLabel());
    }

    public Synset getSynset() {
        return this.synset;
    }
}
