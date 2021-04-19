package org.openrsc.editor;

import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.util.HashMap;
import java.util.Map;

public class TemplateUtil {
    public static TerrainTemplate merge(
            TerrainTemplate currentTemplate,
            TerrainTemplateUpdateEvent updateEvent
    ) {
        Map<TerrainProperty, Integer> values;
        TerrainTemplate.TerrainTemplateBuilder builder;
        if (currentTemplate != null) {
            values = new HashMap<>(currentTemplate.getValues());
            builder = currentTemplate.toBuilder();
        } else {
            values = new HashMap<>(updateEvent.getTemplate().getValues());
            builder = TerrainTemplate.builder();
        }

        updateEvent.getTemplate().getValues().forEach((key, value) -> {
            if (value != null) {
                values.put(key, value);
            } else {
                values.remove(key);
            }
        });
        builder.clearValues();
        builder.values(values);
        return builder.build();
    }

    public static void applyBrush(Tile tile, TerrainTemplate template) {
        if (tile != null && template != null) {
            template.getValues().forEach((property, value) -> {
                switch (property) {
                    case GROUND_TEXTURE:
                        tile.setGroundTexture(value.byteValue());
                        break;
                    case GROUND_OVERLAY:
                        tile.setGroundOverlay(value.byteValue());
                        break;
                    case WALL_DIAGONAL:
                        tile.setDiagonalWalls(value);
                        break;
                    case WALL_EAST:
                        tile.setEastWall(value.byteValue());
                        break;
                    case WALL_NORTH:
                        tile.setNorthWall(value.byteValue());
                        break;
                    case GROUND_ELEVATION:
                        tile.setGroundElevation(value.byteValue());
                        break;
                    case ROOF_TEXTURE:
                        tile.setRoofTexture(value.byteValue());
                        break;
                }
            });
            Util.sectorModified = true;
        }
    }
}
