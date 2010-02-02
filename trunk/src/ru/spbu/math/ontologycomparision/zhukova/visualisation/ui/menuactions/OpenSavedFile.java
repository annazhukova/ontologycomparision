/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.FileChoosers;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;

public class OpenSavedFile extends AbstractAction {
    private static final OpenSavedFile INSTANCE = new OpenSavedFile();

    public static OpenSavedFile getInstance() {
        return OpenSavedFile.INSTANCE;
    }

    private OpenSavedFile() {
        super("Open saved file...");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('i'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        FileChoosers.getOpenSavedFileChooser();
    }
}
