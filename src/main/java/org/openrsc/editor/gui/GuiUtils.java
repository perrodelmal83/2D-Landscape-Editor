package org.openrsc.editor.gui;

import java.awt.event.ActionListener;

public class GuiUtils {
    public static ActionListener fromRunnable(Runnable runnable) {
        return evt -> runnable.run();
    }
}
