package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.ui.menuactions.*;

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
            /*ToolBar.toolBar.add(OpenSavedFile.getInstance());
            ToolBar.toolBar.add(SaveAs.getInstance());*/
            //ToolBar.toolBar.add(Undo.getInstance());
            //ToolBar.toolBar.add(Redo.getInstance());
        }
        return ToolBar.toolBar;
    }
}
