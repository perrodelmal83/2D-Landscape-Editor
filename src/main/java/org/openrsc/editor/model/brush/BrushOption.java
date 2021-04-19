package org.openrsc.editor.model.brush;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum BrushOption {
    NONE(
            "None",
            new TerrainTemplate(Collections.emptyMap())
    ),

    CLEAR_TILE(
            "Delete Tile",
            new TerrainTemplate(Arrays.stream(TerrainProperty.values()).collect(Collectors.toMap(prop -> prop, prop -> 0)))
    ),

    REMOVE_NORTH_WALL(
            "Remove North Wall",
            TerrainTemplate.builder()
                    .value(TerrainProperty.WALL_NORTH, 0)
                    .build()
    ),

    REMOVE_EAST_WALL(
            "Remove East Wall",
            TerrainTemplate.builder()
                    .value(TerrainProperty.WALL_EAST, 0)
                    .build()
    ),

    REMOVE_DIAGONAL_WALL(
            "Remove Diagonal Wall",
            TerrainTemplate.builder()
                    .value(TerrainProperty.WALL_DIAGONAL, 0)
                    .build()
    ),

    REMOVE_OVERLAY(
            "Remove Overlay",
            TerrainTemplate.builder()
                    .value(TerrainProperty.GROUND_OVERLAY, 0)
                    .build()
    ),

    REMOVE_ROOF(
            "Remove Roof",
            TerrainTemplate.builder()
                    .value(TerrainProperty.ROOF_TEXTURE, 0)
                    .build()
    ),

    GRASS(
            "Grass",
            TerrainTemplate.builder()
                    .value(TerrainProperty.GROUND_TEXTURE, 70)
                    .build()
    ),

    GREY_PATH(
            "Grey Path",
            TerrainTemplate.builder()
                    .value(TerrainProperty.GROUND_OVERLAY, 1)
                    .build()
    ),

    WATER(
            "Water",
            TerrainTemplate.builder()
                    .value(TerrainProperty.GROUND_OVERLAY, 2)
                    .build()
    ),

    WOODEN_FLOOR(
            "Wooden Floor",
            TerrainTemplate.builder()
                    .value(TerrainProperty.GROUND_OVERLAY, 3)
                    .build()
    ),

    DARK_RED_BANK_FLOOR(
            "Dark Red Bank Floor",
            TerrainTemplate.builder()
                    .value(TerrainProperty.GROUND_OVERLAY, 6)
                    .build()
    ),

    BLACK_FLOOR(
            "Black Floor",
            TerrainTemplate.builder()
                    .value(TerrainProperty.GROUND_OVERLAY, 16)
                    .build()
    ),

    ROOF(
            "Roof",
            TerrainTemplate.builder()
                    .value(TerrainProperty.ROOF_TEXTURE, 1)
                    .build()
    ),

    CUSTOM(
            "Configure your own",
            null
    );

    private final String label;
    private final TerrainTemplate template;

    @Override
    public String toString() {
        return label;
    }
}
