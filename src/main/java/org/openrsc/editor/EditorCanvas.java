package org.openrsc.editor;

import org.openrsc.editor.gui.MainWindow;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xEnt/Vrunk/Peter
 * @info the Class behind the Map editor that does all the dirty work.
 */

public class EditorCanvas implements Runnable, MouseListener, MouseMotionListener {

    JFrame ourFrame;
    private Graphics2D g2d;
    public static JPanel panel;
    private BufferedImage offscreenImage;
    public static Graphics2D offscreenGraphics;
    private Dimension offscreenDimension;
    /**
     * 2-Dimensional array used to hold Tile objects for easy access.
     */
    public static Tile[][] tileGrid;
    /**
     * The size of the tileGrid.
     */
    public static final int GRID_SIZE = 48;
    /**
     * The number of x/y coordinates in a tile.
     */
    public static final int TILE_SIZE = 11;
    /**
     * The total number of x/y coordinates in the tileGrid.
     */
    public static final int NUM_TILES = GRID_SIZE * TILE_SIZE;
    private List<Tile> hoverTiles = Collections.emptyList();

    /**
     * @param frame - the instance of the JFrame from the GUI class. the Class
     *              Constructor, that parses the frame down to the initialization
     *              process.
     */
    public EditorCanvas(final JFrame frame) {
        init(frame);
    }

    /**
     * @param frame - the instance of the JFrame. the Initialization process.
     */
    @SuppressWarnings("unchecked")
    private void init(JFrame frame) {
        try {
            tileGrid = new Tile[GRID_SIZE][GRID_SIZE];
            panel = new JPanel();
            Util.initData();
            panel.addMouseListener(this);
            panel.setVisible(true);
            panel.addMouseMotionListener(this);
            panel.setLocation(-10, 0);
            panel.setSize(NUM_TILES + TILE_SIZE, NUM_TILES);
            panel.setBackground(Color.BLACK);
            ourFrame = frame;
            ourFrame.setSize(ourFrame.getSize().width, ourFrame.getSize().height - 19);
            ourFrame.add(panel);
            Util.prepareData();

        } catch (Exception e) {
            Util.error(e);
        }
    }

    /**
     * the Thread's entry point + the Main loop, everything has been
     * Initialized, This is the main loop that records FPS, renders the canvas
     * and sleeps thread. But first sets the Tile array for all it's properties.
     * <p>
     * Loop is now done like this, to allow this thread to update a selected
     * tile instead of waiting for the threads sleep to finish to update it. the
     * threads sleep is set in increments.
     */
    public void run() {
        Graphics g = panel.getGraphics();
        g2d = (Graphics2D) g;

        try {
            int curTime = 0;
            while (true) {

                if (Util.STATE == Util.State.TILE_NEEDS_UPDATING) {
                    Util.STATE = Util.State.RENDER_READY;
                    Util.oldSelectedTile.renderTile(offscreenGraphics);
                    Util.selectedTile.renderTile(offscreenGraphics);
                    g2d.drawImage(offscreenImage, 0, 0, panel);
                } else if (Util.STATE == Util.State.LOADED || Util.STATE == Util.State.CHANGING_SECTOR) {
                    Util.unpack(Util.sectorX, Util.sectorY, Util.sectorH);
                    setTiles();
                    render();
                } else if (Util.STATE == Util.State.FORCE_FULL_RENDER) {
                    Util.STATE = Util.State.RENDER_READY;
                    render();
                }

                if (curTime >= (1000 / Util.FPS)) {
                    curTime = 0;
                    if (Util.STATE == Util.State.RENDER_READY) {
                        render();
                    }
                }
                Main.mainWindow.setTitle("RSC Landscape Editor" + " - " + " Sector: " + "h"
                        + Util.sectorH + "x" + Util.sectorX + "y" + Util.sectorY);
                curTime += Util.THREAD_DELAY;
                Thread.sleep(Constants.SLEEP_DELAY_MS);
            }
        } catch (Exception e) {
            Util.error(e);
        }
    }

