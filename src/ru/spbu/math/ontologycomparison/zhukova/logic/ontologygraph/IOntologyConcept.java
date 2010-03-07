package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import edu.smu.tspell.wordnet.Synset;
import ru.spbu.math.ontologycomparison.zhukova.util.impl.HashMapTable;

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
    /*List<C> getChildren();

    void addChild(C child);*/

    /**
     * Returns all direct hypernyms of this concept.
     * @return Hypernym list.
     */
    Set<IOntologyConcept> getParents();

    Set<IOntologyConcept> getAllParents();

    void addParent(IOntologyConcept parent);

    /**
     * Returns relations where this concept acts as an object.
     * @return List of relations.
     */
   /* List<R> getObjectRelations();*/

    /**
     * Returns relations where this concept acts as a subject.
     * @return List of relations.
     */
    Set<IOntologyRelation> getSubjectRelations();

    /**
     * Returns all relations for this concept.
     * @return List of relations.
     */
   /* List<R> getRelations();*/

    /**
     * Returns relations with the given name where this concept acts as an object.
     * @param relationName  Relation name.
     * @return List of relations.
     */
    /*List<R> getObjectRelations(String relationName);

    void addObjectRelation(OntologyRelation relation);*/

    /**
     * Returns relations with the given name where this concept acts as a subject.
     * @param relationName  Relation name.
     * @return List of relations.
     */
    Set<IOntologyRelation> getSubjectRelations(String relationName);

    void addSubjectRelation(IOntologyRelation relation);

    /**
     * Returns all relations with the given name for this concept.
     * @para m relationName  Relation name.
     * @return List of relations.
     */
    /*List<R> getRelations(String relationName);*/

    Collection<IOntologyConcept> getSimilarConcepts();

    boolean isRoot();

    void setIsRoot(boolean root);

    HashMapTable<Synset, String> getSynsetToReason();

    void addSynset(Synset synset, String reason);

    HashMapTable<IOntologyConcept, String> getConceptToReason();

    void addConcept(IOntologyConcept concept, String reason);

    boolean hasMappedConcepts();
}
