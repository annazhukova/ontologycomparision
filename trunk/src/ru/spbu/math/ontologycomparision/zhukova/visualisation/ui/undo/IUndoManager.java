package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.commands.ICommand;

/**
 * @author Anna R. Zhukova
 */
public interface IUndoManager {
    
    void execute(ICommand command, boolean needJoin, boolean doIt);

    void undo();

    void update();

    void redo();
}
