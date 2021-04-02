package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainPresetSelectedEvent;
import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.brush.TerrainTemplate;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StampToolDelegate extends ToolDelegate {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    private List<Tile> hoverTiles = Collections.emptyList();
    private final EditorCanvas editorCanvas;
    private TerrainTemplate currentTemplate;

    public StampToolDelegate(EditorCanvas editorCanvas) {
        super(EditorTool.STAMP);
        this.editorCanvas = editorCanvas;
        eventBus.register(this);
    }

    private void applyBrush(Tile tile) {
        if (tile != null && currentTemplate != null) {
            currentTemplate.getValues().forEach((property, value) -> {
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

    private void calcStampCursor(Point cursorCoords, int stampSize) {
        List<Tile> tiles = new ArrayList<>(stampSize * stampSize);

        // Translate origin back half distance to center main point, then add all points covering stamp size
        Point hoveredGridPosition = Util.mouseCoordsToGridCoords(cursorCoords);
        int xOrigin = hoveredGridPosition.x - stampSize / 2;
        int yOrigin = hoveredGridPosition.y - stampSize / 2;

        for (int j = 0; j < stampSize; j++) {
            for (int i = 0; i < stampSize; i++) {
                Optional.ofNullable(
                        editorCanvas.getTileByGridCoords(xOrigin + i, yOrigin + j)
                ).ifPresent(tiles::add);
            }
        }

        hoverTiles = tiles;
    }

    @Override
    public void render(Graphics2D g) {
        hoverTiles.stream()
                .filter(Objects::nonNull)
                .forEach(editorCanvas::drawTileBorder);
    }

    @Override
    public void onToolMount() {

    }

    @Override
    public void onToolUnmount() {

    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        calcStampCursor(evt.getPoint(), Util.stampSize);
        hoverTiles.forEach(this::applyBrush);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        hoverTiles = Collections.emptyList();
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        calcStampCursor(evt.getPoint(), Util.stampSize);
        hoverTiles.forEach(this::applyBrush);
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        calcStampCursor(evt.getPoint(), Util.stampSize);
    }

    @Subscribe
    public void onTerrainTemplateUpdate(TerrainTemplateUpdateEvent event) {
        this.currentTemplate = event.getTemplate();
    }

    @Subscribe
    public void onTerrainPresetSelected(TerrainPresetSelectedEvent event) {
        this.currentTemplate = event.getTemplate();
    }
}
