package org.openrsc.editor.model.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.template.Template;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum WallDefinition implements Template, LabeledDefinition {
    NONE("None", 0, 0, 0, 0, WallType.WALL),
    STONE_WALL("Stone wall", 1, 1, 1, 12001, WallType.WALL),
    WOODEN_DOOR_FRAME("Wooden door frame", 2, 2, 2, 12002, WallType.DOOR_FRAME),
    STONE_DOOR_FRAME("Stone door frame", 3, 3, 3, 12003, WallType.DOOR_FRAME),
    STONE_WINDOW_GLASS("Stone window (glass)", 4, 4, 4, 12004, WallType.WINDOW),
    PICKET_FENCE("Picket fence", 5, 5, 5, 12005, WallType.WALL),
    METAL_FENCE_1("Metal Fence (Cemetery)", 6, 6, 6, 12006, WallType.WALL),
    STONE_WINDOW_GLASS_DECORATIVE("Stone window (stained glass)", 7, 7, 7, 12007, WallType.WINDOW),
    STONE_WALL_ARDOUGNE("Stone wall (Ardougne)", 8, 8, 8, 12008, WallType.WALL),
    STONE_HALF_WALL("Stone half wall", 11, 11, 11, 12011, WallType.WALL),
    STONE_WINDOW_ARCH("Stone window (arch)", 14, 14, 14, 12014, WallType.WINDOW),
    WOODEN_WALL_WHITE("Wooden wall (white)", 15, 15, 15, 12015, WallType.WALL),
    WOODEN_WALL_WHITE_WINDOW("Wooden wall (white) window", 16, 16, 16, 12016, WallType.WINDOW),
    STONE_DOUBLE_ARCH_DOOR_FRAME("Stone double door frame (arch)", 17, 17, 17, 12017, WallType.DOOR_FRAME),
    SWAMP_WALL("Swamp wall", 19, 19, 19, 12019, WallType.WALL),
    STONE_WALL_RUINS("Stone wall (ruins)", 42, 42, 42, 12042, WallType.WALL),
    LAVA_ROCK_WALL("Lava rock wall", 43, 43, 43, 12043, WallType.WALL),
    WOODEN_PANEL_WALL("Wooden panel wall", 57, 57, 57, 12057, WallType.WALL),
    METAL_FENCE_2("Metal Fence (Ardougne)", 63, 63, 63, 12063, WallType.WALL),
    JUNGLE_INVISIBLE_WALL("Invisible jungle wall", 87, 87, 87, 12087, WallType.WALL),
    CAVE_INVISIBLE_WALL("Invisible cave wall", 120, 120, 120, 12120, WallType.WALL),
    WOODEN_PANEL_WINDOW("Wooden panel window", 145, 145, 145, 12145, WallType.WINDOW),
    WOODEN_SHACK_WINDOW("Wooden shack window (disheveled)", 145, 145, 145, 12145, WallType.WINDOW);

    private final String label;
    private final int northWall;
    private final int eastWall;
    private final int diagonalWallForward;
    private final int diagonalWallBackward;
    private final WallType wallType;

    public static final Map<Integer, WallDefinition> NORMAL;
    public static final Map<Integer, WallDefinition> DIAGONAL_BACKWARDS;

    static {
        NORMAL = new HashMap<>();
        DIAGONAL_BACKWARDS = new HashMap<>();

        Arrays.stream(WallDefinition.values()).filter(val -> val != NONE).forEach(wallDefinition -> {
            NORMAL.put(
                    wallDefinition.getNorthWall(),
                    wallDefinition
            );
            DIAGONAL_BACKWARDS.put(
                    wallDefinition.getDiagonalWallBackward(),
                    wallDefinition
            );
        });
    }

    public TerrainTemplate toTerrainTemplate(WallDirection direction) {
        TerrainTemplate.TerrainTemplateBuilder builder = TerrainTemplate.builder();
        switch (direction) {
            case NORTH:
                builder.value(TerrainProperty.WALL_NORTH, getNorthWall());
                break;
            case EAST:
                builder.value(TerrainProperty.WALL_EAST, getEastWall());
                break;
            case DIAGONAL_FORWARD:
                builder.value(TerrainProperty.WALL_DIAGONAL, getDiagonalWallForward());
                break;
            case DIAGONAL_BACKWARD:
                builder.value(TerrainProperty.WALL_DIAGONAL, getDiagonalWallBackward());
                break;
        }
        return builder.build();
    }

    public static WallDefinition fromInt(int i) {
        if (i >= 12000) {
            return DIAGONAL_BACKWARDS.get(i);
        }

        return NORMAL.get(i);
    }

    public static Color getWallColor(WallDefinition definition) {
        if (definition.getWallType() == WallType.WALL) {
            return ColorConstants.WALL_OUTLINE_COLOR;
        } else if (definition.getWallType() == WallType.WINDOW) {
            return ColorConstants.WINDOW_OUTLINE_COLOR;
        } else if (definition.getWallType() == WallType.DOOR_FRAME) {
            return ColorConstants.DOOR_OUTLINE_COLOR;
        } else {
            return ColorConstants.INVISIBLE;
        }
    }

    @Override
    public String toString() {
        return label;
    }
}
