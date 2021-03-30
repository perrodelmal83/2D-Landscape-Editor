package org.openrsc.editor.gui.menu;

import org.openrsc.editor.gui.GuiUtils;

import javax.swing.*;

public class BrushMenu extends BaseMenu {
    public BrushMenu(
            Runnable onCreateBrush,
            Runnable onDeleteBrush,
            Runnable onSaveBrush,
            Runnable onModifyBrush
    ) {
        super("Brush");

        JMenuItem createBrush = new JMenuItem();
        createBrush.setText("Create Brush");
        createBrush.addActionListener(GuiUtils.fromRunnable(onCreateBrush));
        add(createBrush);

        JMenuItem deleteBrush = new JMenuItem();
        deleteBrush.setText("Delete Brush");
        deleteBrush.addActionListener(GuiUtils.fromRunnable(onDeleteBrush));
        add(deleteBrush);

        JMenuItem saveBrush = new JMenuItem();
        saveBrush.setText("Save Brush");
        saveBrush.addActionListener(GuiUtils.fromRunnable(onSaveBrush));
        add(saveBrush);

        JMenuItem modifyBrush = new JMenuItem();
        modifyBrush.setText("Modify Brush");
        modifyBrush.addActionListener(GuiUtils.fromRunnable(onModifyBrush));
        add(modifyBrush);
    }
}
