package org.openrsc.editor;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class Actions {
    // Open Landscape.
    public static void onOpenLandscape(File file) {
        Util.ourFile = file;
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
        Util.copiedTile = Util.selectedTile;
    }

    public static void onPaste() {
        if (Util.copiedTile != null) {
            Util.selectedTile.setDiagonalWalls(Util.copiedTile.getDiagonalWalls());
            Util.selectedTile.setVerticalWall(Util.copiedTile.getVerticalWall());
            Util.selectedTile.setHorizontalWall(Util.copiedTile.getHorizontalWall());
            Util.selectedTile.setGroundElevation(Util.copiedTile.getGroundElevation());
            Util.selectedTile.setGroundTexture(Util.copiedTile.getGroundTexture());
            Util.selectedTile.setRoofTexture(Util.copiedTile.getRoofTexture());
            Util.selectedTile.setGroundOverlay(Util.copiedTile.getGroundOverlay());
            Util.STATE = Util.State.TILE_NEEDS_UPDATING;
        }
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

    public static CompletableFuture<Boolean> toggleShowNpcs() {
        return attemptUpdate(Util::toggleShowNpcs).thenApply(unused -> Util.showNpcs);
    }

    public static CompletableFuture<Void> attemptUpdate(Runnable task) {
        return CompletableFuture.runAsync(() -> {
            if (Util.STATE == Util.State.RENDER_READY) {
                task.run();
                Util.STATE = Util.State.FORCE_FULL_RENDER;
            }
        });
    }

    public static CompletableFuture<Boolean> toggleShowRoofs() {
        return attemptUpdate(Util::toggleShowRoofs).thenApply(unused -> Util.showRoofs);
    }

    public static void changeSectorHeight(SectorHeight sectorHeight) {
        int converted = sectorHeight.ordinal();
        if (Util.sectorH != converted && Util.STATE == Util.State.RENDER_READY) {
            Util.sectorH = converted;
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
        if (Util.sectorChanged
                && Util.tileArchive != null
                && JOptionPane.showConfirmDialog(
                null,
                "Changes have been made to this Section\r\nDo you wish to save the current map?", "Saving", 0) == 0) {
            if (Util.save()) {
                Util.sectorChanged = false;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to saved to " + Util.ourFile.getPath()
                );
            }
        } else {
            Util.sectorChanged = false;
        }
    }
}
