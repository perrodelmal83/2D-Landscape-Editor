package org.openrsc.editor.gui.graphics;

import org.openrsc.editor.Util;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.Tile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Optional;

public class InspectToolDelegate extends ToolDelegate {
    private final EditorCanvas editorCanvas;

    private Point gridPoint = null;

    public InspectToolDelegate(EditorCanvas editorCanvas) {
        super(EditorTool.INSPECT);
        this.editorCanvas = editorCanvas;
    }

    @Override
    public void render(Graphics2D g) {
        Optional.ofNullable(gridPoint).ifPresent(gridPoint -> {
            Tile currentTile = editorCanvas.getTileByGridCoords(
                    gridPoint.x, gridPoint.y
            );
            editorCanvas.drawTileBorder(currentTile);
        });
    }

    @Override
    public void onToolMount() {

    }

    @Override
    public void onToolUnmount() {
        gridPoint = null;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        gridPoint = Util.mouseCoordsToGridCoords(mouseEvent.getPoint());
        Tile currentTile = editorCanvas.getTileByGridCoords(
                gridPoint.x, gridPoint.y
        );

        System.out.println("Selected tile info: " + "\nID: " + currentTile.getID());
        System.out.println("Ground Elevation: " + currentTile.getGroundElevationInt());
        System.out.println("Ground Overlay: " + currentTile.getGroundOverlayInt());
        System.out.println("Roof Texture: " + currentTile.getRoofTextureInt());
        System.out.println("GroundTexture: " + currentTile.getGroundTextureInt());
        System.out.println("Diagonal Wall: " + currentTile.getDiagonalWalls());
        System.out.println("North Wall: " + currentTile.getTopBorderWallInt());
        System.out.println("East Wall: " + currentTile.getRightBorderWallInt());
        System.out.println("RSC Coords: " + Util.getRSCCoords(currentTile));
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

    }
}
