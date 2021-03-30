package org.openrsc.editor.gui.menu;

import org.openrsc.editor.gui.GuiUtils;

import javax.swing.*;

public class EditMenu extends BaseMenu {
    public EditMenu(Runnable onUndo, Runnable onCopy, Runnable onPaste) {
        super("Edit");

        JMenuItem undo = new JMenuItem();
        JMenuItem copyTile = new JMenuItem();
        JMenuItem pasteTile = new JMenuItem();

        undo.setText("Undo");
        undo.addActionListener(GuiUtils.fromRunnable(onUndo));
        add(undo);

        copyTile.setText("Copy Tile");
        copyTile.addActionListener(evt -> {
            pasteTile.setEnabled(true);
            onCopy.run();
        });
        add(copyTile);

        pasteTile.setText("Paste Tile");
        pasteTile.setEnabled(false);
        pasteTile.addActionListener(GuiUtils.fromRunnable(onPaste));
        add(pasteTile);
    }
}
