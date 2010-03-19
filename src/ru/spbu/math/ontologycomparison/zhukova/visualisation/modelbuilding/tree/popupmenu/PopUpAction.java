package ru.spbu.math.ontologycomparison.zhukova.visualisation.modelbuilding.tree.popupmenu;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Anna Zhukova
 */
/*package*/ class PopUpAction extends AbstractAction {

    private final PopUpMenu popUpMenu;
    private final boolean select;
    private final int selectionMode;
    private final boolean hide;

    public PopUpAction(String name, PopUpMenu popUpMenu, boolean select, int selectionMode, boolean hide) {
        super(name);
        this.popUpMenu = popUpMenu;
        this.select = select;
        this.selectionMode = selectionMode;
        this.hide = hide;
    }

    public void actionPerformed(ActionEvent e) {
        popUpMenu.visitRow(select, selectionMode, hide);
    }
}
