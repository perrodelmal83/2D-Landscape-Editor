package org.openrsc.editor.gui.menu;

import javax.swing.*;

public class BaseMenu extends JMenu {
    public BaseMenu(String name) {
        super(name);
        getPopupMenu().setLightWeightPopupEnabled(false);
    }
}
