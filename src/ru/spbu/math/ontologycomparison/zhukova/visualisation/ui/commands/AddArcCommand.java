package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.commands;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.Arc;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;


public class AddArcCommand implements ICommand {
    private final IGraphModel graphModel;
    private final Arc arc;

    public AddArcCommand(IGraphModel gr, Arc arc) {
        this.graphModel = gr;
        this.arc = arc;
    }

    public void doIt() {
        this.graphModel.addArc(this.arc);
    }

    public void undo() {
        this.graphModel.removeArc(this.arc);
    }
}
