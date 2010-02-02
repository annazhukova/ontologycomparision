package ru.spbu.math.ontologycomparision.zhukova.logic.owl;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;

/**
 * @author Anna Zhukova
 * Strategy of nodes relationship induced by the property processing.
 */
public interface IPropertyVisitor<C extends IOntologyConcept> {

    /**
     * Process relationship forsed by the property specified between given nodes.
     * @param node First node.
     * @param friend Second node.
     * @param relationName Name of relation.
     */
    void inRelationship(C node, C friend, String relationName);
}
