package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.commands;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.Arc;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;


public class DeleteArcCommand implements ICommand {
    private final IGraphModel graphModel;
    private final Arc arc;

    public DeleteArcCommand(IGraphModel gr, Arc arc){
        this.graphModel = gr;
        this.arc = arc;
    }
    public void doIt() {
        this.graphModel.removeArc(this.arc);
    }

    public void undo() {
        this.graphModel.addArc(this.arc);
    }
}
