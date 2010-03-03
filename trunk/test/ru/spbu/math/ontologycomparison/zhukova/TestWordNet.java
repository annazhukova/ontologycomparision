package ru.spbu.math.ontologycomparison.zhukova;

import edu.smu.tspell.wordnet.Synset;

import junit.framework.TestCase;
import junit.framework.Assert;
import ru.spbu.math.ontologycomparison.zhukova.logic.wordnet.WordNetHelper;

import java.util.Collection;

/**
 * @author Anna R. Zhukova
 */
public class TestWordNet extends TestCase {
    private static final String JAVA = "java";
    private static final String COFFEE = "coffee";
    private static final String JAVA_DEFINITION = "a simple platform-independent object-oriented programming language";
    private static final String PROGRAMMING_LANGUAGE_DEFINITION = "a language designed for programming computers";
    private static final int JAVA_SYNSET_NUMBER = 3;

    public void testSynsetCount() {
        Assert.assertEquals(JAVA_SYNSET_NUMBER, WordNetHelper.getSynsetsForWord(JAVA).size());
    }

    public void testSynsetContent() {
        for (Synset synset : WordNetHelper.getSynsetsForWord(JAVA)) {
            for (String word : synset.getWordForms()) {
                if (word.equalsIgnoreCase(COFFEE)) {
                    return;
                }
            }
        }
        fail();
    }

    public void testHypernyms() {
        for (Synset synset : WordNetHelper.getSynsetsForWord(JAVA)) {
            if (synset.getDefinition().contains(JAVA_DEFINITION)) {
                Collection<? extends Synset> hypernymSet =
                        WordNetHelper.getHypernymsForSynset(synset);
                for (Synset hypernym : hypernymSet) {
                    if (hypernym.getDefinition().contains(PROGRAMMING_LANGUAGE_DEFINITION)) {
                        return;
                    }
                }
            }
        }
        fail();
    }
}
