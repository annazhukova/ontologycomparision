package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public interface ILabeledOntologyEntity {

    IRI getUri();    

    String[] getLabels();

    String getNormalizedMainLabel();

    String getMainLabel();

    Collection<String> getLabelCollection();
}
