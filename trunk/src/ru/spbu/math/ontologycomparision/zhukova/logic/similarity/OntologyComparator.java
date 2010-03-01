package ru.spbu.math.ontologycomparision.zhukova.logic.similarity;

import com.sun.istack.internal.NotNull;
import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.SynsetHelper;
import ru.spbu.math.ontologycomparision.zhukova.util.HashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.SetHelper;

import java.util.*;

/**
 * @author Anna Zhukova
 */
public class OntologyComparator<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {
    private SynsetHelper<C, R> firstSynsetHelper;
    private SynsetHelper<C, R> secondSynsetHelper;
    public static final int LEXICAL_DIFFERENCE_THRESHOLD = 0;
    private Map<C, SimilarConcepts<C, R>> conceptToSimilarConcepts;
    private final int conceptsCount;
    private static final String UNMAPPED_REASON = "Unmapped";
    private static final String LEXICAL_REASON = "Lexical";

    public OntologyComparator(IOntologyGraph<C, R> firstGraph, IOntologyGraph<C, R> secondGraph) {
        this.conceptsCount = firstGraph.getConcepts().size() + secondGraph.getConcepts().size();
        this.firstSynsetHelper = new SynsetHelper<C, R>(firstGraph);
        this.secondSynsetHelper = new SynsetHelper<C, R>(secondGraph);
    }

    public double getSimilarity() {
        if (this.conceptToSimilarConcepts == null) {
            this.mergeOntologies();
        }
        double intersectionSize = (double) conceptToSimilarConcepts.keySet().size() / 2;
        return intersectionSize / (this.conceptsCount - intersectionSize);
    }

    public Set<ISimilarConcepts<C, R>> mergeOntologies() {
        Set<ISimilarConcepts<C, R>> result = new HashSet<ISimilarConcepts<C, R>>();
        Set<C> unmapped = new HashSet<C>();
        this.conceptToSimilarConcepts = new HashMap<C, SimilarConcepts<C, R>>();
        //System.out.println("MERGING SYNSET CONCEPTS");
        this.mergeSynsetConcepts(result, unmapped, this.conceptToSimilarConcepts);
        /*System.out.printf("\tRESULT: %s\n", result);
        System.out.printf("\tUNMAPPED: %s\n", unmapped);
        System.out.println("MERGING NO SYNSET CONCEPTS");*/
        this.mergeNoSynsetConcepts(result, unmapped, this.conceptToSimilarConcepts);
        /*System.out.printf("\tRESULT: %s\n", result);
        System.out.printf("\tUNMAPPED: %s\n", unmapped);*/
        this.addUnmappedConceptsToResult(result, unmapped);
        return result;
    }

    

    private void addUnmappedConceptsToResult(Set<ISimilarConcepts<C, R>> result, Set<C> unmapped) {
        for (C concept : unmapped) {
            Synset synset = getSynsetForConcept(concept, concept);
            if (synset != null) {
                result.add(new SimilarConcepts<C, R>(synset, concept));
            } else {
                result.add(new SimilarConcepts<C, R>(UNMAPPED_REASON, concept));
            }
        }
    }

    private void mergeSynsetConcepts(Set<ISimilarConcepts<C, R>> result, Set<C> unmapped, Map<C, SimilarConcepts<C, R>> conceptToSimilarConcepts) {
        IHashTable<Synset, C> firstSynsetToConcept = this.firstSynsetHelper.getSynsetToConceptTable();
        IHashTable<Synset, C> secondSynsetToConcept = this.secondSynsetHelper.getSynsetToConceptTable();
        for (Map.Entry<Synset, Set<C>> entry : firstSynsetToConcept.entrySet()) {
            Synset synset = entry.getKey();
            Set<C> first = entry.getValue();
            Set<C> second = secondSynsetToConcept.get(synset);
            if (second != null) {
                conceptsAreSimilar(result, first, second, conceptToSimilarConcepts, synset);
            } else {
                unmapped.addAll(entry.getValue());
            }
        }
        for (Synset key : SetHelper.INSTANCE.setSubtraction(secondSynsetToConcept.keySet(), firstSynsetToConcept.keySet())) {
            unmapped.addAll(secondSynsetToConcept.get(key));
        }
    }

    private void conceptsAreSimilar(Set<ISimilarConcepts<C, R>> result, Set<C> first, Set<C> second,
                                    Map<C, SimilarConcepts<C, R>> conceptToSimilarConcepts, Object similarityReason) {
        SimilarConcepts<C, R> similarConcepts = new SimilarConcepts<C, R>(similarityReason, first);
        similarConcepts.addConcepts(second);
        result.add(similarConcepts);
        for (C concept : first) {
            conceptToSimilarConcepts.put(concept, similarConcepts);
        }
        for (C concept : second) {
            conceptToSimilarConcepts.put(concept, similarConcepts);
        }
    }


