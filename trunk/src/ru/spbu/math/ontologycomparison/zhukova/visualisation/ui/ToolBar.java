package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.menuactions.*;

import javax.swing.*;

/**
 * @author Anna R. Zhukova
 */
/*package*/ class ToolBar {
    private static JToolBar toolBar;

    /*package*/
    static JToolBar getToolBar() {
        if (ToolBar.toolBar == null) {
            ToolBar.toolBar = new JToolBar();
            ToolBar.toolBar.add(Open.getInstance());
        }
        return ToolBar.toolBar;
    }
}
