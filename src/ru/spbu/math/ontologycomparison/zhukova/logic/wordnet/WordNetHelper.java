package ru.spbu.math.ontologycomparison.zhukova.logic.wordnet;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import ru.spbu.math.ontologycomparison.zhukova.util.RecursiveAddHelper;

import java.io.File;
import java.util.*;

/**
 * @author Anna Zhukova
 */
public class WordNetHelper {

    protected static final String WORDNET_DATABASE_DIR_PROPERTY =
            "wordnet.database.dir";
    private static final RecursiveAddHelper<Synset> SYNSET_RECURSIVE_ADD_HELPER = new RecursiveAddHelper<Synset>();
    private static final int MAX_RECURSIVE_LEVEL = 5;
    private static final int MAX_RECURSIVE_SIZE = 40;
    private static final WordNetDatabase DATABASE = WordNetDatabase.getFileInstance();

    static {
        System.setProperty(WORDNET_DATABASE_DIR_PROPERTY,
                (new File("resources/dict/")).getAbsolutePath());
    }

    public static Collection<Synset> getSynsetsForWord(String word) {
        Synset[] synsets = DATABASE.getSynsets(word);
        Collection result = synsets == null ? Collections.EMPTY_SET : Arrays.asList(synsets);
        synsets = null;
        return result;
    }

    public static Collection<Synset> getHypernymsForSynsetRecursively(Synset synset) {
        Collection<Synset> directHypernyms = getHypernymsForSynset(synset);
        Set<Synset> result = new LinkedHashSet<Synset>(directHypernyms);
        SYNSET_RECURSIVE_ADD_HELPER.addRecursively(result, directHypernyms,
                new RecursiveAddHelper.ElementsToAddExtractor<Synset>() {
                    public Collection<Synset> extract(Synset element) {
                        return getHypernymsForSynset(element);
                    }
                }, MAX_RECURSIVE_LEVEL, MAX_RECURSIVE_SIZE);
        return result;
    }

    public static Collection<Synset> getHypernymsForSynset(Synset synset) {
        if (synset instanceof NounSynset) {
            return new HashSet<Synset>(Arrays.asList(((NounSynset) synset).getHypernyms()));
        }
        if (synset instanceof VerbSynset) {
            return new HashSet<Synset>(Arrays.asList(((VerbSynset) synset).getHypernyms()));
        }
        return Collections.EMPTY_SET;
    }

    public static Collection<Synset> getHyponymsForSynsetRecursively(Synset synset) {
        Collection<Synset> directHyponyms = getHyponymsForSynset(synset);
        Set<Synset> result = new LinkedHashSet<Synset>(directHyponyms);
        SYNSET_RECURSIVE_ADD_HELPER.addRecursively(result, directHyponyms,
                new RecursiveAddHelper.ElementsToAddExtractor<Synset>() {
                    public Collection<Synset> extract(Synset element) {
                        return getHyponymsForSynset(element);
                    }
                }, MAX_RECURSIVE_LEVEL, MAX_RECURSIVE_SIZE);
        return result;
    }

    public static Collection<Synset> getHyponymsForSynset(Synset synset) {
        if (synset instanceof NounSynset) {
            return new HashSet<Synset>(Arrays.asList(((NounSynset) synset).getHyponyms()));
        }
        return Collections.EMPTY_SET;
    }    


    public static Collection<Synset> getPartHolonymsForSynset(Synset synset) {
        if (synset instanceof NounSynset) {
            return new HashSet<Synset>(Arrays.asList(((NounSynset) synset).getPartHolonyms()));
        }
        return Collections.EMPTY_SET;
    }
}
