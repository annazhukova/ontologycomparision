package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.commands;

import java.util.LinkedList;
import java.util.Iterator;

public class Composite implements ICommand {
    private final LinkedList<ICommand> commands;

    public Composite(){
        this.commands = new LinkedList<ICommand>();
    }

    public void addCommand(ICommand c){
        this.commands.add(c);
    }

    public void doIt() {
        for (ICommand cmd : this.commands) {
            cmd.doIt();
        }
    }

    public void undo() {
        for (Iterator<ICommand> it = this.commands.descendingIterator(); it.hasNext(); ) {
            ICommand cmd = it.next();
            cmd.undo();
        }
    }
}
