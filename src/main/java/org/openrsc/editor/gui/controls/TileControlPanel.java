package org.openrsc.editor.gui.controls;

import com.google.common.eventbus.EventBus;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainPresetSelectedEvent;
import org.openrsc.editor.model.brush.BrushOption;
import org.openrsc.editor.model.template.TerrainProperty;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class TileControlPanel extends JPanel {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    public TileControlPanel(int x, int y) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLocation(x, y);
        setSize(400, 400);
        setVisible(true);

        JPanel presetsPanel = getPresetsPanel();
        add(presetsPanel);

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

        Arrays.stream(TerrainProperty.values())
                .forEach(terrainProperty -> add(new TerrainPropertySlider(terrainProperty)));
    }

    private JPanel getPresetsPanel() {
        JPanel presetsPanel = new JPanel();
        presetsPanel.setLayout(new BoxLayout(presetsPanel, BoxLayout.X_AXIS));

        JLabel loadPresetLabel = new JLabel("Load Preset");
        loadPresetLabel.setPreferredSize(new Dimension(150, 15));
        presetsPanel.add(loadPresetLabel);

        JComboBox<BrushOption> brushes = new JComboBox<>(BrushOption.values());
        brushes.setSelectedItem(BrushOption.NONE);
        brushes.addActionListener(evt -> {
            BrushOption option = (BrushOption) brushes.getSelectedItem();
            eventBus.post(new TerrainPresetSelectedEvent(
                    Objects.requireNonNull(option).getTemplate()
            ));
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
}
