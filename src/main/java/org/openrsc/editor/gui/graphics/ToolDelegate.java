package org.openrsc.editor.gui.graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.model.EditorTool;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@AllArgsConstructor
@Getter
public abstract class ToolDelegate implements MouseListener, MouseMotionListener {
    private final EditorTool tool;

    public abstract void render(Graphics2D g);

    public abstract void onToolMount();

    public abstract void onToolUnmount();

    private void onEditorToolSelected(EditorToolSelectedEvent evt) {
        if (!tool.equals(evt.getEditorTool())) {
            this.onToolUnmount();
        } else {
            this.onToolMount();
        }
    }
}
