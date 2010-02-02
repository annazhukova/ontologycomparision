package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.*;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo.UndoManager;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo.IUndoManager;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools.ToolTipMouseMotionListener;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools.Tool;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools.SelectingTool;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.graphpane.tools.ITool;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphPane extends JPanel implements IGraphPane {
    private Set<IVertex> selectedVertices = new HashSet<IVertex>();
    private ITool currentTool = SelectingTool.getInstance();
    private GraphModel graphModel = new GraphModel(this);

    private class MyObserver implements Observer {

        public void update(Observable o, Object arg) {
            if (arg != null) {
                IVertex v = (IVertex) arg;
                for (IVertex selectedVertex : GraphPane.this.selectedVertices) {
                    if (selectedVertex.equals(v)) {
                        GraphPane.this.selectedVertices.remove(v);
                        break;
                    }
                }
            }
            repaint();
        }
    }

    public GraphPane() {
        super();
        setLayout(new BorderLayout());
        setSize(750, 750);
        setPreferredSize(new Dimension(750, 750));
        setBackground(Color.WHITE);
        setFocusable(true);
        addMouseMotionListener(new ToolTipMouseMotionListener(this));
        Tool.setGraphModel(this, new UndoManager());
        addMouseListener(this.currentTool);
        addMouseMotionListener(this.currentTool);
    }


    public void checkPoint(Point p) {
        int width = getWidth();
        int height = getHeight();
        if (p.x >= width) {
            width = p.x + 10;
        }
        if (p.y >= height) {
            height = p.y + 10;
        }
        Dimension d = new Dimension(width, height);
        setSize(d);
        setPreferredSize(d);
    }

    public void deselectVertices() {
        this.selectedVertices = new HashSet<IVertex>();
    }

    public void selectVertex(IVertex v) {
        this.selectedVertices.add(v);
    }

    public void setGraphModel(GraphModel gr, IUndoManager undoManager) {
        deselectVertices();
        this.graphModel = gr;
        this.graphModel.addObserver(new MyObserver());
        Tool.setGraphModel(this, undoManager);
        revalidate();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.graphModel != null) {
            for (IArc arc : this.graphModel.getArcs()) {
                arc.paintIfNeeded(g);
            }
            List<SimpleVertex> vertices = new LinkedList<SimpleVertex>(this.graphModel.getSimpleVertices());
            for (ListIterator<SimpleVertex> it = vertices.listIterator(vertices.size()); it.hasPrevious(); ) {
                IVertex v = it.previous();
                v.paintIfNeeded(g, this, this.selectedVertices.contains(v));
            }
            List<SuperVertex> svertices = new LinkedList<SuperVertex>(this.graphModel.getSuperVertices());
            for (ListIterator<SuperVertex> it = svertices.listIterator(svertices.size()); it.hasPrevious(); ) {
                IVertex v = it.previous();
                v.paintIfNeeded(g, this, this.selectedVertices.contains(v));
            }
            this.currentTool.paint(g);
        }
    }

    public IGraphModel getGraphModel() {
        return this.graphModel;
    }

    public Set<IVertex> getSelectedVertices() {
        return Collections.unmodifiableSet(this.selectedVertices);
    }

    public void setTool(Tool tool) {
        removeMouseListener(this.currentTool);
        removeMouseMotionListener(this.currentTool);
        this.currentTool = tool;
        addMouseListener(this.currentTool);
        addMouseMotionListener(this.currentTool);
        repaint();
    }
}
