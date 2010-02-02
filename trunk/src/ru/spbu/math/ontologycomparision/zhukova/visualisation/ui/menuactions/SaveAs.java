/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.FileChoosers;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;

public class SaveAs extends AbstractAction {
    private static final SaveAs INSTANCE = new SaveAs();

    public static SaveAs getInstance() {
        return SaveAs.INSTANCE;
    }

    private SaveAs() {
        super("Save as...");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('s'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        FileChoosers.getSaveFileChooser(false);
    }
}
