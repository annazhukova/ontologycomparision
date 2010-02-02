/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.FileChoosers;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;

public class Exit extends AbstractAction {
    private static final Exit INSTANCE = new Exit();
    private static JFrame frame;

    public static Exit getInstance() {
        return Exit.INSTANCE;
    }

    private Exit() {
        super("Exit");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('e'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        confirmExit();
    }

    public static void confirmExit() {
        int save = JOptionPane.showConfirmDialog(Exit.frame, "Save changes before exiting?");
        if (save == JOptionPane.OK_OPTION) {
            FileChoosers.getSaveFileChooser(true);
        } else if (save == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    public static void setFrame(JFrame frame) {
        Exit.frame = frame;
    }
}
