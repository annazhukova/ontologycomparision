package ru.spbu.math.ontologycomparision.zhukova.logic.owl;

import org.semanticweb.owl.model.OWLPropertyExpression;
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
     * @param property Property of relation.
     */
    void inRelationship(C node, C friend, OWLPropertyExpression property);
}
