package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.model.EditorTool;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@Getter
public abstract class ToolDelegate implements MouseListener, MouseMotionListener {
    private static EventBus eventBus = EventBusFactory.getEventBus();

    private final EditorTool tool;

    public ToolDelegate(EditorTool tool) {
        this.tool = tool;
        eventBus.register(this);
    }

    public abstract void render(Graphics2D g);

    public abstract void onToolMount();

    public abstract void onToolUnmount();

    @Subscribe
    private void onEditorToolSelected(EditorToolSelectedEvent evt) {
        if (!tool.equals(evt.getEditorTool())) {
            this.onToolUnmount();
        } else {
            this.onToolMount();
        }
    }
}
