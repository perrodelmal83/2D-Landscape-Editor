package org.openrsc.editor;

import org.openrsc.editor.data.GameObjectLoc;
import org.openrsc.editor.data.ItemLoc;
import org.openrsc.editor.data.NpcLoc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.openrsc.editor.EditorCanvas.TILE_SIZE;

/**
 * @author xEnt/Vrunk/Peter the Properties/values that each RSC Tile holds.
 */

public class Tile {

    /**
     * @param sh - the Instance to the new Shape.
     */
    public void setShape(Shape sh) {
        this.shape = sh;
    }

    /**
     * @return the Instance to the Shape (Rectangle)
     */
    public Shape getShape() {
        return this.shape;
    }

    /**
     * @return the groundElevation
     */
    public byte getGroundElevation() {
        return groundElevation;
    }

    /**
     * @return the groundElevation integer
     */
    public int getGroundElevationInt() {
        return groundElevation & 0xff;
    }

    /**
     * @param groundElevation the groundElevation to set
     */
    public void setGroundElevation(byte groundElevation) {
        this.groundElevation = groundElevation;
    }

    /**
     * @return the groundTexture
     */
    public byte getGroundTexture() {
        return groundTexture;
    }

    /**
     * @return the groundTexture int
     */
    public int getGroundTextureInt() {
        return groundTexture & 0xff;
    }

    /**
     * @param groundTexture the groundTexture to set
     */
    public void setGroundTexture(byte groundTexture) {
        this.groundTexture = groundTexture;
    }

    /**
     * @return the groundOverlay
     */
    public byte getGroundOverlay() {
        return groundOverlay;
    }

    public int getGroundOverlayInt() {
        return groundOverlay & 0xff;
    }

    /**
     * @param groundOverlay the groundOverlay to set
     */
    public void setGroundOverlay(byte groundOverlay) {
        this.groundOverlay = groundOverlay;
    }

    /**
     * @return - gets the assigned lane given when tiles were created on the
     * map.
     */
    public int getLane() {
        return this.lane;
    }

