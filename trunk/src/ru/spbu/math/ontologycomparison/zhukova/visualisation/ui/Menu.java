/**
 * @author Anna R. Zhukova
 */
package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions.*;

import javax.swing.*;

/*package*/ class Menu {
    private static JMenuBar menuBar;
    private static JMenu menuFile;
    private static JMenu menuHelp;
    private static JMenu menuEdit;

    /*package*/
    static JMenuBar getMenuBar() {
        if (Menu.menuBar == null) {
            Menu.menuBar = new JMenuBar();
            Menu.menuBar.add(getMenuFile());
        }
        return Menu.menuBar;
    }

    private static JMenu getMenuFile() {
        if (Menu.menuFile == null) {
            Menu.menuFile = new JMenu("File");
            Menu.menuFile.setMnemonic('f');
            Menu.menuFile.add(Open.getInstance());
        }
        return Menu.menuFile;
    }

    private static JMenu getMenuHelp() {
        if (Menu.menuHelp == null) {
            Menu.menuHelp = new JMenu("Help");
            Menu.menuHelp.setMnemonic('h');
        }
        return Menu.menuHelp;
    }

    private static JMenu getMenuEdit() {
        if (Menu.menuEdit == null) {
            Menu.menuEdit = new JMenu("Edit");
            Menu.menuEdit.setMnemonic('d');
        }
        return Menu.menuEdit;
    }
}
