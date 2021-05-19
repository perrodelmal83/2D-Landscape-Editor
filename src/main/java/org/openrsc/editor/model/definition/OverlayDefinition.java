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
    NONE("None", 0, true, ColorConstants.INVISIBLE, true),
    ROAD("Road", 1, true, Color.DARK_GRAY, false),
    WATER("Water", 2, false, ColorConstants.WATER_BLUE, false),
    WOODEN_FLOOR("Wooden Floor", 3, true, ColorConstants.FLOOR_BROWN, true),
    WOODEN_BRIDGE("Wooden Water Bridge", 4, true, ColorConstants.FLOOR_BROWN, true),
    GRAY_FLOOR("Gray Floor", 5, true, ColorConstants.FLOOR_GRAY, true),
    RED_FLOOR("Red Floor", 6, true, ColorConstants.FLOOR_RED, true),
    SEWER_WATER("Water (sewer)", 7, false, ColorConstants.WATER_SEWER, false),
    BLACK_CLIFF("Black (impassable)", 8, false, Color.BLACK, false),
    WHITE_CLIFF("White (impassable)", 9, false, Color.WHITE, false),
    LAVA("Lava", 11, false, Color.ORANGE, false),
    BLUE_FLOOR("Blue Floor", 13, true, ColorConstants.FLOOR_BLUE, true),
    PENTAGRAM("Pentagram", 14, true, Color.LIGHT_GRAY, true),
    PURPLE_FLOOR("Purple Floor", 15, true, ColorConstants.FLOOR_PURPLE, true),
    BLACK_FLOOR("Black Floor", 16, true, Color.BLACK, true),
    CAVE_CLIFF("Brown (impassable)", 24, false, ColorConstants.FLOOR_BROWN.darker().darker(), false),
    NOTHINGNESS("Nothingness (out of bounds)", 250, false, Color.LIGHT_GRAY, false);

    public static final Map<Integer, OverlayDefinition> OVERLAYS;

    static {
        OVERLAYS = new HashMap<>();

        Arrays.stream(OverlayDefinition.values()).filter(def -> def != NONE).forEach(
                overlayDefinition -> OVERLAYS.put(overlayDefinition.getOverlay(), overlayDefinition)
        );
    }

    private final String label;
    private final int overlay;
    private final boolean passable;
    private final Color color;
    private final boolean floor;

    public TerrainTemplate toTerrainTemplate() {
        return TerrainTemplate.builder()
                .value(TerrainProperty.GROUND_OVERLAY, getOverlay())
                .build();
    }

    @Override
    public String toString() {
        return label;
    }
}
