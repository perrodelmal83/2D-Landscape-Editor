package org.openrsc.editor.gui.menu;

import org.openrsc.editor.Actions;

import javax.swing.*;

public class EditorMenuBar extends JMenuBar {

    public EditorMenuBar() {
        super();
        this.add(
                new FileMenu(
                        Actions::onOpenLandscape,
                        Actions::onOpenSection,
                        Actions::onSaveLandscape,
                        Actions::onRevertLandscape,
                        Actions::onExit
                )
        );

        this.add(
                new EditMenu(
                        Actions::onUndo,
                        Actions::onCopy,
                        Actions::onPaste
                )
        );

        this.add(
                new AdvancedMenu(
                        Actions::onShowUnderground,
                        Actions::onShowGroundLevel,
                        Actions::onShowUpstairs,
                        Actions::onShowSecondStory,
                        Actions::onJumpToCoords,
                        Actions::toggleShowRoofs,
                        Actions::toggleShowNpcs
                )
        );
    }
}
