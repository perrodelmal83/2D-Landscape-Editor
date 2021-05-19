package org.openrsc.editor;

import org.openrsc.editor.gui.MainWindow;

import java.awt.EventQueue;

public class Main {
    public static MainWindow mainWindow;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> mainWindow = new MainWindow());
    }
}
