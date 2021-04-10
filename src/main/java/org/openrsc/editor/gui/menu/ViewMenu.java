package org.openrsc.editor.gui.menu;

import com.google.common.eventbus.EventBus;
import org.openrsc.editor.event.DisplayConfigurationUpdateEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.gui.GuiUtils;
import org.openrsc.editor.gui.ImageUtils;
import org.openrsc.editor.model.configuration.DisplayConfiguration;
import org.openrsc.editor.model.configuration.DisplayConfigurationProperty;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import java.util.Arrays;

import static org.openrsc.editor.model.configuration.DisplayConfiguration.DEFAULT_DISPLAY_CONFIGURATION;

public class ViewMenu extends BaseMenu {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    public ImageIcon checkMark;
    public DisplayConfiguration displayConfiguration;

    public ViewMenu(
            Runnable onShowUnderground,
            Runnable onShowGroundLevel,
            Runnable onShowUpstairs,
            Runnable onShowSecondStory,
            Runnable onJumpToCoords
    ) {
        super("View");

        this.displayConfiguration = DEFAULT_DISPLAY_CONFIGURATION;

        String resourcePath = "/icons/check.png";
        try {
            this.checkMark = new ImageIcon(ImageUtils.getIconImage(resourcePath, 15));
        } catch (Exception e) {
            System.err.println("Unable to load checkmark image from " + resourcePath);
        }

        JMenuItem showUnderground = new JMenuItem();
        showUnderground.setText("View Underground");
        showUnderground.addActionListener(GuiUtils.fromRunnable(onShowUnderground));
        add(showUnderground);

        JMenuItem showGroundLevel = new JMenuItem();
        showGroundLevel.setText("View Ground Level");
        showGroundLevel.addActionListener(GuiUtils.fromRunnable(onShowGroundLevel));
        add(showGroundLevel);

        JMenuItem showUpstairs = new JMenuItem();
        showUpstairs.setText("View Upstairs");
        showUpstairs.addActionListener(GuiUtils.fromRunnable(onShowUpstairs));
        add(showUpstairs);

        JMenuItem show2ndStory = new JMenuItem();
        show2ndStory.setText("View 2nd Story");
        show2ndStory.addActionListener(GuiUtils.fromRunnable(onShowSecondStory));
        add(show2ndStory);

        addSeparator();

        JMenuItem jumpToCoordinates = new JMenuItem();
        jumpToCoordinates.setText("Jump to Coordinates");
        jumpToCoordinates.addActionListener(GuiUtils.fromRunnable(onJumpToCoords));
        add(jumpToCoordinates);

        addSeparator();

        Arrays.stream(DisplayConfigurationProperty.values())
                .forEach(
                        property -> add(getDisplayConfigMenuItem(property))
                );
    }

    public JMenuItem getDisplayConfigMenuItem(DisplayConfigurationProperty property) {
        JMenuItem item = new JMenuItem();
        item.setIcon(checkMarkIf(property.isDefaultValue()));
        item.setText(property.getLabel());
        item.addActionListener(evt -> item.setIcon(
                checkMarkIf(
                        toggleDisplayConfigurationProperty(property)
                )
        ));
        return item;
    }

    private boolean toggleDisplayConfigurationProperty(DisplayConfigurationProperty propName) {
        boolean invertedPropValue = !this.displayConfiguration.getProperties().get(propName);
        eventBus.post(
                DisplayConfigurationUpdateEvent.builder().updatedProperty(propName, invertedPropValue).build()
        );
        this.displayConfiguration = this.displayConfiguration.toBuilder().property(propName, invertedPropValue).build();
        return invertedPropValue;
    }

    public ImageIcon checkMarkIf(boolean condition) {
        return condition ? checkMark : null;
    }
}
