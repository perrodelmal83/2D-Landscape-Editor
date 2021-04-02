package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.DisplayConfigurationUpdateEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.model.DisplayConfiguration;
import org.openrsc.editor.model.DisplayConfigurationProperty;
import org.openrsc.editor.model.Tile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import static org.openrsc.editor.gui.graphics.EditorCanvas.TILE_SIZE;
import static org.openrsc.editor.model.DisplayConfiguration.DEFAULT_DISPLAY_CONFIGURATION;

public class TileRenderer {
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
        if (Util.getOverlay.containsKey(tile.getGroundOverlay())) {
            g.setColor(Util.getOverlay.get(tile.getGroundOverlay()));
            g.fill(tile.getShape());
            g.draw(tile.getShape());
        }
        // paints Tile wall color (Vertical) + the line to show a wall is there.
        if (Util.getVerticalWallColor.containsKey(tile.getTopBorderWall())) {
            EditorCanvas.drawLine(tile, LineLocation.BORDER_TOP, Util.WALL_OUTLINE_COLOR);
        }
        // paints Tile wall color (Horizontal) + line to show a wall is there.
        if (Util.getHorizontalWallColor.containsKey(tile.getRightBorderWall())) {
            EditorCanvas.drawLine(tile, LineLocation.BORDER_RIGHT, Util.WALL_OUTLINE_COLOR);
        }
        // paints Diagonal walls.
        if (Util.forwardSlashDiagWallColorsMap.containsKey(tile.getDiagonalWallsInt())) {
            EditorCanvas.drawLine(tile, LineLocation.DIAGONAL_FROM_TOP_RIGHT, Util.WALL_OUTLINE_COLOR);
        }
        if (Util.backSlashDiagWallColorsMap.containsKey(tile.getDiagonalWallsInt())) {
            EditorCanvas.drawLine(tile, LineLocation.DIAGONAL_FROM_TOP_LEFT, Util.WALL_OUTLINE_COLOR);
        }

        renderPeripherals(tile, g);
    }

    private void renderPeripherals(Tile tile, Graphics2D offscreenGraphics) {
        Point rscCoords = Util.getRSCCoords(tile);
        int innerInset = 8;
        Shape innerRectangle = new Rectangle(
                tile.getX() + 1 + innerInset / 2,
                tile.getY() + innerInset / 2,
                TILE_SIZE - 1 - innerInset,
                TILE_SIZE - 1 - innerInset
        );
        Shape outerRectangle = new Rectangle(
                tile.getX() + 1,
                tile.getY(),
                TILE_SIZE - 1,
                TILE_SIZE - 1
        );
        boolean showObjects = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_OBJECTS);
        boolean showItems = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_ITEMS);
        boolean showNpcs = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_NPCS);
        boolean showRoofs = displayConfiguration.getProperties().get(DisplayConfigurationProperty.SHOW_ROOFS);

        if (showObjects && Util.gameObjectLocationMap.get(rscCoords) != null) {
            offscreenGraphics.setColor(Color.CYAN);
            offscreenGraphics.fill(innerRectangle);
            offscreenGraphics.draw(innerRectangle);
        }
        if (showItems && Util.itemLocationMap.get(rscCoords) != null) {
            offscreenGraphics.setColor(Color.RED);
            offscreenGraphics.fill(innerRectangle);
            offscreenGraphics.draw(innerRectangle);
        }
        if (showNpcs && Util.npcLocationMap.get(rscCoords) != null) {
            offscreenGraphics.setColor(Color.YELLOW);
            offscreenGraphics.fill(innerRectangle);
            offscreenGraphics.draw(innerRectangle);
        }
        if (showRoofs && tile.getRoofTexture() == (byte) 1) {
            offscreenGraphics.setColor(Color.ORANGE);
            offscreenGraphics.draw(outerRectangle);
        }
    }

    @Subscribe
    public void onDisplayConfigurationChanged(DisplayConfigurationUpdateEvent event) {
        DisplayConfiguration.DisplayConfigurationBuilder configBuilder = this.displayConfiguration.toBuilder();
        event.getUpdatedProperties().forEach(configBuilder::property);
        this.displayConfiguration = configBuilder.build();
    }

}
