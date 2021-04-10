package org.openrsc.editor.model.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum RoofDefinition implements LabeledDefinition {
    NONE("None", 0),
    STANDARD_ROOF("Standard Roof", 1),
    WOODEN_PANEL_ROOF("Wooden Panel Roof", 2),
    UNKNOWN("Unknown", 3);

    public static final Map<Integer, RoofDefinition> ROOFS;

    static {
        ROOFS = new HashMap<>();

        Arrays.stream(RoofDefinition.values()).filter(def -> def != NONE).forEach(roofDefinition -> {
            ROOFS.put(roofDefinition.getRoof(), roofDefinition);
        });
    }

    private final String label;
    private final int roof;

    public TerrainTemplate toTerrainTemplate() {
        return TerrainTemplate.builder()
                .value(TerrainProperty.ROOF_TEXTURE, getRoof())
                .build();
    }

    @Override
    public String toString() {
        return label;
    }
}
