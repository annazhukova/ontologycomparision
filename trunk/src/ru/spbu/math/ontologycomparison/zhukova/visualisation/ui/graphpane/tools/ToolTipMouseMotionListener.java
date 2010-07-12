package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Anna R. Zhukova
 */
public class ToolTipMouseMotionListener extends MouseAdapter {
    private final GraphPane graphPane;

    public ToolTipMouseMotionListener(GraphPane gp) {
        this.graphPane = gp;
    }
    
    public void mouseMoved(MouseEvent e) {
       /* IGraphModel gm = this.graphPane.getGraphModel();
        if (gm != null) {
            Point mouseLocation = e.getPoint();
            Set<IArc> arcs = gm.getArcs();
            for (IArc arc : arcs) {
                if (arc.hitTest(mouseLocation)) {
                    this.graphPane.setToolTipText(arc.getToolTipHTML());
                    return;
                }
            }
            Collection<SuperVertex> superVertices = gm.getKeyToSuperVertexMap().values();
            for (IVertex vertex : superVertices) {
                if (vertex.hitTest(mouseLocation) && !vertex.isHidden()) {
                    this.graphPane.setToolTipText(String.format("<html>%s", vertex.getToolTipText()));
                    return;
                }
            }
            this.graphPane.setToolTipText(null);
        }*/
    }
}
