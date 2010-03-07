package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public interface IOntologyProperty extends ILabeledOntologyEntity {

    Collection<IOntologyConcept> getDomains();

    Collection<IOntologyConcept> getRanges();

    boolean isFunctional();

    void addProperty(IOntologyProperty property, String reason);
}
