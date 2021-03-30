package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.Main;
import org.openrsc.editor.Tile;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.gui.MainWindow;
import org.openrsc.editor.model.TerrainProperty;
import org.openrsc.editor.model.TerrainTemplate;

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

    private static final EventBus eventBus = EventBusFactory.getEventBus();

    public TerrainTemplate currentTemplate = TerrainTemplate.builder().build();
    private JFrame frame;
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
    public static final int TILE_SIZE = 16;
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
        eventBus.register(this);
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
            this.frame = frame;
            this.frame.add(panel);
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
                Thread.sleep(GraphicsControls.SLEEP_DELAY_MS);
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

        hoverTiles.forEach(this::applyBrush);
        Util.updateText(tile);
        Util.selectedTile = tile;
        Util.STATE = Util.State.FORCE_FULL_RENDER;
    }

    /**
     * Applies the TileTemplate from the brush
     *
     * @param tile - the new Tile object to update
     */
    private void applyBrush(Tile tile) {
        if (tile != null) {
            currentTemplate.getValues().entrySet().forEach(entry -> {
                TerrainProperty property = entry.getKey();
                Integer value = entry.getValue();

                switch (property) {
                    case GROUND_TEXTURE:
                        tile.setGroundTexture(value.byteValue());
                        break;
                    case GROUND_OVERLAY:
                        tile.setGroundOverlay(value.byteValue());
                        break;
                    case WALL_DIAGONAL:
                        tile.setDiagonalWalls(value);
                    case WALL_EAST:
                        tile.setEastWall(value.byteValue());
                    case WALL_NORTH:
                        tile.setNorthWall(value.byteValue());
                    case GROUND_ELEVATION:
                        tile.setGroundElevation(value.byteValue());
                    case ROOF_TEXTURE:
                        tile.setRoofTexture(value.byteValue());
                }
            });
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

    public void mouseClicked(MouseEvent e) {
        if (Util.STATE == Util.State.RENDER_READY || Util.STATE == Util.State.TILE_NEEDS_UPDATING) {
            paintStampCursor(e.getPoint(), lastButton, Util.stampSize);
            handleTilePaint(e.getPoint(), lastButton, false);
        }
    }

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

    @Subscribe
    public void onTerrainTemplateUpdate(TerrainTemplateUpdateEvent event) {
        this.currentTemplate = event.getTemplate();
    }
}
