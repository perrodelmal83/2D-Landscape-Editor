package org.openrsc.editor.gui.menu.toolbar;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.model.EditorTool;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;

import static org.openrsc.editor.gui.ImageUtils.getIconImage;

public class ToolButton extends JButton {
    private static final int ICON_SIZE = 50;
    private static final Border DARK_GRAY_BORDER = BorderFactory.createLineBorder(Color.DARK_GRAY);
    private static final Border THICK_YELLOW_BORDER = BorderFactory.createLineBorder(Color.YELLOW, 4, false);
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    public EditorTool editorTool;

    public ToolButton(EditorTool editorTool) {
        super();
        eventBus.register(this);
        this.editorTool = editorTool;
        try {
            setIcon(new ImageIcon(
                    getIconImage(editorTool.getResource(), ICON_SIZE / 3)
            ));
        } catch (Exception e) {
            setText(editorTool.getText());
        }
        setToolTipText(editorTool.getText());
        setBorder(DARK_GRAY_BORDER);
        setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        addActionListener(evt -> {
            this.setBorder(THICK_YELLOW_BORDER);
            eventBus.post(new EditorToolSelectedEvent(this.editorTool));
        });
    }

    @Subscribe
    public void onToolSelected(EditorToolSelectedEvent editorToolSelectedEvent) {
        if (!editorToolSelectedEvent.getEditorTool().equals(this.editorTool)) {
            this.setBorder(DARK_GRAY_BORDER);
        }
    }
}
