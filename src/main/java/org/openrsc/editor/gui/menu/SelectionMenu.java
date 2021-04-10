package org.openrsc.editor.gui.menu;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.action.CreateBuildingAction;
import org.openrsc.editor.event.action.GenerateLandscapeAction;
import org.openrsc.editor.event.selection.SelectRegionUpdateEvent;
import org.openrsc.editor.gui.dialog.CreateBuildingDialog;
import org.openrsc.editor.gui.dialog.GenerateLandscapeDialog;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.SelectRegion;

import javax.swing.JMenuItem;

public class SelectionMenu extends BaseMenu {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    public SelectRegion selectRegion;

    public SelectionMenu() {
        super("Selection");
        eventBus.register(this);

        setEnabled(false);

        JMenuItem createBuilding = new JMenuItem();
        createBuilding.setText("Create Building");
        createBuilding.addActionListener((evt) -> {
            new CreateBuildingDialog(
                    (configuration) -> eventBus.post(
                            CreateBuildingAction.builder()
                                    .selectRegion(selectRegion)
                                    .configuration(configuration)
                                    .build()
                    ),
                    () -> {/* Do nothing, was canceled*/}
            );
        });
        add(createBuilding);

        JMenuItem generateLandscape = new JMenuItem();
        generateLandscape.setText("Generate Landscape");
        generateLandscape.addActionListener(evt ->
                new GenerateLandscapeDialog(
                        configuration -> eventBus.post(
                                GenerateLandscapeAction.builder()
                                        .configuration(configuration)
                                        .selectRegion(selectRegion)
                                        .build()
                        )
                )
        );
        add(generateLandscape);
    }

    @Subscribe
    public void onRegionSelected(SelectRegionUpdateEvent evt) {
        setEnabled(evt.isSelectionPresent());
        this.selectRegion = evt.getSelectRegion();
    }

    @Subscribe
    public void onToolSelected(EditorToolSelectedEvent evt) {
        setEnabled(
                evt.getEditorTool() == EditorTool.SELECT
                        && this.selectRegion != null
                        && !this.selectRegion.getPoints().isEmpty()
        );
    }
}
