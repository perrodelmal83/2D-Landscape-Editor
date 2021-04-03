package org.openrsc.editor;

import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.template.TerrainTemplate;

public class TemplateUtil {
    public static TerrainTemplate merge(
            TerrainTemplate currentTemplate,
            TerrainTemplateUpdateEvent updateEvent
    ) {

        TerrainTemplate.TerrainTemplateBuilder builder;
        if (currentTemplate != null) {
            builder = currentTemplate.toBuilder();
        } else {
            builder = TerrainTemplate.builder();
        }
        updateEvent.getTemplate().getValues().forEach(builder::value);
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
        }
    }
}
