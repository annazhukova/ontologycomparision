package ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordSense;
import edu.smu.tspell.wordnet.WordNetException;

/**
 * @author Anna Zhukova
 */
public class EmptySynset implements Synset {
    private static final String EMPTY_SYNSET_DEFINITION = "<Empty Synset>";

    public SynsetType getType() {
        return null;
    }

    public String[] getWordForms() {
        return new String[0];
    }

    public WordSense[] getAntonyms(String wordForm) throws WordNetException {
        return new WordSense[0];
    }

    public WordSense[] getDerivationallyRelatedForms(String wordForm) throws WordNetException {
        return new WordSense[0];
    }

    public int getTagCount(String wordForm) {
        return 0;
    }

    public String getDefinition() {
        return EMPTY_SYNSET_DEFINITION;
    }

    public String[] getUsageExamples() {
        return new String[0];  
    }
}