    /**
     * this Method is called <FPS> a second. This is where the painting is done.
     * Double buffering is Applied to this, Captures the old image, updates the
     * Image with the new Render behind the scenes, the paints it over the old
     * one This stops Viewing problems from occurring, ripping/tearing etc.
     */
    public void render() {
        renderInit();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                tileGrid[i][j].renderTile(offscreenGraphics);
            }
        }
        hoverTiles.stream()
                .filter(Objects::nonNull)
                .forEach(tile -> {
                    drawLine(tile, LineLocation.BORDER_TOP, Color.YELLOW);
                    drawLine(tile, LineLocation.BORDER_BOTTOM, Color.YELLOW);
                    drawLine(tile, LineLocation.BORDER_RIGHT, Color.YELLOW);
                    drawLine(tile, LineLocation.BORDER_LEFT, Color.YELLOW);
                });
        g2d.drawImage(offscreenImage, 0, 0, panel);
    }

    /**
     * Set out the Tile sizes/locations in the array.
     */
    public void setTiles() {
        try {
            int count = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    Tile tile = new Tile();
                    tileGrid[i][j] = tile;
                    tile.setX(NUM_TILES - i * TILE_SIZE);
                    tile.setY(j * TILE_SIZE);
                    tile.setID(count);
                    tile.setLane(i);
                    tile.setPosition(j);
                    tile = tile.unpack(Util.buffer, tile);
                    tile.setShape(new Rectangle(tile.getX(), tile.getY(), TILE_SIZE, TILE_SIZE));
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.STATE = Util.State.RENDER_READY;
        }
    }

    /**
     * Initialize the BackBuffer.
     */
    void renderInit() {
        Dimension currentSize = panel.getSize();
        if (offscreenImage == null || !currentSize.equals(offscreenDimension)) {
            offscreenImage = (BufferedImage) panel.createImage(currentSize.width, panel.getSize().height);
            offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
            offscreenDimension = currentSize;
        }
    }

    public static int lastButton = 0;

    private void paintStampCursor(Point cursorCoords, int mouseButton, int stampSize) {
        List<Tile> tiles = new ArrayList<>(stampSize * stampSize);

        // Translate origin back half distance to center main point, then add all points covering stamp size
        Point hoveredGridPosition = Util.mouseCoordsToGridCoords(cursorCoords);
        int xOrigin = hoveredGridPosition.x - (stampSize / 2);
        int yOrigin = hoveredGridPosition.y - (stampSize / 2);

        for (int j = 0; j < stampSize; j++) {
            for (int i = 0; i < stampSize; i++) {
                Optional.ofNullable(
                        getTileByGridCoords(xOrigin + i, yOrigin + j)
                ).ifPresent(tiles::add);
            }
        }

        hoverTiles = tiles;
        Util.STATE = Util.State.FORCE_FULL_RENDER;
    }

    private Tile getTileByGridCoords(final int x, final int y) {
        try {
            return EditorCanvas.tileGrid[x][y];
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Handles everything that goes on when selecting a Tile.
     *
     * @param p - Point from the mouse click.
     */
    private void handleTilePaint(Point p, int mouseButton, boolean drag) {

        Tile tile = Util.mapCoordsToGridTile(p.getLocation());
        if (tile == null) {
            return;
        }
        if (Util.selectedTile == null) {
            Util.selectedTile = tile;
            Util.oldSelectedTile = Util.selectedTile;
        }
        Util.oldSelectedTile = Util.selectedTile;
        if (mouseButton == 3) {
            Util.updateText(tile);
            Util.selectedTile = tile;
            Util.STATE = Util.State.TILE_NEEDS_UPDATING;
            return;
        }
        if (!"None".equals(MainWindow.brushes.getSelectedItem())) {
            Util.sectorModified = true;
        }

        hoverTiles.forEach(this::checkPaint);
        Util.updateText(tile);
        Util.selectedTile = tile;
        Util.STATE = Util.State.FORCE_FULL_RENDER;

    }

    /**
     * Checks and handles Weather the Tile needs to be Painted.
     *
     * @param tile - the new Tile object to update
     */
    private void checkPaint(Tile tile) {
        try {
            if (tile != null) {
                final String selected = MainWindow.brushes.getSelectedItem().toString();
                switch (selected) {
                    case "Configure your own":
                        tile.setGroundTexture((byte) MainWindow.textureJS.getValue());
                        tile.setDiagonalWalls(MainWindow.diagonalWallJS.getValue());
                        tile.setTopBorderWall((byte) MainWindow.verticalWallJS.getValue());
                        tile.setRightBorderWall((byte) MainWindow.horizontalWallJS.getValue());
                        tile.setGroundOverlay((byte) MainWindow.overlayJS.getValue());
                        tile.setRoofTexture((byte) MainWindow.roofTextureJS.getValue());
                        tile.setGroundElevation((byte) MainWindow.elevationJS.getValue());
                        break;
                    case "Delete Tile":
                        Util.clearTile(tile);
                        break;
                    case "Remove North Wall":
                        tile.setTopBorderWall((byte) 0);
                        break;
                    case "Remove East Wall":
                        tile.setRightBorderWall((byte) 0);
                        break;
                    case "Remove Diagonal Wall":
                        tile.setDiagonalWalls(0);
                        break;
                    case "Remove Overlay":
                        tile.setGroundOverlay((byte) 0);
                        break;
                    case "Remove Roof":
                        tile.setRoofTexture((byte) 0);
                        break;
                    case "Grey Path":
                        tile.setGroundOverlay((byte) 1);
                        break;
                    case "Water":
                        tile.setGroundOverlay((byte) 2);
                        break;
                    case "Wooden Floor":
                        tile.setGroundOverlay((byte) 3);
                        break;
                    case "Dark Red Bank Floor":
                        tile.setGroundOverlay((byte) 6);
                        break;
                    case "Black Floor":
                        tile.setGroundOverlay((byte) 16);
                        break;
                    case "North Wall(0) -":
                        tile.setTopBorderWall((byte) 15);
                        break;
                    case "East Wall(0) |":
                        tile.setRightBorderWall((byte) 15);
                        break;
                    case "Diagonal Wall(0) /":
                        tile.setDiagonalWalls(1);
                        break;
                    case "North Wall(1) -":
                        tile.setTopBorderWall((byte) 5);
                        break;
                    case "North Wall(2) -":
                        tile.setTopBorderWall((byte) 1);
                        break;
                    case "North Wall(3) -":
                        tile.setTopBorderWall((byte) 7);
                        break;
                    case "North Wall(4) -":
                        tile.setTopBorderWall((byte) 14);
                        break;
                    case "North Wall(5) -":
                        tile.setTopBorderWall((byte) 57);
                        break;
                    case "North Wall(6) -":
                        tile.setTopBorderWall((byte) 16);
                        break;
                    case "North Wall(7) -":
                        tile.setTopBorderWall((byte) 4);
                        break;
                    case "East Wall(1) |":
                        tile.setRightBorderWall((byte) 5);
                        break;
                    case "East Wall(2) |":
                        tile.setRightBorderWall((byte) 1);
                        break;
                    case "East Wall(3) |":
                        tile.setRightBorderWall((byte) 7);
                        break;
                    case "East Wall(4) |":
                        tile.setRightBorderWall((byte) 14);
                        break;
                    case "East Wall(5) |":
                        tile.setRightBorderWall((byte) 57);
                        break;
                    case "East Wall(6) |":
                        tile.setRightBorderWall((byte) 16);
                        break;
                    case "East Wall(7) |":
                        tile.setRightBorderWall((byte) 4);
                        break;
                    case "Diagonal Wall(1) /":
                        tile.setDiagonalWalls(14);
                        break;
                    case "Diagonal Wall(2) /":
                        tile.setDiagonalWalls(3);
                        break;
                    case "Diagonal Wall(3) /":
                        tile.setDiagonalWalls(19);
                        break;
                    case "Diagonal Wall(4) /":
                        tile.setDiagonalWalls(17);
                        break;
                    case "Diagonal Wall(5) /":
                        tile.setDiagonalWalls(5);
                        break;
                    case "Diagonal Wall(6) /":
                        tile.setDiagonalWalls(4);
                        break;
                    case "Diagonal Wall(0) \\":
                        tile.setDiagonalWalls(12004);
                        break;
                    case "Grass":
                        tile.setGroundTexture((byte) 70);
                        break;
                    case "Roof":
                        tile.setRoofTexture((byte) 1);
                        break;
                }
                if (Util.eleReady) {
                    tile.setGroundElevation(Util.newEle);
                }

            }
        } catch (Exception e) {
            Util.error(e);
        }
    }

    public static void drawLine(int x1, int y1, int x2, int y2, Color color) {
        offscreenGraphics.setColor(color);
        offscreenGraphics.setStroke(new BasicStroke(2));
        offscreenGraphics.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    public static void drawLine(Tile tile, LineLocation lineLocation, Color color) {
        switch (lineLocation) {
            case BORDER_TOP:
                drawLine(
                        tile.getX(),
                        tile.getY(),
                        tile.getX() + TILE_SIZE,
                        tile.getY(),
                        color
                );
                break;
            case BORDER_RIGHT:
                drawLine(
                        tile.getX() + TILE_SIZE,
                        tile.getY(),
                        tile.getX() + TILE_SIZE,
                        tile.getY() + TILE_SIZE,
                        color
                );
                break;
            case BORDER_BOTTOM:
                drawLine(
                        tile.getX(),
                        tile.getY() + TILE_SIZE,
                        tile.getX() + TILE_SIZE,
                        tile.getY() + TILE_SIZE,
                        color
                );
                break;
            case BORDER_LEFT:
                drawLine(
                        tile.getX(),
                        tile.getY(),
                        tile.getX(),
                        tile.getY() + TILE_SIZE,
                        color
                );
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                drawLine(
                        tile.getX(),
                        tile.getY(),
                        tile.getX() + TILE_SIZE,
                        tile.getY() + TILE_SIZE,
                        color
                );
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawLine(
                        tile.getX() + TILE_SIZE,
                        tile.getY(),
                        tile.getX(),
                        tile.getY() + TILE_SIZE,
                        color
                );
                break;
        }
    }

    // ONCE AGAIN i failed at getting this to work, i get so confused.
    // Basically what i attempted was to have the Brush over the cursor when u
    // move it
    // like the sims, or age of empires style kinda.

    /*
     * int lastPos = -1; int lastLane = -1; Tile lastTile = null; public void
     * mouseMoved(MouseEvent e) { if(Util.STATE == Util.State.RENDER_READY) {
     * Tile tile = Util.findTileInGrid(e.getPoint().getLocation());
     *
     * if((tile.lane != lastLane && tile.position != lastPos) || lastTile ==
     * null) { if(lastTile == null) { lastPos = tile.position; lastLane =
     * tile.lane; lastTile = tile; } // Revert the old tile back to how it was
     * try {
     *
     * } catch (Exception r) { System.out.println(r); }
     * //tileGrid[lastLane][lastPos].lane = lastTile.lane;
     * tileGrid[lastLane][lastPos] = lastTile;
     *
     * //lastTile.getClass().
     *
     *
     * tile.setGroundOverlay((byte)2); Util.selectedTile = tile; Util.STATE =
     * Util.State.TILE_NEEDS_UPDATING; lastTile = tile; } else {
     *
     * } } }
     */


    /**
     * the Mouse click event.
     */
    public void mouseClicked(MouseEvent e) {
        if (Util.STATE == Util.State.RENDER_READY || Util.STATE == Util.State.TILE_NEEDS_UPDATING) {
            paintStampCursor(e.getPoint(), lastButton, Util.stampSize);
            handleTilePaint(e.getPoint(), lastButton, false);
        }
    }

    /**
     * the mouse Drag event
     */
    public void mouseDragged(MouseEvent e) {
        if (Util.STATE == Util.State.RENDER_READY || Util.STATE == Util.State.TILE_NEEDS_UPDATING) {
            paintStampCursor(e.getPoint(), lastButton, Util.stampSize);
            handleTilePaint(e.getPoint().getLocation(), lastButton, true);
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (Util.STATE == Util.State.RENDER_READY || Util.STATE == Util.State.TILE_NEEDS_UPDATING) {
            paintStampCursor(e.getPoint(), lastButton, Util.stampSize);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
