package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.commands.Composite;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.commands.MoveVertexCommand;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.GraphPane;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.IGraphPane;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IVertex;

import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public class MovingTool extends Tool {
    private static Point veryFrom;
    private static Point from;

    private static final MovingTool INSTANCE = new MovingTool();

    private MovingTool() {
        super();
    }

    public static MovingTool getInstance(Point from) {
        MovingTool.from = from;
        MovingTool.veryFrom = from;
        return INSTANCE;
    }

    public void mouseDragged(MouseEvent e) {
        Point mouseLocation = e.getPoint();
        int dx = mouseLocation.x - MovingTool.from.x;
        int dy = mouseLocation.y - MovingTool.from.y;
        GraphPane graphPane = Tool.getGraphPane();
        Set<IVertex> vertices = graphPane.getSelectedVertices();
        for (IVertex v : vertices) {
            graphPane.getGraphModel().moveVertex(v, dx, dy);
        }
        MovingTool.from = mouseLocation;
        graphPane.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        Composite c = new Composite();
        Point mouseLocation = e.getPoint();
        int dx = mouseLocation.x - MovingTool.veryFrom.x;
        int dy = mouseLocation.y - MovingTool.veryFrom.y;
        IGraphPane graphPane = Tool.getGraphPane();
        Set<IVertex> vertices = graphPane.getSelectedVertices();
        for (IVertex v : vertices) {
            c.addCommand(new MoveVertexCommand(graphPane.getGraphModel(), v, dx, dy));
        }
        graphPane.setTool(SelectingTool.getInstance());
    }

    public void paint(Graphics g) {           
    }
}
