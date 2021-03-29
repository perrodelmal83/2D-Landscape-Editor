package org.openrsc.editor.gui.menu;

import org.openrsc.editor.gui.GuiUtils;

import javax.swing.*;

public class FileMenu extends BaseMenu {

    public FileMenu(
            Runnable onOpenLandscape,
            Runnable onOpenSection,
            Runnable onSaveLandscape,
            Runnable onRevertLandscape,
            Runnable onExit
    ) {
        super("File");

        JMenuItem openLandscape = new JMenuItem();
        openLandscape.setText("Open Landscape");
        openLandscape.addActionListener(GuiUtils.fromRunnable(onOpenLandscape));
        add(openLandscape);

        JMenuItem openSection = new JMenuItem();
        openSection.setText("Open Section");
        openSection.addActionListener(GuiUtils.fromRunnable(onOpenSection));
        add(openSection);

        JMenuItem saveLandscape = new JMenuItem();
        saveLandscape.setText("Save Landscape");
        saveLandscape.addActionListener(GuiUtils.fromRunnable(onSaveLandscape));
        add(saveLandscape);

        JMenuItem revertLandscape = new JMenuItem();
        revertLandscape.setText("Revert Landscape");
        revertLandscape.addActionListener(GuiUtils.fromRunnable(onRevertLandscape));
        add(revertLandscape);

        JMenuItem exit = new JMenuItem();
        exit.setText("Exit");
        exit.addActionListener(GuiUtils.fromRunnable(onExit));
        add(exit);
    }
}
