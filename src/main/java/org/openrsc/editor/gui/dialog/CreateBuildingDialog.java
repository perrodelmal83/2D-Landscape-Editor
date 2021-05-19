package org.openrsc.editor.gui.dialog;

import org.openrsc.editor.model.configuration.CreateBuildingConfiguration;
import org.openrsc.editor.model.definition.OverlayDefinition;
import org.openrsc.editor.model.definition.RoofDefinition;
import org.openrsc.editor.model.definition.WallDefinition;
import org.openrsc.editor.model.definition.WallDirection;
import org.openrsc.editor.model.definition.WallType;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public class CreateBuildingDialog extends JFrame {
    private final CreateBuildingConfiguration.CreateBuildingConfigurationBuilder builder;

    public CreateBuildingDialog(
            Consumer<CreateBuildingConfiguration> onComplete,
            Runnable onCancel
    ) throws HeadlessException {
        super("Create Building");
        builder = CreateBuildingConfiguration.builder();

        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        JCheckBox levelElevation = new JCheckBox("Level Elevation");
        levelElevation.setAlignmentX(SwingConstants.LEFT);
        panel.add(levelElevation);

        JLabel wallsLabel = new JLabel("Walls");
        wallsLabel.setAlignmentX(SwingConstants.LEFT);
        JComboBox<WallDefinition> walls = new JComboBox<>(
                Arrays.stream(WallDefinition.values())
                        .filter(definition -> definition.getWallType() == WallType.WALL)
                        .toArray(WallDefinition[]::new)
        );
        walls.addActionListener(evt -> {
            WallDefinition definition = (WallDefinition) walls.getSelectedItem();
            if (definition != null) {
                builder.northWall(definition.toTerrainTemplate(WallDirection.NORTH));
                builder.eastWall(definition.toTerrainTemplate(WallDirection.EAST));
                builder.diagonalWall(definition.toTerrainTemplate(WallDirection.DIAGONAL_FORWARD));
                builder.reverseDiagonalWall(definition.toTerrainTemplate(WallDirection.DIAGONAL_BACKWARD));
            }
        });
        walls.setSelectedItem(WallDefinition.STONE_WALL);
        add(wallsLabel);
        add(walls);

        JLabel floorLabel = new JLabel("Floor");
        floorLabel.setAlignmentX(SwingConstants.LEFT);
        JComboBox<OverlayDefinition> floor = new JComboBox<>(
                Arrays.stream(OverlayDefinition.values())
                        .filter(OverlayDefinition::isFloor)
                        .toArray(OverlayDefinition[]::new)
        );
        floor.addActionListener(evt ->
                Optional.ofNullable((OverlayDefinition) floor.getSelectedItem()).ifPresent(
                        overlayDefinition -> builder.floor(overlayDefinition.toTerrainTemplate())
                )
        );
        floor.setSelectedItem(OverlayDefinition.WOODEN_FLOOR);
        panel.add(floorLabel);
        panel.add(floor);

        JLabel roofLabel = new JLabel("Roof");
        roofLabel.setAlignmentX(SwingConstants.LEFT);
        JComboBox<RoofDefinition> roof = new JComboBox<>(RoofDefinition.values());
        roof.addActionListener(evt ->
                Optional.ofNullable((RoofDefinition) roof.getSelectedItem()).ifPresent(
                        roofDefinition -> builder.roof(roofDefinition.toTerrainTemplate())
                )
        );
        roof.setSelectedItem(RoofDefinition.STANDARD_ROOF);
        panel.add(roofLabel);
        panel.add(roof);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(evt -> {
            setVisible(false);
            onCancel.run();
        });
        JButton submit = new JButton("Submit");
        submit.addActionListener(evt -> {
            setVisible(false);
            onComplete.accept(builder.build());
        });

        buttonPanel.add(cancel);
        buttonPanel.add(submit);

        panel.add(buttonPanel);

        add(panel);
        pack();
        setVisible(true);
    }
}
