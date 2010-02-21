package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * @author Anna R. Zhukova
 */
public abstract class Tool extends MouseAdapter implements ITool {
    private static GraphPane graphPane;

    public static void setGraphModel(GraphPane graphPane) {
        Tool.graphPane = graphPane;
    }

    /*package*/ static GraphPane getGraphPane() {
        return Tool.graphPane;
    }

    public abstract void paint(Graphics g);
}
