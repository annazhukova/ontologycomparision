package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.*;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.util.*;

/**
 * @author Anna R. Zhukova
 */
public class ToolTipMouseMotionListener extends MouseAdapter {
    private final GraphPane graphPane;

    public ToolTipMouseMotionListener(GraphPane gp) {
        this.graphPane = gp;
    }
    
    public void mouseMoved(MouseEvent e) {
        IGraphModel gm = this.graphPane.getGraphModel();
        if (gm != null) {
            Point mouseLocation = e.getPoint();
            Collection<SimpleVertex> simpleVertices = gm.getNameToSimpleVertexMap().values();
            for (IVertex vertex : simpleVertices) {
                if (vertex.hitTest(mouseLocation) && !vertex.isHidden()) {
                    this.graphPane.setToolTipText(vertex.getToolTipText());
                    return;
                }
            }
            Set<IArc> arcs = gm.getArcs();
            for (IArc arc : arcs) {
                if (arc.hitTest(mouseLocation)) {
                    this.graphPane.setToolTipText(arc.getToolTipHTML());
                    return;
                }
            }
            Collection<SuperVertex> superVertices = gm.getNameToSuperVertexMap().values();
            for (IVertex vertex : superVertices) {
                if (vertex.hitTest(mouseLocation) && !vertex.isHidden()) {
                    this.graphPane.setToolTipText(vertex.getToolTipText());
                    return;
                }
            }
            this.graphPane.setToolTipText(null);
        }
    }
}
