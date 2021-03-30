package org.openrsc.editor.gui.controls;

import org.openrsc.editor.Util;
import org.openrsc.editor.model.TerrainProperty;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.function.Function;

public class TileControlPanel extends JPanel {
    public TileControlPanel(int x, int y) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLocation(x, y);
        setSize(400, 500);
        setVisible(true);

        addLabeledSlider(
                "Stamp Size (" + Util.stampSize + ")",
                Util.stampSize,
                0,
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
        label.setPreferredSize(new Dimension(100, 15));
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