    /**
     * @return - the position inside the lane
     */
    public int getPosition() {
        return this.position;
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
    public int getHorizontalWallInt() {
        return rightBorderWall & 0xff;
    }

    /**
     * @param rightBorderWall the horizontalWall to set
     */
    public void setRightBorderWall(byte rightBorderWall) {
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
    public int getVerticalWallInt() {
        return topBorderWall & 0xff;
    }

    /**
     * @param topBorderWall the verticalWall to set
     */

    public void setTopBorderWall(byte topBorderWall) {
        this.topBorderWall = topBorderWall;
    }

    /**
     * @return the diagonalWalls
     */
    public int getDiagonalWalls() {
        return diagonalWalls;
    }

    /**
     * @return the diagonalWalls
     */
    public int getDiagonalWallsInt() {
        return diagonalWalls & 0xff;
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

    /**
     * Sets the tile lane
     *
     * @param lane
     */
    public void setLane(int lane) {
        this.lane = lane;
    }

    /**
     * Sets the position in the lane.
     *
     * @param pos - the given position
     */
    public void setPosition(int pos) {
        this.position = pos;
    }

    /**
     * @return the instance of the GameObjectLoc, holding RSC Object tile
     * properties
     */
    public GameObjectLoc getTileObject() {
        return this.objectLoc;
    }

    /**
     * @return the instance of the Item object, holding RSC item properties on
     * the tile.
     */
    public ItemLoc getTileItem() {
        return this.itemLoc;
    }

    /**
     * @return the instance of the NPC object, holding RSC npc properties for
     * the tile.
     */
    public NpcLoc getTileNpc() {
        return this.npcLoc;
    }

    /**
     * Renders the Tile's properties to the back buffer to complete the render.
     *
     * @param offscreenGraphics - the backbuffer.
     */
    public void renderTile(Graphics2D offscreenGraphics) {
        // paints Tile ground data.
        if (this.getGroundTextureInt() >= 0) {
            int ourb = this.getGroundTextureInt();
            offscreenGraphics.setColor(Util.MAP_BRIGHTNESS_LIGHT ? Util.colorArray[ourb] : Util.colorArray[ourb]
                    .darker().darker());
            offscreenGraphics.fill(this.getShape());
            offscreenGraphics.draw(this.getShape());
        }

        // paints Tile ground data (Paths/roads etc, things on top of the
        // original data)
        if (Util.getOverlay.get(this.getGroundOverlay()) != null && this.getGroundOverlay() >= 0) {
            offscreenGraphics.setColor(Util.getOverlay.get(this.getGroundOverlay()));
            offscreenGraphics.fill(this.getShape());
            offscreenGraphics.draw(this.getShape());
        }
        // paints Tile wall color (Vertical) + the line to show a wall is there.
        if (Util.getVerticalWallColor.get(this.getTopBorderWall()) != null) {
            EditorCanvas.drawLine(this, LineLocation.BORDER_TOP, Util.WALL_OUTLINE_COLOR);
        }
        // paints Tile wall color (Horizontal) + line to show a wall is there.
        if (Util.getHorizontalWallColor.get(this.getRightBorderWall()) != null) {
            EditorCanvas.drawLine(this, LineLocation.BORDER_RIGHT, Util.WALL_OUTLINE_COLOR);
        }
        // paints Diagonal walls.
        if (Util.getDiagonalWallColorS.get(this.getDiagonalWallsInt()) != null) {
            EditorCanvas.drawLine(this, LineLocation.DIAGONAL_FROM_TOP_RIGHT, Util.WALL_OUTLINE_COLOR);
        }
        if (Util.getDiagonalWallColorW.get(this.getDiagonalWallsInt()) != null) {
            EditorCanvas.drawLine(this, LineLocation.DIAGONAL_FROM_TOP_LEFT, Util.WALL_OUTLINE_COLOR);
        }
        /*
         * this is the Outline around the selected Tile.
         */
        if (Util.selectedTile != null && Util.selectedTile.equals(this)) {
            offscreenGraphics.setColor(Color.GREEN);
            Shape rec = new Rectangle(this.getX() + 1, this.getY(), TILE_SIZE - 1, TILE_SIZE - 1);
            offscreenGraphics.draw(rec);
        }

        // Objects (Cyan)
        if (Util.showNpcs) {
            if (Util.gameObjectLocationMap.get(Util.getRSCCoords(this)) != null) {
                this.objectLoc = Util.gameObjectLocationMap.get(Util.getRSCCoords(this));
                int size = 8;
                Shape rec = new Rectangle(this.getX() + 1 + size / 2, this.getY() + size / 2, TILE_SIZE - 1
                        - size, TILE_SIZE - 1 - size);
                offscreenGraphics.setColor(Color.CYAN);
                offscreenGraphics.fill(rec);
                offscreenGraphics.draw(rec);
            }
            if (Util.itemLocationMap.get(Util.getRSCCoords(this)) != null) {
                this.itemLoc = Util.itemLocationMap.get(Util.getRSCCoords(this));
                int size = 8;
                Shape rec = new Rectangle(this.getX() + 1 + size / 2, this.getY() + size / 2, TILE_SIZE - 1
                        - size, TILE_SIZE - 1 - size);
                offscreenGraphics.setColor(Color.RED);
                offscreenGraphics.fill(rec);
                offscreenGraphics.draw(rec);
            }
            if (Util.npcLocationMap.get(Util.getRSCCoords(this)) != null) {
                this.npcLoc = Util.npcLocationMap.get(Util.getRSCCoords(this));
                int size = 8;
                Shape rec = new Rectangle(this.getX() + 1 + size / 2, this.getY() + size / 2, TILE_SIZE - 1
                        - size, TILE_SIZE - 1 - size);
                offscreenGraphics.setColor(Color.YELLOW);
                offscreenGraphics.fill(rec);
                offscreenGraphics.draw(rec);
            }
        }

        // Roofs
        if (Util.showRoofs && this.getRoofTexture() == (byte) 1) {
            offscreenGraphics.setColor(Color.ORANGE);
            Shape rec = new Rectangle(this.getX() + 1, this.getY(), TILE_SIZE - 1, TILE_SIZE - 1);
            offscreenGraphics.draw(rec);

        }

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

    private int lane = 0;
    private int position = 0;
    private GameObjectLoc objectLoc;
    private ItemLoc itemLoc;
    private NpcLoc npcLoc;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            return ((Tile) obj).getRSCCoords().equals(this.getRSCCoords());
        }
        return false;
    }


}
