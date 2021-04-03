package org.openrsc.editor.model;

import lombok.Getter;
import lombok.Setter;
import org.openrsc.editor.Util;
import org.openrsc.editor.model.data.GameObjectLoc;
import org.openrsc.editor.model.data.ItemLoc;
import org.openrsc.editor.model.data.NpcLoc;

import java.awt.Point;
import java.awt.Shape;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author xEnt/Vrunk/Peter the Properties/values that each RSC Tile holds.
 */

@Getter
@Setter
public class Tile {

    private GameObjectLoc objectLoc;
    private ItemLoc itemLoc;
    private NpcLoc npcLoc;

    private int gridX = 0;
    private int gridY = 0;
    private int ID = -1;
    private int x = -1;
    private int y = -1;
    private Shape shape;
    private byte groundElevation = -1;
    private byte groundTexture = -1;
    private byte groundOverlay = -1;
    private byte roofTexture = -1;
    private byte rightBorderWall = -1;
    private byte topBorderWall = -1;
    private int diagonalWalls = -1;

    public int getGroundElevationInt() {
        return groundElevation & 0xff;
    }

    public int getGroundTextureInt() {
        return groundTexture & 0xff;
    }

    public int getGroundOverlayInt() {
        return groundOverlay & 0xff;
    }

    public int getGridX() {
        return this.gridX;
    }

    /**
     * @return - the position inside the lane
     */
    public int getGridY() {
        return this.gridY;
    }

    /**
     * @return the roof Texture
     */
    public byte getRoofTexture() {
        return roofTexture;
    }

    /**
     * @return the roof Texture integer
     */
    public int getRoofTextureInt() {
        return roofTexture & 0xff;
    }

    /**
     * @param roofTexture the roofTexture to set
     */
    public void setRoofTexture(byte roofTexture) {
        this.roofTexture = roofTexture;
    }

    /**
     * @return the horizontalWall
     */
    public byte getRightBorderWall() {
        return rightBorderWall;
    }

    /**
     * @return the horizontalWall
     */
    public int getRightBorderWallInt() {
        return rightBorderWall & 0xff;
    }

    /**
     * @param rightBorderWall the horizontalWall to set
     */
    public void setEastWall(byte rightBorderWall) {
        this.rightBorderWall = rightBorderWall;
    }

    /**
     * @return the verticalWall
     */
    public byte getTopBorderWall() {
        return topBorderWall;
    }

    /**
     * @return the verticalWall int
     */
    public int getTopBorderWallInt() {
        return topBorderWall & 0xff;
    }

    /**
     * @param topBorderWall the verticalWall to set
     */

    public void setNorthWall(byte topBorderWall) {
        this.topBorderWall = topBorderWall;
    }

    /**
     * @return the diagonalWalls
     */
    public int getDiagonalWalls() {
        return diagonalWalls;
    }

    /**
     * @param diagonalWalls the diagonalWalls to set
     */
    public void setDiagonalWalls(int diagonalWalls) {
        this.diagonalWalls = diagonalWalls;
    }

    /**
     * @param y - the new Y axis.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @param x - the new X axis.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the tile's X axis
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return the tile's Y axis
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return - the ID of the tiles set inside the drawTile loop.
     */
    public int getID() {
        return ID;
    }

    /**
     * @return - the Point holding the correctly synced RSC Coordinates for this
     * tile.
     */
    public Point getRSCCoords() {
        return Util.getRSCCoords(this);
    }

    /**
     * @param id - the new tile's ID.
     */
    public void setID(int id) {
        ID = id;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public void setGridY(int pos) {
        this.gridY = pos;
    }

    /**
     * @param in   - the Byte buffer holding every tile value.
     * @param tile - the current instance
     * @return - the new instance to the Tile with all properties set.
     */
    public Tile unpack(ByteBuffer in, Tile tile) {
        tile.groundElevation = in.get();
        tile.groundTexture = in.get();
        tile.groundOverlay = in.get();
        tile.roofTexture = in.get();
        tile.rightBorderWall = in.get();
        tile.topBorderWall = in.get();
        tile.diagonalWalls = in.getInt();
        return tile;
    }

    /**
     * Writes the Tile raw data into a ByteBuffer
     *
     * @return - the packed tile
     */
    public ByteBuffer pack() throws IOException {
        ByteBuffer out = ByteBuffer.allocate(10);

        out.put(groundElevation);
        out.put(groundTexture);
        out.put(groundOverlay);
        out.put(roofTexture);

        out.put(rightBorderWall);
        out.put(topBorderWall);
        out.putInt(diagonalWalls);

        out.flip();
        return out;
    }
}
