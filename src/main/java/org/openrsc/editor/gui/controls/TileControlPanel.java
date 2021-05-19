package org.openrsc.editor.gui.controls;

import com.google.common.collect.Collections2;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainPresetSelectedEvent;
import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.model.brush.BrushOption;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TileControlPanel extends JPanel {
    private static final EventBus eventBus = EventBusFactory.getEventBus();
    private TerrainTemplate currentTemplate;

    private final Map<TerrainProperty, TerrainPropertySlider> terrainPropertySliderMap;
    private JComboBox<TerrainProperty> properties;
    private JComboBox<BrushOption> brushes;
    private JButton addTerrainProp;

    public TileControlPanel(int x, int y) {
        super();
        eventBus.register(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLocation(x, y);
        setSize(400, 500);
        setVisible(true);

        add(getPresetsPanel());
        addLabeledSlider(
                "Stamp Size (" + Util.stampSize + ")",
                Util.stampSize,
                1,
                7,
                1,
                true,
                (updatedValue) -> {
                    Util.stampSize = updatedValue;
                    return "Stamp Size (" + Util.stampSize + ")";
                }
        );
        add(new JSeparator());
        add(getAddPropertyPanel());

        this.terrainPropertySliderMap = Arrays.stream(TerrainProperty.values())
                .collect(Collectors.toMap(
                        property -> property,
                        TerrainPropertySlider::new,
                        (prop1, prop2) -> prop2,
                        TreeMap::new
                ));

        this.terrainPropertySliderMap.forEach((key, value) -> {
            add(value);
            value.setVisible(false);
        });

        add(new Box.Filler(
                new Dimension(400, 0),
                new Dimension(400, 500),
                new Dimension(400, 500)
        ));
    }

    private JPanel getAddPropertyPanel() {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
        properties = new JComboBox<>(
                Arrays.stream(TerrainProperty.values()).toArray(TerrainProperty[]::new)
        );
        addTerrainProp = new JButton("Add Terrain Property");
        addTerrainProp.addActionListener(evt -> {
            TerrainProperty property = (TerrainProperty) properties.getSelectedItem();
            eventBus.post(
                    new TerrainTemplateUpdateEvent(
                            TerrainTemplate.builder().value(property, 0).build()
                    )
            );
            brushes.setSelectedItem(BrushOption.CUSTOM);
        });
        addPanel.add(properties);
        addPanel.add(addTerrainProp);
        return addPanel;
    }

    private JPanel getPresetsPanel() {
        JPanel presetsPanel = new JPanel();
        presetsPanel.setLayout(new BoxLayout(presetsPanel, BoxLayout.X_AXIS));

        JLabel loadPresetLabel = new JLabel("Load Preset");
        loadPresetLabel.setPreferredSize(new Dimension(150, 15));
        presetsPanel.add(loadPresetLabel);

        brushes = new JComboBox<>(BrushOption.values());
        brushes.setSelectedItem(BrushOption.NONE);
        brushes.addActionListener(evt -> {
            BrushOption option = (BrushOption) brushes.getSelectedItem();
            if (option != BrushOption.CUSTOM) {
                eventBus.post(new TerrainPresetSelectedEvent(
                        Objects.requireNonNull(option).getTemplate()
                ));
            }
        });
        presetsPanel.add(brushes);
        return presetsPanel;
    }

    public void addLabeledSlider(
            String initialText,
            int initialValue,
            int min,
            int max,
            int tickSpacing,
            boolean snapToTicks,
            // Takes in the value of the slider, returns the new label
            Function<Integer, String> onChange
    ) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(150, 15));
        label.setText(initialText);
        label.setVisible(true);
        panel.add(label);

        JSlider slider = new JSlider();
        slider.setName("test");
        slider.setVisible(true);
        slider.setValue(initialValue);
        slider.setMinorTickSpacing(tickSpacing);
        slider.setMaximum(max);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(snapToTicks);
        slider.setMinimum(min);
        slider.addChangeListener(evt -> {
            label.setText(onChange.apply(slider.getValue()));
        });
        panel.add(slider);

        add(panel);
    }

    public void syncPropertySliders() {
        this.terrainPropertySliderMap.forEach((key, value) -> value.setVisible(false));

        currentTemplate.getValues()
                .keySet()
                .stream()
                .map(terrainPropertySliderMap::get)
                .forEach(slider -> slider.setVisible(true));

        Collection<TerrainProperty> presentProps = currentTemplate.getValues().keySet();
        TerrainProperty[] options = Collections2.filter(
                Set.of(TerrainProperty.values()),
                prop -> !presentProps.contains(prop)
        ).stream().sorted().toArray(TerrainProperty[]::new);
        ComboBoxModel<TerrainProperty> model = new DefaultComboBoxModel<>(
                options
        );
        properties.setModel(model);
        addTerrainProp.setEnabled(options.length > 0);
        properties.setEnabled(options.length > 0);
    }

    @Subscribe
    public void onTerrainPresetSelected(TerrainPresetSelectedEvent event) {
        currentTemplate = event.getTemplate();
        syncPropertySliders();
    }

    @Subscribe
    public void onTerrainTemplateUpdate(TerrainTemplateUpdateEvent event) {
        currentTemplate = TemplateUtil.merge(currentTemplate, event);
        brushes.setSelectedItem(BrushOption.CUSTOM);
        syncPropertySliders();
    }
}
