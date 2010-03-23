package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IArc;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools.ITool;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools.SelectingTool;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools.Tool;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools.ToolTipMouseMotionListener;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphPane extends JPanel implements IGraphPane, GraphModel.Listener {
    private Set<IVertex> selectedVertices = new HashSet<IVertex>();
    private ITool currentTool = SelectingTool.getInstance();
    private GraphModel graphModel = new GraphModel(this);
    private final List<SuperVertexSelectionListener> listeners = new ArrayList<SuperVertexSelectionListener>(1);

    public void addListener(SuperVertexSelectionListener listener) {
        this.listeners.add(listener);
    }

    public void update() {
        repaint();
    }

    public void vertexRemoved(IVertex vertex) {
        GraphPane.this.selectedVertices.remove(vertex);
    }

    public GraphPane() {
        super();
        setLayout(new BorderLayout());
        setSize(750, 750);
        setPreferredSize(new Dimension(750, 750));
        setBackground(Color.WHITE);
        setFocusable(true);
        addMouseMotionListener(new ToolTipMouseMotionListener(this));
        Tool.setGraphModel(this);
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
        this.selectedVertices.clear();
        for (SuperVertexSelectionListener listener : this.listeners) {
            listener.selectionCleared();
        }
    }

    public void selectVertex(IVertex v) {
        this.selectedVertices.add(v);
        if (v instanceof SuperVertex) {
            for (SuperVertexSelectionListener listener : this.listeners) {
                listener.vertexSelected(v.getToolTipText());
            }
        }
    }

    public void setGraphModel(GraphModel gr) {
        deselectVertices();
        this.graphModel = gr;
        this.graphModel.addListener(this);
        Tool.setGraphModel(this);
        revalidate();
        update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.graphModel != null) {
            for (IArc arc : this.graphModel.getArcs()) {
                arc.paintIfNeeded(g);
            }
            List<SimpleVertex> vertices = new LinkedList<SimpleVertex>(this.graphModel.getSimpleVertices());
            for (ListIterator<SimpleVertex> it = vertices.listIterator(vertices.size()); it.hasPrevious();) {
                IVertex v = it.previous();
                v.paintIfNeeded(g, this, this.selectedVertices.contains(v));
            }
            List<SuperVertex> superVertices = new LinkedList<SuperVertex>(this.graphModel.getSuperVertices());
            for (ListIterator<SuperVertex> it = superVertices.listIterator(superVertices.size()); it.hasPrevious();) {
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
        update();
    }

    public static interface SuperVertexSelectionListener {

        void selectionCleared();

        void vertexSelected(String message);
    }
}
