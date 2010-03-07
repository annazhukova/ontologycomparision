package ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph;

import java.net.URI;
import java.util.Collection;

/**
 * @author Anna Zhukova
 */
public interface ILabeledOntologyEntity {

    URI getUri();    

    String[] getLabels();

    String getMainLabel();

    Collection<String> getLabelCollection();
}
