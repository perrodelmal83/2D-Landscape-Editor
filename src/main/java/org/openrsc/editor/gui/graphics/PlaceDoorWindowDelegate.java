package org.openrsc.editor.gui.graphics;

import org.openrsc.editor.Util;
import org.openrsc.editor.gui.dialog.SelectWallDialog;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.definition.WallDefinition;
import org.openrsc.editor.model.definition.WallDirection;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class PlaceDoorWindowDelegate extends ToolDelegate {
    private final EditorCanvas editorCanvas;

    private Tile hoverTile;
    private WallDirection wallDirection;
    private WallDefinition wallDef;

    public PlaceDoorWindowDelegate(EditorCanvas editorCanvas) {
        super(EditorTool.PLACE_DOOR_WINDOW);
        this.editorCanvas = editorCanvas;
    }

    @Override
    public void render(Graphics2D g) {
        if (hoverTile != null && wallDirection != null) {
            EditorCanvas.drawLine(hoverTile, wallDirection.getLineLocation(), Color.WHITE, 2);
        }
    }

    @Override
    public void onToolMount() {
        this.wallDef = WallDefinition.STONE_WALL;
        new SelectWallDialog(
                (wallDef) -> this.wallDef = wallDef
        );
    }

    @Override
    public void onToolUnmount() {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (wallDirection != null && hoverTile != null) {
            switch (wallDirection) {
                case NORTH:
                    hoverTile.setNorthWall((byte) wallDef.getNorthWall());
                    break;
                case EAST:
                    hoverTile.setEastWall((byte) wallDef.getEastWall());
                    break;
                case DIAGONAL_FORWARD:
                    hoverTile.setDiagonalWalls(wallDef.getDiagonalWallForward());
                    break;
                case DIAGONAL_BACKWARD:
                    hoverTile.setDiagonalWalls(wallDef.getDiagonalWallBackward());
                    break;
            }
            Util.sectorModified = true;
        }
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

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point mouseCoords = mouseEvent.getPoint();
        Point tileGridPos = Util.mouseCoordsToGridCoords(mouseCoords);
        Tile tile = editorCanvas.getTileByGridCoords(tileGridPos);
        Tile tileWest = editorCanvas.getTileByGridCoords(new Point(tile.getGridX() + 1, tile.getGridY()));
        Tile tileSouth = editorCanvas.getTileByGridCoords(new Point(tile.getGridX(), tile.getGridY() + 1));

        wallDirection = null;
        if (tile.getDiagonalWalls() != 0) {
            wallDirection =
                    tile.getDiagonalWalls() > 12000 ? WallDirection.DIAGONAL_BACKWARD : WallDirection.DIAGONAL_FORWARD;
            hoverTile = tile;
            return;
        }
        if (tileWest != null && tileWest.getRightBorderWallInt() != 0) {
            wallDirection = WallDirection.EAST;
            hoverTile = tileWest;
        }
        if (tileSouth != null && tileSouth.getTopBorderWallInt() != 0) {
            wallDirection = WallDirection.NORTH;
            hoverTile = tileSouth;
        }
        if (tile.getRightBorderWallInt() != 0) {
            wallDirection = WallDirection.EAST;
            hoverTile = tile;
        }
        if (tile.getTopBorderWallInt() != 0) {
            wallDirection = WallDirection.NORTH;
            hoverTile = tile;
        }
    }
}
