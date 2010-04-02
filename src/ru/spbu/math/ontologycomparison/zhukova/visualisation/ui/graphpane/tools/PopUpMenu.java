package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SuperVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.popupmenu.IRepaintListener;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Anna Zhukova
 */
public class PopUpMenu extends JPopupMenu implements IRepaintListener {
    private GraphPane graphPane;
    private SuperVertex[] vertices;

    public PopUpMenu() {
        super();
        add(new AbstractAction("remove mapping") {
            public void actionPerformed(ActionEvent e) {
                for (SuperVertex vertex : vertices) {
                  graphPane.getGraphModel().removeSuperVertex(vertex);
                }
            }
        });
    }

    public void setGraphPane(GraphPane graphPane) {
        this.graphPane = graphPane;
    }

    public GraphPane getGraphPane() {
        return graphPane;
    }

    public void setVertices(SuperVertex... vertices) {
        this.vertices = vertices;
    }

    public void update() {
    }
}

