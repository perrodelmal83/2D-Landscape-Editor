package org.openrsc.editor.gui.controls;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainPresetSelectedEvent;
import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Dimension;
import java.util.Optional;

public class TerrainPropertySlider extends JPanel {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    private final TerrainProperty terrainProperty;
    private final JSlider slider;
    private final JLabel label;
    private final JButton removeButton;
    private TerrainTemplate currentTemplate;
    private boolean dropNextEvent = false;

    public TerrainPropertySlider(TerrainProperty terrainProperty) {
        super();
        eventBus.register(this);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.terrainProperty = terrainProperty;

        label = new JLabel();
        label.setPreferredSize(new Dimension(150, 15));
        label.setText(terrainProperty.getLabel());
        label.setVisible(true);
        add(label);

        slider = new JSlider();
        slider.setName("test");
        slider.setVisible(true);
        slider.setValue(0);
        slider.setMinorTickSpacing(5);
        slider.setMaximum(254);
        slider.setPaintTicks(true);
        slider.setMinimum(0);
        slider.addChangeListener(evt -> {
            // Check that this event is a result of user sliding the item
            if (evt.getSource() == slider && !dropNextEvent) {
                TerrainTemplate.TerrainTemplateBuilder builder =
                        this.currentTemplate != null ? this.currentTemplate.toBuilder() : TerrainTemplate.builder();
                eventBus.post(
                        new TerrainTemplateUpdateEvent(
                                builder
                                        .value(this.terrainProperty, slider.getValue())
                                        .build()
                        )
                );
                label.setText(getPropertyLabel(slider.getValue()));
            }
            dropNextEvent = false;
        });
        add(slider);

        removeButton = new JButton("X");
        removeButton.addActionListener(evt -> {
            TerrainTemplate.TerrainTemplateBuilder builder =
                    this.currentTemplate != null ? this.currentTemplate.toBuilder() : TerrainTemplate.builder();
            eventBus.post(
                    new TerrainTemplateUpdateEvent(
                            builder.value(this.terrainProperty, null).build()
                    )
            );
            setSlider(0);
        });
        add(removeButton);
    }

    private String getPropertyLabel(Integer value) {
        return value != null ? terrainProperty.getLabel() + " (" + value + ")" : terrainProperty.getLabel();
    }

    private void setSlider(Integer value) {
        dropNextEvent = true;
        slider.setValue(Optional.ofNullable(value).orElseGet(() -> 0));
        label.setText(getPropertyLabel(value));
    }

    @Subscribe
    public void onTerrainPresetSelected(TerrainPresetSelectedEvent event) {
        currentTemplate = event.getTemplate();
        Integer value = currentTemplate.getValues().get(terrainProperty);
        setSlider(value);
    }

    @Subscribe
    public void onTerrainTemplateUpdate(TerrainTemplateUpdateEvent event) {
        currentTemplate = TemplateUtil.merge(currentTemplate, event);
    }
}
