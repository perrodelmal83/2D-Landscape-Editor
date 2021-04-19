package org.openrsc.editor.gui.dialog;

import org.openrsc.editor.model.configuration.StrokePathConfiguration;
import org.openrsc.editor.model.definition.WallType;

import javax.swing.JFrame;
import java.awt.HeadlessException;
import java.util.Collections;
import java.util.function.Consumer;

public class StrokePathDialog extends JFrame {
    public StrokePathDialog(Consumer<StrokePathConfiguration> onComplete) throws HeadlessException {
        new SelectWallDialog(
                Collections.singleton(WallType.WALL),
                (wallDef) -> onComplete.accept(
                        StrokePathConfiguration.builder()
                                .wallDefinition(wallDef)
                                .build()
                )
        );
    }
}
