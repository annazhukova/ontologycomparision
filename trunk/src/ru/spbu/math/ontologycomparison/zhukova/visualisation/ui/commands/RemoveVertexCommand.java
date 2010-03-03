package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.commands;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.*;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.SimpleVertex;

import java.util.LinkedList;
import java.util.List;


public class RemoveVertexCommand implements ICommand {
    private final IGraphModel myGraphModel;
    private final SimpleVertex mySimpleVertex;
    private List<IArc> myArcs;

    public RemoveVertexCommand(IGraphModel gr, SimpleVertex simpleVertex){
        myGraphModel = gr;
        mySimpleVertex = simpleVertex;
        myArcs = new LinkedList<IArc>();
    }

    public void doIt() {
        myArcs = myGraphModel.removeVertex(mySimpleVertex);
    }

    public void undo() {
        myGraphModel.addVertex(mySimpleVertex);
        for (IArc arc : myArcs) {
            myGraphModel.addArc(arc);
        }
    }
}
