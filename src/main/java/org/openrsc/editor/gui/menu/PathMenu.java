package org.openrsc.editor.gui.menu;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.SelectPathUpdateEvent;
import org.openrsc.editor.event.action.ClearPathAction;
import org.openrsc.editor.event.action.ConvertPathToSelectionAction;
import org.openrsc.editor.event.action.StrokePathAction;
import org.openrsc.editor.gui.dialog.StrokePathDialog;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.SelectPath;

import javax.swing.JMenuItem;

public class PathMenu extends BaseMenu {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    public SelectPath selectPath;

    public PathMenu() {
        super("Path");
        eventBus.register(this);

        setEnabled(false);

        JMenuItem clearPath = new JMenuItem();
        clearPath.setText("Clear Path");
        clearPath.addActionListener((evt) -> eventBus.post(new ClearPathAction()));
        add(clearPath);

        JMenuItem convertToSelection = new JMenuItem();
        convertToSelection.setText("Convert Path to Selection");
        convertToSelection.addActionListener((evt) -> eventBus.post(
                new ConvertPathToSelectionAction(selectPath.getPoints())
        ));
        add(convertToSelection);

        JMenuItem strokePath = new JMenuItem();
        strokePath.setText("Stroke Path");
        strokePath.addActionListener(evt -> new StrokePathDialog(
                (configuration) -> eventBus.post(new StrokePathAction(
                        selectPath,
                        configuration
                ))
        ));
        add(strokePath);
    }

    @Subscribe
    public void onPathUpdated(SelectPathUpdateEvent evt) {
        setEnabled(evt.isPresent());
        this.selectPath = evt.getSelectPath();
    }

    @Subscribe
    public void onToolSelected(EditorToolSelectedEvent evt) {
        setEnabled(
                evt.getEditorTool() == EditorTool.DRAW_PATH
                        && this.selectPath != null
                        && !this.selectPath.getPoints().isEmpty()
        );
    }
}
