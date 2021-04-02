package org.openrsc.editor.gui.menu;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.SelectRegionUpdateEvent;
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
        createBuilding.addActionListener((evt) -> System.out.println("Create building"));
        add(createBuilding);
    }

    @Subscribe
    public void onRegionSelected(SelectRegionUpdateEvent evt) {
        setEnabled(evt.isSelectionPresent());
        this.selectRegion = evt.getSelectRegion();
    }
}
