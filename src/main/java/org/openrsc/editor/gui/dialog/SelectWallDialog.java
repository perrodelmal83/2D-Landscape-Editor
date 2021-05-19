package org.openrsc.editor.gui.dialog;

import org.openrsc.editor.model.definition.WallDefinition;
import org.openrsc.editor.model.definition.WallType;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class SelectWallDialog extends JFrame {
    public SelectWallDialog(Collection<WallType> wallTypes, Consumer<WallDefinition> onComplete) {
        super();

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel wallsLabel = new JLabel("Walls");
        WallDefinition[] wallDefinitions = Arrays.stream(WallDefinition.values())
                .filter(definition -> wallTypes.contains(definition.getWallType()))
                .toArray(WallDefinition[]::new);
        JComboBox<WallDefinition> walls = new JComboBox<>(
                wallDefinitions
        );
        walls.setSelectedItem(WallDefinition.STONE_WALL);
        panel.add(wallsLabel);
        panel.add(walls);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(evt -> {
            setVisible(false);
        });
        JButton submit = new JButton("Submit");
        submit.addActionListener(evt -> {
            setVisible(false);
            onComplete.accept((WallDefinition) walls.getSelectedItem());
        });

        panel.add(cancel);
        panel.add(submit);

        add(panel);
        pack();
        setVisible(true);
    }
}
