package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.impl.OntologyRelation;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

/**
 * @author Anna Zhukova
 */
public interface IOntologyConcept<C extends IOntologyConcept, R extends IOntologyRelation<C>> {

    /**
     * URI of thic concept.
     * @return URI.
     */
    URI getUri();

    String[] getLabels();

    Collection<String> getLabelCollection();

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
    Set<C> getParents();

    Set<C> getAllParents();

    void addParent(C parent);

    /**
     * Returns relations where this concept acts as an object.
     * @return List of relations.
     */
   /* List<R> getObjectRelations();*/

    /**
     * Returns relations where this concept acts as a subject.
     * @return List of relations.
     */
    Set<R> getSubjectRelations();

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
    Set<R> getSubjectRelations(String relationName);

    void addSubjectRelation(OntologyRelation relation);

    /**
     * Returns all relations with the given name for this concept.
     * @param relationName  Relation name.
     * @return List of relations.
     */
    /*List<R> getRelations(String relationName);*/
}
