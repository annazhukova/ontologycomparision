package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import javax.swing.*;

/**
 * @author Anna R. Zhukova
 */
/*package*/ class ToolBar {
    private static JToolBar toolBar;

    /*package*/

    static JToolBar getToolBar(AbstractAction... actions) {
        if (ToolBar.toolBar == null) {
            ToolBar.toolBar = new JToolBar();
            for (AbstractAction action : actions) {
                ToolBar.toolBar.add(action);
            }
        }
        return ToolBar.toolBar;
    }
}
