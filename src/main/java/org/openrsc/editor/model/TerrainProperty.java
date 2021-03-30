package org.openrsc.editor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TerrainProperty {
    GROUND_TEXTURE("Texture"),
    GROUND_OVERLAY("Overlay"),
    WALL_DIAGONAL("Diagonal Wall"),
    WALL_NORTH("North Wall"),
    WALL_EAST("East Wall"),
    ROOF_TEXTURE("Roof Texture"),
    GROUND_ELEVATION("Elevation");

    private final String label;
}
