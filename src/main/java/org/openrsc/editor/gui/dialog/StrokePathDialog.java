package org.openrsc.editor.gui.dialog;

import org.openrsc.editor.model.configuration.StrokePathConfiguration;

import javax.swing.JFrame;
import java.awt.HeadlessException;
import java.util.function.Consumer;

public class StrokePathDialog extends JFrame {
    public StrokePathDialog(Consumer<StrokePathConfiguration> onComplete) throws HeadlessException {
        new SelectWallDialog(
                (wallDef) -> onComplete.accept(
                        StrokePathConfiguration.builder()
                                .wallDefinition(wallDef)
                                .build()
                )
        );
    }
}
