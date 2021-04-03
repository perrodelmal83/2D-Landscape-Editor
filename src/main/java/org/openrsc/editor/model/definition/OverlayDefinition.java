package org.openrsc.editor.model.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    BLACK_CLIFF("Cliff (black)", 8, false, Color.BLACK),
    WHITE_CLIFF("Cliff (white)", 9, false, Color.WHITE),
    BLACK_FLOOR("Black Floor", 16, true, Color.BLACK);

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
}
