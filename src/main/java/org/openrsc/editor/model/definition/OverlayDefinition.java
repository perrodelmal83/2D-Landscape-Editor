package org.openrsc.editor.model.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum OverlayDefinition implements LabeledDefinition {
    ROAD("Road", 1, true, Color.DARK_GRAY),
    WATER("Water", 2, false, ColorConstants.WATER_BLUE),
    WOODEN_FLOOR("Wooden Floor", 3, true, ColorConstants.FLOOR_BROWN),
    WOODEN_BRIDGE("Wooden Water Bridge", 4, true, ColorConstants.FLOOR_BROWN),
    GRAY_FLOOR("Gray Floor", 5, true, ColorConstants.FLOOR_GRAY),
    RED_FLOOR("Red Floor", 6, true, ColorConstants.FLOOR_RED),
    SEWER_WATER("Water (sewer)", 7, false, ColorConstants.WATER_SEWER),
    BLACK_CLIFF("Black (impassable)", 8, false, Color.BLACK),
    WHITE_CLIFF("White (impassable)", 9, false, Color.WHITE),
    BLUE_FLOOR("Blue Floor", 13, true, ColorConstants.FLOOR_BLUE),
    PENTAGRAM("Pentagram", 14, true, Color.LIGHT_GRAY),
    PURPLE_FLOOR("Purple Floor", 15, true, ColorConstants.FLOOR_PURPLE),
    BLACK_FLOOR("Black Floor", 16, true, Color.BLACK),
    CAVE_CLIFF("Brown (impassable)", 24, false, ColorConstants.FLOOR_BROWN.darker().darker());

    public static final Map<Integer, OverlayDefinition> OVERLAYS;

    static {
        OVERLAYS = new HashMap<>();

        Arrays.stream(OverlayDefinition.values()).forEach(overlayDefinition -> {
            OVERLAYS.put(overlayDefinition.getOverlay(), overlayDefinition);
        });
    }

    private final String label;
    private final int overlay;
    private final boolean passable;
    private final Color color;

    public TerrainTemplate toTerrainTemplate() {
        return TerrainTemplate.builder()
                .value(TerrainProperty.GROUND_OVERLAY, getOverlay())
                .build();
    }
}
