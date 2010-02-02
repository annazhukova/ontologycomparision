package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.commands;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.IGraphModel;


public class MoveVertexCommand implements ICommand {
    private final IGraphModel graphModel;
    private final IVertex vertex;
    private final int dX;
    private final int dY;

    public MoveVertexCommand(IGraphModel graphModel, IVertex classVertex, int x, int y){
        this.graphModel = graphModel;
        this.vertex = classVertex;
        this.dX = x;
        this.dY = y;
    }

    public void doIt() {
        this.graphModel.moveVertex(this.vertex, this.dX, this.dY);
    }

    public void undo() {
        this.graphModel.moveVertex(this.vertex, -this.dX, -this.dY);
    }
}
