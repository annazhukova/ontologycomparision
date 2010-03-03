package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

/**
 * @author Anna R. Zhukova
 */
public interface IGraphModelBuilder {

    GraphModel buildGraphModel(GraphPane graphPane);
    
    int getSimilarity();
}
