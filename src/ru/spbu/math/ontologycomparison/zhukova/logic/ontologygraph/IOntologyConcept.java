package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.util.IHashMapTable;

import java.util.Collection;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IOntologyConcept extends ILabeledOntologyEntity {

    /**
     * Returns all direct hyponyms of this concept.
     * @return Hyponym list.
     */
    Set<IOntologyConcept> getParents();

    Set<IOntologyConcept> getAllParents();

    void addParent(IOntologyConcept parent);

    /**
     * Returns relations where this concept acts as a subject.
     * @return List of relations.
     */
    Set<IOntologyRelation> getSubjectRelations();

    /**
     * Returns relations with the given name where this concept acts as a subject.
     * @param relationName  Relation name.
     * @return List of relations.
     */
    Set<IOntologyRelation> getSubjectRelations(String relationName);

    void addSubjectRelation(IOntologyRelation relation);

    Collection<IOntologyConcept> getSimilarConcepts();

    boolean isRoot();

    void setIsRoot(boolean root);

    IHashMapTable<Synset, String> getSynsetToReason();

    void addSynset(Synset synset, String reason, int count);

    IHashMapTable<IOntologyConcept, String> getConceptToReason();

    void addConcept(IOntologyConcept concept, String reason, int count);

    boolean hasMappedConcepts();

    int getDepth();

    void increaseDepth();

    IOntologyConcept[] getChildren();

    void addChild(IOntologyConcept child);

    boolean hasSynsets();
}
