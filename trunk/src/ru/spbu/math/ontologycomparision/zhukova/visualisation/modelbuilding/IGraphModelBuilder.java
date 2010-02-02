package ru.spbu.math.ontologycomparision.zhukova.visualisation.modelbuilding;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

/**
 * @author Anna R. Zhukova
 */
public interface IGraphModelBuilder {

    GraphModel buildGraphModel(GraphPane graphPane);
}
