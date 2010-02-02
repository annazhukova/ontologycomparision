package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo.IUndoManager;

import java.awt.event.MouseAdapter;
import java.awt.*;

/**
 * @author Anna R. Zhukova
 */
public abstract class Tool extends MouseAdapter implements ITool {
    private static GraphPane graphPane;
    private static IUndoManager undoManager;

    public static void setGraphModel(GraphPane graphPane, IUndoManager undoManager) {
        Tool.graphPane = graphPane;
        Tool.undoManager = undoManager;
    }

    /*package*/ static GraphPane getGraphPane() {
        return Tool.graphPane;
    }

    /*package*/ static IUndoManager getUndoManager() {
        return Tool.undoManager;
    }

    public abstract void paint(Graphics g);
}