    private IHashTable<String[], C> mergeUnmappedAndNoSynsetConcepts(Set<C> unmapped) {
        IHashTable<String[], C> allNoSynsetConcepts = this.noSynsetConceptUnion();
        List<String[]> noSynsetConceptLabels = new ArrayList<String[]>(allNoSynsetConcepts.keySet());
        IHashTable<String[], C> mergedNoSynsetConcepts = new HashTable<String[], C>();
        lexicallyMergeUnmappedAndNoSynsetConcepts(unmapped, allNoSynsetConcepts, noSynsetConceptLabels, mergedNoSynsetConcepts);
        lexicallyMergeNoSynsetConcepts(allNoSynsetConcepts, noSynsetConceptLabels, mergedNoSynsetConcepts);
        /*System.out.printf("\tPREMERGED: %s\n", mergedNoSynsetConcepts);*/
        return mergedNoSynsetConcepts;
    }

    private void mergeNoSynsetConcepts(Set<ISimilarConcepts<C, R>> result, Set<C> unmapped,
                                       Map<C, SimilarConcepts<C, R>> conceptToSimilarConcepts) {
        IHashTable<String[], C> mergedNoSynsetConcepts = this.mergeUnmappedAndNoSynsetConcepts(unmapped);
        unmapped.clear();
        unmapped.addAll(mergedNoSynsetConcepts.allValues());
        for (Map.Entry<String[], Set<C>> entry : mergedNoSynsetConcepts.entrySet()) {
            List<C> values = new ArrayList<C>(entry.getValue());
            if (values.size() == 1) {
                C concept = values.get(0);
                unmapped.add(concept);
            } else {
                int j = 0;
                for (C first : values) {
                    if (++j == values.size()) {
                        break;
                    }
                    for (Iterator<C> it = values.listIterator(j); it.hasNext();) {
                        C second = it.next();
                        Set<C> firstParents = first.getAllParents();
                        Set<C> secondParents = second.getAllParents();
                        allParentCycleLabel:
                        for (C firstParent : firstParents) {
                            SimilarConcepts<C, R> similarToFirstParent = conceptToSimilarConcepts.get(firstParent);
                            if (similarToFirstParent != null &&
                                    SetHelper.INSTANCE.setIntersection(similarToFirstParent.getConcepts(), secondParents).size() > 0) {
                                conceptsAreSimilar(result, first, second, unmapped, conceptToSimilarConcepts);
                            } else {
                                for (C secondParent : secondParents) {
                                    if (!areLabelsReallyDifferent(firstParent.getLabels(), secondParent.getLabels())) {
                                        conceptsAreSimilar(result, first, second, unmapped, conceptToSimilarConcepts);
                                        conceptsAreSimilar(result, firstParent, secondParent, unmapped, conceptToSimilarConcepts);
                                        break allParentCycleLabel;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void lexicallyMergeNoSynsetConcepts(IHashTable<String[], C> noSynsetConcepts, List<String[]> noSynsetConceptLabels, IHashTable<String[], C> mergedNoSynsetConcepts) {
        List<String[]> labelsToRemove = new ArrayList<String[]>();
        int i = 0;
        for (String[] firstLabel : noSynsetConceptLabels) {
            i++;
            if (i < noSynsetConceptLabels.size()) {
                for (Iterator<String[]> iterator = noSynsetConceptLabels.listIterator(i); iterator.hasNext();) {
                    String[] secondLabel = iterator.next();
                    if (!areLabelsReallyDifferent(firstLabel, secondLabel)) {
                        mergedNoSynsetConcepts.insertAll(firstLabel, noSynsetConcepts.get(firstLabel));
                        mergedNoSynsetConcepts.insertAll(firstLabel, noSynsetConcepts.get(secondLabel));
                        labelsToRemove.add(secondLabel);
                        labelsToRemove.add(firstLabel);

                    }
                }
            }
        }
        noSynsetConceptLabels.removeAll(labelsToRemove);
        for (String[] label : noSynsetConceptLabels) {
            mergedNoSynsetConcepts.insertAll(label, noSynsetConcepts.get(label));
        }
    }

    private void lexicallyMergeUnmappedAndNoSynsetConcepts(Set<C> unmappedConcepts, IHashTable<String[], C> noSynsetConcepts,
                                                           List<String[]> noSynsetConceptLabels, IHashTable<String[], C> mergedNoSynsetConcepts) {
        // lexically merging unmapped concepts with no synset concepts
        for (C concept : unmappedConcepts) {
            // unmapped concepts have synsets so their labels are better ones
            String[] label = concept.getLabels();
            mergedNoSynsetConcepts.insert(label, concept);
            for (Iterator<String[]> it = noSynsetConceptLabels.iterator(); it.hasNext();) {
                String[] noSynsetConceptLabel = it.next();
                if (!areLabelsReallyDifferent(label, noSynsetConceptLabel)) {
                    mergedNoSynsetConcepts.insertAll(label, noSynsetConcepts.get(noSynsetConceptLabel));
                    it.remove();
                }
            }
        }
    }

    private boolean areLabelsReallyDifferent(String[] label1, String[] label2) {
        for (String firstLabel : label1) {
            for (String secondLabel : label2) {
                 /*if (OntologyComparator.differencePerCent(this.getNormalizedString(firstLabel),
                         this.getNormalizedString(secondLabel)) <= OntologyComparator.LEXICAL_DIFFERENCE_THRESHOLD) {
                     return false;
                 }*/
                if (this.getNormalizedString(firstLabel).equals(this.getNormalizedString(secondLabel))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void conceptsAreSimilar(Set<ISimilarConcepts<C, R>> result, C first, C second, Set<C> unmapped,
                                    Map<C, SimilarConcepts<C, R>> conceptToSimilarConcepts) {
        SimilarConcepts<C, R> similar = conceptToSimilarConcepts.get(first);
        if (similar == null) {
            similar = conceptToSimilarConcepts.get(second);
            if (similar != null) {
                similar.addConcept(first);
                conceptToSimilarConcepts.put(first, similar);
            }
        } else {
            similar.addConcept(second);
            conceptToSimilarConcepts.put(second, similar);
        }
        Synset synset = getSynsetForConcept(first, second);
        if (similar == null) {
            if (synset != null) {
                similar = new SimilarConcepts<C, R>(synset, first, second);
            } else {
                similar = new SimilarConcepts<C, R>(LEXICAL_REASON, first, second);
            }
            conceptToSimilarConcepts.put(first, similar);
            conceptToSimilarConcepts.put(second, similar);
        } else {
            similar.addSimilarityReason(synset == null ? LEXICAL_REASON : synset);
        }
        result.add(similar);
        unmapped.remove(first);
        unmapped.remove(second);
    }

    private Synset getSynsetForConcept(C... concept) {
        /*System.out.println(first + " " + second);
        System.out.println(" " + this.firstSynsetHelper.getConceptToSynsetTable());
        System.out.println(" " + this.secondSynsetHelper.getConceptToSynsetTable());*/
        for (C aConcept : concept) {
            Set<Synset> firstSynsets = this.firstSynsetHelper.getConceptToSynsetTable().get(aConcept);
            Set<Synset> secondSynsets = this.secondSynsetHelper.getConceptToSynsetTable().get(aConcept);
            Set<Synset> setIntersection = SetHelper.INSTANCE.setIntersection(firstSynsets, secondSynsets);
            if (!setIntersection.isEmpty()) {
                return setIntersection.iterator().next();
            }
            if (firstSynsets != null && !firstSynsets.isEmpty()) {
                return firstSynsets.iterator().next();
            }
            if (secondSynsets != null && !secondSynsets.isEmpty()) {
                return secondSynsets.iterator().next();
            }
        }
        return null;
    }

    private String getNormalizedString(String s) {
        return s.toLowerCase().replaceAll("[\\-_]", " ").trim();
    }

    public static int differencePerCent(@NotNull String s1, @NotNull String s2) {
        int[][] a = new int[s1.length()][s2.length()];
        if (s1.length() == 0) {
            return s2.length() * 100;
        }
        if (s2.length() == 0) {
            return s1.length() * 100;
        }
        for (int i = 0; i < s1.length(); i++) {
            a[i][0] = i;
        }

        for (int j = 0; j < s2.length(); j++) {
            a[0][j] = j;
        }

        for (int i = 1; i < s1.length(); i++) {
            for (int j = 1; j < s2.length(); j++) {
                a[i][j] = Math.min(Math.min(a[i - 1][j - 1] + (s1.charAt(i) == s2.charAt(j) ? 0 : 1), a[i - 1][j] + 1), a[i][j - 1] + 1);
            }
        }

        double minLength = (double) Math.min(s1.length(), s2.length());
        if (minLength == 0.0) {
            minLength = 0.01;
        }
        return (int) ((a[s1.length() - 1][s2.length() - 1] / minLength) * 100);
    }

    public IHashTable<String[], C> noSynsetConceptUnion() {
        IHashTable<String[], C> result = new HashTable<String[], C>(this.firstSynsetHelper.getConcepsWithNoSynset());
        result.insertAll(this.secondSynsetHelper.getConcepsWithNoSynset());
        return result;
    }

    private Set<Synset> getParentSynsets(Map<C, Synset> conceptToSynsetMap, C concept) {
        Set<Synset> result = new HashSet<Synset>();
        for (C parent : concept.getAllParents()) {
            Synset synset = conceptToSynsetMap.get(parent);
            if (synset != null) {
                result.add(synset);
            }
        }
        return result;
    }

}
