/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.undo.IUndoManager;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;

public class Redo extends AbstractAction {
    private static final Redo INSTANCE = new Redo();
    private static IUndoManager undoManager;

    public static Redo getInstance() {
        return Redo.INSTANCE;
    }

    private Redo() {
        super("Redo");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('r'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        Redo.undoManager.redo();
    }

    public static void setUndoManager(IUndoManager undoManager) {
        Redo.undoManager = undoManager;
    }
}
