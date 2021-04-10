package org.openrsc.editor;

import org.openrsc.editor.gui.MoveDirection;

import javax.swing.JOptionPane;
import java.io.File;

public class Actions {
    // Open Landscape.
    public static void onOpenLandscape(File file) {
        Util.currentFile = file;
    }

    public static void onOpenSection() {
        if (Util.tileArchive == null) {
            return;
        }
        promptSaveIfChangesPresent();
        new SelectSection().setVisible(true);
    }

    public static void onSaveLandscape() {
        promptSaveIfChangesPresent();
    }

    public static void onRevertLandscape() {
        if (Util.STATE == Util.State.RENDER_READY) {
            Util.STATE = Util.State.CHANGING_SECTOR;
        }
    }

    public static void onExit() {
        System.exit(0);
    }

    public static void onUndo() {
        //TODO: Implement this functionality
    }

    public static void onCopy() {
    }

    public static void onPaste() {
    }

    public static void onShowUnderground() {
        changeSectorHeight(SectorHeight.UNDERGROUND);
    }

    public static void onShowGroundLevel() {
        changeSectorHeight(SectorHeight.GROUND_FLOOR);
    }

    public static void onShowUpstairs() {
        changeSectorHeight(SectorHeight.UPSTAIRS);
    }

    public static void onShowSecondStory() {
        changeSectorHeight(SectorHeight.SECOND_LEVEL);
    }

    public static void onJumpToCoords() {
        Util.handleJumpToCoords();
    }

    public static void changeSectorHeight(SectorHeight sectorHeight) {
        int height = sectorHeight.getHeight();
        if (Util.sectorH != height && Util.STATE == Util.State.RENDER_READY) {
            Util.sectorH = height;
            Util.STATE = Util.State.CHANGING_SECTOR;
        }
    }

    public static void onMove(MoveDirection moveDirection) {
        promptSaveIfChangesPresent();
        if (Util.STATE == Util.State.RENDER_READY) {
            switch (moveDirection) {
                case UP:
                    Util.sectorY--;
                    break;
                case DOWN:
                    Util.sectorY++;
                    break;
                case LEFT:
                    Util.sectorX++;
                    break;
                case RIGHT:
                    Util.sectorX--;
            }
            Util.STATE = Util.State.CHANGING_SECTOR;
        }
    }

    public static void promptSaveIfChangesPresent() {
        if (Util.sectorModified
                && Util.tileArchive != null
                && JOptionPane.showConfirmDialog(
                null,
                "Changes have been made to this Section\r\nDo you wish to save the current map?", "Saving", 0) == 0) {
            if (Util.save()) {
                Util.sectorModified = false;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to saved to " + Util.currentFile.getPath()
                );
            }
        } else {
            Util.sectorModified = false;
        }
    }

    public static void onOpenDataDir(File dir) {
        Util.prepareData(dir);
    }
}
