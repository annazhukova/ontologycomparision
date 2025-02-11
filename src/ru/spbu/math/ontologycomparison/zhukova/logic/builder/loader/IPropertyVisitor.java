package ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader;

import org.semanticweb.owlapi.model.OWLPropertyExpression;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;

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
