package org.openrsc.editor.gui.menu;

import org.openrsc.editor.Util;
import org.openrsc.editor.gui.GuiUtils;
import org.openrsc.editor.gui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class AdvancedMenu extends BaseMenu {
    public AdvancedMenu(
            Runnable onShowUnderground,
            Runnable onShowGroundLevel,
            Runnable onShowUpstairs,
            Runnable onShowSecondStory,
            Runnable onJumpToCoords,
            Supplier<CompletableFuture<Boolean>> toggleShowRoofs,
            Supplier<CompletableFuture<Boolean>> toggleShowNpcs
    ) {
        super("Advanced");

        JMenuItem showUnderground = new JMenuItem();
        showUnderground.setText("Show Underground");
        showUnderground.addActionListener(GuiUtils.fromRunnable(onShowUnderground));
        add(showUnderground);

        JMenuItem showGroundLevel = new JMenuItem();
        showGroundLevel.setText("Show Ground Level");
        showGroundLevel.addActionListener(GuiUtils.fromRunnable(onShowGroundLevel));
        add(showGroundLevel);

        JMenuItem showUpstairs = new JMenuItem();
        showUpstairs.setText("Show Upstairs");
        showUpstairs.addActionListener(GuiUtils.fromRunnable(onShowUpstairs));
        add(showUpstairs);

        JMenuItem show2ndStory = new JMenuItem();
        show2ndStory.setText("Show 2nd Story");
        show2ndStory.addActionListener(GuiUtils.fromRunnable(onShowSecondStory));
        add(show2ndStory);

        JMenuItem jumpToCoordinates = new JMenuItem();
        jumpToCoordinates.setText("Jump to Coordinates");
        jumpToCoordinates.addActionListener(GuiUtils.fromRunnable(onJumpToCoords));
        add(jumpToCoordinates);

        JMenuItem toggleRoofs = new JMenuItem();
        toggleRoofs.setText("Toggle Roofs");
        toggleRoofs.addActionListener(evt -> toggleShowRoofs.get().thenAccept(
                showRoofs -> toggleRoofs.setText(
                        (showRoofs ? "Hide" : "Show") + " Roofs"
                )
        ));
        add(toggleRoofs);

        JMenuItem toggleNpcs = new JMenuItem();
        toggleNpcs.setText("Toggle Npcs/Objects/Items");
        toggleNpcs.addActionListener(evt -> toggleShowNpcs.get().thenAccept(
                showNpcs -> toggleNpcs.setText(
                        (showNpcs ? "Hide" : "Show") + " Npcs/Objects/Items"
                )
        ));
        add(toggleNpcs);

        JMenuItem toggleInfo = new JMenuItem();
        toggleInfo.setText("Toggle Tile Info");
        toggleInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Util.STATE == Util.State.RENDER_READY && !MainWindow.tile.getText().equals("")) {
                    if (!Util.toggleInfo) {
                        Util.toggleInfo = true;
                        MainWindow.tile.setText("");
                        MainWindow.elevation.setText("");
                        MainWindow.overlay.setText("");
                        MainWindow.roofTexture.setText("");
                        MainWindow.groundtexture.setText("");
                        MainWindow.diagonalWall.setText("");
                        MainWindow.verticalWall.setText("");
                        MainWindow.horizontalWall.setText("");
                        Util.updateText(Util.selectedTile);
                    } else {
                        Util.toggleInfo = false;
                        MainWindow.tile.setText("");
                        MainWindow.elevation.setText("");
                        MainWindow.overlay.setText("");
                        MainWindow.roofTexture.setText("");
                        MainWindow.groundtexture.setText("");
                        MainWindow.diagonalWall.setText("");
                        MainWindow.verticalWall.setText("");
                        MainWindow.horizontalWall.setText("");
                        Util.updateText(Util.selectedTile);
                    }
                }

            }
        });
        add(toggleInfo);
    }
}
