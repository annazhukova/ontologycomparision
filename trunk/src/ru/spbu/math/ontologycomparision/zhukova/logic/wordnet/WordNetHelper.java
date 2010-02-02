package ru.spbu.math.ontologycomparision.zhukova.logic.wordnet;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.VerbSynset;

import java.io.File;
import java.util.*;

/**
 * @author Anna Zhukova
 */
public class WordNetHelper {

    protected static final String WORDNET_DATABASE_DIR_PROPERTY =
            "wordnet.database.dir";

    static {
        System.setProperty(WORDNET_DATABASE_DIR_PROPERTY,
                (new File("./resources/dict/")).getAbsolutePath());
    }

    public static Collection<Synset> getSynsetsForWord(String word) {
        WordNetDatabase database = WordNetDatabase.getFileInstance();
        return Arrays.asList(database.getSynsets(word));
    }

    public static Collection<? extends Synset> getHypernymsForSynset(Synset synset) {
        if (synset instanceof NounSynset) {
            /*NounSynset nounSynset = (NounSynset) synset;
            NounSynset[] hypernyms = nounSynset.getHypernyms();
            NounSynset[] instanceHypernyms = nounSynset.getInstanceHypernyms();
            NounSynset[] result =
                    new NounSynset[hypernyms.length + instanceHypernyms.length];
            System.arraycopy(hypernyms, 0, result, 0, hypernyms.length);
            System.arraycopy(instanceHypernyms, 0, result, hypernyms.length,
                    instanceHypernyms.length);
            return Arrays.asList(result);*/
            NounSynset nounSynset = (NounSynset) synset;
            List<NounSynset> result = new ArrayList<NounSynset>();
            result.addAll(Arrays.asList(nounSynset.getHypernyms()));
            for (NounSynset hypernym : nounSynset.getHypernyms()) {
                result.addAll((List<NounSynset>)getHypernymsForSynset(hypernym));
            }
            return result;
        }
        if (synset instanceof VerbSynset) {
            return Arrays.asList(((VerbSynset) synset).getHypernyms());
        }
        return Collections.EMPTY_LIST;
    }

    public static Collection<? extends Synset> getHyponymsForSynset(Synset synset) {
        if (synset instanceof NounSynset) {
            NounSynset nounSynset = (NounSynset) synset;
            NounSynset[] hyponyms = nounSynset.getHyponyms();
            NounSynset[] instanceHyponyms = nounSynset.getInstanceHyponyms();
            NounSynset[] result =
                    new NounSynset[hyponyms.length + instanceHyponyms.length];
            System.arraycopy(hyponyms, 0, result, 0, hyponyms.length);
            System.arraycopy(instanceHyponyms, 0, result, hyponyms.length,
                    instanceHyponyms.length);
            return Arrays.asList(result);
        }
        return Collections.EMPTY_LIST;
    }
}
