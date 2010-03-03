package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.commands;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;


public class AddVertexCommand implements ICommand {
    private final IGraphModel graphModel;
    private final SimpleVertex simpleVertex;

    public AddVertexCommand(IGraphModel gr, SimpleVertex v){
        this.graphModel = gr;
        this.simpleVertex = v;
    }

    public void doIt() {
        this.graphModel.addVertex(this.simpleVertex);
    }

    public void undo() {
        this.graphModel.removeVertex(this.simpleVertex);
    }
}
