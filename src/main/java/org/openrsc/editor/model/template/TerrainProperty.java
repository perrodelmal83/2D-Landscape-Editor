package org.openrsc.editor.model.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.definition.LabeledDefinition;
import org.openrsc.editor.model.definition.OverlayDefinition;
import org.openrsc.editor.model.definition.RoofDefinition;
import org.openrsc.editor.model.definition.WallDefinition;

@Getter
@AllArgsConstructor
public enum TerrainProperty {
    GROUND_TEXTURE("Texture", null),
    GROUND_OVERLAY("Overlay", OverlayDefinition.class),
    WALL_DIAGONAL("Diagonal Wall", WallDefinition.class),
    WALL_NORTH("North Wall", WallDefinition.class),
    WALL_EAST("East Wall", WallDefinition.class),
    ROOF_TEXTURE("Roof Texture", RoofDefinition.class),
    GROUND_ELEVATION("Elevation", null);

    private final String label;
    private final Class<? extends LabeledDefinition> definitionClass;

    @Override
    public String toString() {
        return label;
    }
}
