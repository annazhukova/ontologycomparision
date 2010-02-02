package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.commands.ICommand;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.commands.Composite;

import java.util.*;

public class UndoManager implements IUndoManager {
    private boolean needJoin = false;
    private final List<ICommand> undoList = new LinkedList<ICommand>();
    private final List<ICommand> redoList = new LinkedList<ICommand>();

    public void execute(ICommand command, boolean needJoin, boolean doIt) {
        this.redoList.clear();
        if (this.undoList.isEmpty() || !this.needJoin) {
            this.undoList.add(command);
        } else {
            Composite composite = new Composite();
            composite.addCommand(this.undoList.remove(this.undoList.size() - 1));
            composite.addCommand(command);
            this.undoList.add(composite);
        }
        this.needJoin = needJoin;
        if (doIt) {
            command.doIt();
        }
    }

    public void undo() {
        if (!this.undoList.isEmpty()) {
            ICommand c = this.undoList.remove(this.undoList.size() - 1);
            this.redoList.add(c);
            c.undo();
        }
    }

    public void update() {
        this.needJoin = false;
    }

    public void redo() {
        if (!this.redoList.isEmpty()) {
            ICommand c = this.redoList.remove(this.redoList.size() - 1);
            this.undoList.add(c);
            c.doIt();
        }
    }
}
