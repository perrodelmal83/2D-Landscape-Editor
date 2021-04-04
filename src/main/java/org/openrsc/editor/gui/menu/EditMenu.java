package org.openrsc.editor.gui.menu;

import com.google.common.eventbus.EventBus;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.action.GenerateElevationAction;
import org.openrsc.editor.gui.GuiUtils;

import javax.swing.JMenuItem;

public class EditMenu extends BaseMenu {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

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

        JMenuItem generateElevation = new JMenuItem("Generate Elevation");
        generateElevation.setEnabled(true);
        generateElevation.addActionListener((evt) -> eventBus.post(new GenerateElevationAction()));
        add(generateElevation);
    }
}
