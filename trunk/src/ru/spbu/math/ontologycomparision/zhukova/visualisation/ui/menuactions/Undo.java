/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo.IUndoManager;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;

public class Undo extends AbstractAction {
    private static final Undo INSTANCE = new Undo();
    private static IUndoManager undoManager;

    public static Undo getInstance() {
        return Undo.INSTANCE;
    }

    private Undo() {
        super("Undo");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('u'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        Undo.undoManager.undo();
    }

    public static void setUndoManager(IUndoManager undoManager) {
        Undo.undoManager = undoManager;
    }
}
