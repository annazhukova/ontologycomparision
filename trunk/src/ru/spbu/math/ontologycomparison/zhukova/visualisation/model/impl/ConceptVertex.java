package ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyConcept;

import java.awt.*;

/**
 * @author Anna Zhukova
 */
public class ConceptVertex extends SimpleVertex {
    private final IOntologyConcept concept;

    public ConceptVertex(IOntologyConcept concept, Point location, SuperVertex superVertex, Color color) {
        super(superVertex, color);
        this.concept = concept;
        setLocation(location);
    }

    public IOntologyConcept getConcept() {
        return concept;
    }

    public String getName() {
        return concept.getMainLabel();
    }
}
