package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.DisplayConfigurationUpdateEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.configuration.DisplayConfiguration;
import org.openrsc.editor.model.configuration.DisplayConfigurationProperty;
import org.openrsc.editor.model.definition.ColorConstants;
import org.openrsc.editor.model.definition.OverlayDefinition;
import org.openrsc.editor.model.definition.RoofDefinition;
import org.openrsc.editor.model.definition.WallDefinition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Optional;

import static org.openrsc.editor.gui.graphics.EditorCanvas.TILE_SIZE;
import static org.openrsc.editor.model.configuration.DisplayConfiguration.DEFAULT_DISPLAY_CONFIGURATION;

public class TileRenderer {
    private static final int WALL_THICKNESS = 2;
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    private DisplayConfiguration displayConfiguration;

    public TileRenderer() {
        eventBus.register(this);

        this.displayConfiguration = DEFAULT_DISPLAY_CONFIGURATION;
    }

    public void renderTile(Tile tile, Graphics2D g) {
        if (tile == null) {
            return;
        }

        g.setStroke(new BasicStroke(2));
        // paints Tile ground data.
        if (tile.getGroundTextureInt() >= 0) {
            int groundTexture = tile.getGroundTextureInt();
            g.setColor(
                    Util.MAP_BRIGHTNESS_LIGHT ? Util.colorArray[groundTexture] : Util.colorArray[groundTexture].darker().darker()
            );
            g.fill(tile.getShape());
            g.draw(tile.getShape());
        }

        // paints Tile ground data (Paths/roads etc, things on top of the original data)
        OverlayDefinition overlayDef = OverlayDefinition.OVERLAYS.get(tile.getGroundOverlayInt());
        if (overlayDef != null) {
            g.setColor(OverlayDefinition.OVERLAYS.get(tile.getGroundOverlayInt()).getColor());
            g.fill(tile.getShape());
            g.draw(tile.getShape());

            // Draw X to let user know this terrain is not passible
            if (!overlayDef.isPassable()) {
                EditorCanvas.drawLine(
                        tile,
                        LineLocation.DIAGONAL_FROM_TOP_RIGHT,
                        ColorConstants.IMPASSIBLE_TERRAIN_OUTLINE,
                        WALL_THICKNESS);
                EditorCanvas.drawLine(
                        tile,
                        LineLocation.DIAGONAL_FROM_TOP_LEFT,
                        ColorConstants.IMPASSIBLE_TERRAIN_OUTLINE,
                        WALL_THICKNESS
                );
            }
        }
        // paints walls
        Optional.ofNullable(
                WallDefinition.fromInt(tile.getTopBorderWallInt())
        ).ifPresent(northWall ->
                EditorCanvas.drawLine(
                        tile,
                        LineLocation.BORDER_TOP,
                        WallDefinition.getWallColor(northWall),
                        WALL_THICKNESS
                )
        );
        Optional.ofNullable(
                WallDefinition.fromInt(tile.getRightBorderWallInt())
        ).ifPresent(eastWall ->
                EditorCanvas.drawLine(
                        tile,
                        LineLocation.BORDER_RIGHT,
                        WallDefinition.getWallColor(eastWall),
                        WALL_THICKNESS
                )
        );
        // paints Diagonal walls.
        if (WallDefinition.NORMAL.containsKey(tile.getDiagonalWalls())) {
            WallDefinition def = WallDefinition.fromInt(tile.getDiagonalWalls());
            EditorCanvas.drawLine(
                    tile,
                    LineLocation.DIAGONAL_FROM_TOP_RIGHT,
                    WallDefinition.getWallColor(def),
                    WALL_THICKNESS);
        }
        if (WallDefinition.DIAGONAL_BACKWARDS.containsKey(tile.getDiagonalWalls())) {
            WallDefinition def = WallDefinition.fromInt(tile.getDiagonalWalls());
            EditorCanvas.drawLine(
                    tile,
                    LineLocation.DIAGONAL_FROM_TOP_LEFT,
                    WallDefinition.getWallColor(def),
                    WALL_THICKNESS
            );
        }

        boolean showRoofs = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_ROOFS);
        Shape outerRectangle = new Rectangle(
                tile.getX() + 1,
                tile.getY(),
                TILE_SIZE - 1,
                TILE_SIZE - 1
        );
        if (showRoofs && tile.getRoofTextureInt() != 0) {
            if (RoofDefinition.ROOFS.containsKey(tile.getRoofTextureInt())) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.GREEN);
            }
            g.draw(outerRectangle);

        }

        renderPeripherals(tile, g);
    }

    private void renderPeripherals(Tile tile, Graphics2D g) {
        Point rscCoords = Util.getRSCCoords(tile);

        boolean showObjects = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_OBJECTS);
        boolean showItems = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_ITEMS);
        boolean showNpcs = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_NPCS);

        if (showObjects && Util.sceneryLocationMap.get(rscCoords) != null) {
            fillInnerTile(tile, g, Color.CYAN);
        }
        if (showObjects && Util.boundaryLocsMap.get(rscCoords) != null) {
            fillInnerTile(tile, g, Color.CYAN);
        }
        if (showItems && Util.itemLocationMap.get(rscCoords) != null) {
            fillInnerTile(tile, g, Color.RED);
        }
        if (showNpcs && Util.npcLocationMap.get(rscCoords) != null) {
            fillInnerTile(tile, g, Color.YELLOW);
        }
    }

    private void fillInnerTile(Tile tile, Graphics2D g, Color color) {
        int innerInset = 8;
        Shape innerRectangle = new Rectangle(
                tile.getX() + 1 + innerInset / 2,
                tile.getY() + innerInset / 2,
                TILE_SIZE - 1 - innerInset,
                TILE_SIZE - 1 - innerInset
        );

        g.setColor(color);
        g.fill(innerRectangle);
        g.draw(innerRectangle);
    }

    @Subscribe
    public void onDisplayConfigurationChanged(DisplayConfigurationUpdateEvent event) {
        DisplayConfiguration.DisplayConfigurationBuilder configBuilder = this.displayConfiguration.toBuilder();
        event.getUpdatedProperties().forEach(configBuilder::property);
        this.displayConfiguration = configBuilder.build();
    }

}
