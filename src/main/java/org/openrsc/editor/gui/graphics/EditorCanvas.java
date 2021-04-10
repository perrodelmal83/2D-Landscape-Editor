package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainPresetSelectedEvent;
import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.gui.graphics.visitor.PathVisitor;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * @author xEnt/Vrunk/Peter
 * @info the Class behind the Map editor that does all the dirty work.
 */

public class EditorCanvas extends Canvas implements Runnable {

    private static final EventBus eventBus = EventBusFactory.getEventBus();

    public static final int GRID_SIZE = 48;
    public static final int TILE_SIZE = 16;
    public static final int GRID_PIXEL_SIZE = GRID_SIZE * TILE_SIZE;

    private final TileRenderer tileRenderer;
    private final SelectToolDelegate selectToolDelegate;
    private final PathToolDelegate pathToolDelegate;
    private final StampToolDelegate stampToolDelegate;
    private final ToolDelegate inspectToolDelegate;
    private final PlaceDoorWindowDelegate placeDoorWindowDelegate;

    public static Tile[][] tileGrid;
    private Graphics2D g2d;
    private BufferedImage offscreenImage;
    public static Graphics2D offscreenGraphics;
    private ToolDelegate currentToolDelegate;

    // Controlled by tool delegates
    public TerrainTemplate currentTemplate = TerrainTemplate.builder().build();
    private PathVisitor pathVisitor;

    public EditorCanvas() {
        super();
        this.selectToolDelegate = new SelectToolDelegate(this);
        this.pathToolDelegate = new PathToolDelegate(this);
        this.stampToolDelegate = new StampToolDelegate(this);
        this.inspectToolDelegate = new InspectToolDelegate(this);
        this.placeDoorWindowDelegate = new PlaceDoorWindowDelegate(this);

        init();
        setCurrentTool(selectToolDelegate);

        tileRenderer = new TileRenderer();
        eventBus.register(this);
    }

    private void setCurrentTool(ToolDelegate toolDelegate) {
        if (currentToolDelegate != null) {
            removeMouseListener(currentToolDelegate);
            removeMouseMotionListener(currentToolDelegate);
        }

        currentToolDelegate = toolDelegate;

        addMouseListener(currentToolDelegate);
        addMouseMotionListener(currentToolDelegate);
    }

    private void init() {
        try {
            tileGrid = new Tile[GRID_SIZE][GRID_SIZE];
            setVisible(true);
            setLocation(0, 60);
            setSize(new Dimension(GRID_PIXEL_SIZE, GRID_PIXEL_SIZE));
            setBackground(Color.BLACK);
            Util.prepareData(null);
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
        Graphics g = getGraphics();
        g2d = (Graphics2D) g;

        initializeBackBufferGraphics();
        try {
            int curTime = 0;
            while (true) {

                if (Util.STATE == Util.State.LOADED || Util.STATE == Util.State.CHANGING_SECTOR) {
                    Util.unpack(Util.sectorX, Util.sectorY, Util.sectorH);
                    setTiles();
                    render();
                } else if (Util.STATE == Util.State.FORCE_FULL_RENDER) {
                    Util.STATE = Util.State.RENDER_READY;
                    render();
                } else {
                    render();
                }
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
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Optional.ofNullable(tileGrid[i][j]).ifPresent(
                        tile -> tileRenderer.renderTile(tile, offscreenGraphics)
                );
            }
        }

        // Call render function on selected tool delegate
        if (currentToolDelegate != null) {
            currentToolDelegate.render(offscreenGraphics);
        }

        g2d.drawImage(offscreenImage, 0, 0, this);
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
                    tile.setX(GRID_PIXEL_SIZE - (i + 1) * TILE_SIZE);
                    tile.setY(j * TILE_SIZE);
                    tile.setID(count);
                    tile.setGridX(i);
                    tile.setGridY(j);
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

    void initializeBackBufferGraphics() {
        if (offscreenImage == null) {
            offscreenImage = (BufferedImage) createImage(GRID_PIXEL_SIZE, GRID_PIXEL_SIZE);
            offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        }
    }

    public Tile getTileByGridCoords(Point p) {
        return getTileByGridCoords(p.x, p.y);
    }

    public Tile getTileByGridCoords(final int x, final int y) {
        try {
            return EditorCanvas.tileGrid[x][y];
        } catch (Exception ex) {
            return null;
        }
    }

    public static void drawLine(int x1, int y1, int x2, int y2, Color color, int thickness) {
        offscreenGraphics.setColor(color);
        offscreenGraphics.setStroke(new BasicStroke(thickness));
        offscreenGraphics.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    public void drawTileBorder(Tile tile) {
        drawTileBorder(tile, Color.YELLOW);
    }

    public void drawTileBorder(Tile tile, Color color) {
        drawLine(tile, LineLocation.BORDER_TOP, color, 2);
        drawLine(tile, LineLocation.BORDER_BOTTOM, color, 2);
        drawLine(tile, LineLocation.BORDER_RIGHT, color, 2);
        drawLine(tile, LineLocation.BORDER_LEFT, color, 2);
    }

    public static void drawLine(Tile tile, LineLocation lineLocation, Color color, int thickness) {
        switch (lineLocation) {
            case BORDER_TOP:
                drawLine(
                        tile.getX(),
                        tile.getY(),
                        tile.getX() + TILE_SIZE,
                        tile.getY(),
                        color,
                        thickness
                );
                break;
            case BORDER_RIGHT:
                drawLine(
                        tile.getX() + TILE_SIZE,
                        tile.getY(),
                        tile.getX() + TILE_SIZE,
                        tile.getY() + TILE_SIZE,
                        color,
                        thickness
                );
                break;
            case BORDER_BOTTOM:
                drawLine(
                        tile.getX(),
                        tile.getY() + TILE_SIZE,
                        tile.getX() + TILE_SIZE,
                        tile.getY() + TILE_SIZE,
                        color,
                        thickness
                );
                break;
            case BORDER_LEFT:
                drawLine(
                        tile.getX(),
                        tile.getY(),
                        tile.getX(),
                        tile.getY() + TILE_SIZE,
                        color,
                        thickness
                );
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                drawLine(
                        tile.getX(),
                        tile.getY(),
                        tile.getX() + TILE_SIZE,
                        tile.getY() + TILE_SIZE,
                        color,
                        thickness
                );
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawLine(
                        tile.getX() + TILE_SIZE,
                        tile.getY(),
                        tile.getX(),
                        tile.getY() + TILE_SIZE,
                        color,
                        thickness
                );
                break;
        }
    }

    protected Point snapPixelPointToGridPoint(Point pixelPoint) {
        int halfTileSize = TILE_SIZE / 2;
        Point p = new Point(pixelPoint.x - halfTileSize, pixelPoint.y + halfTileSize);
        return Util.mouseCoordsToGridCoords(p);
    }

    protected Point gridPointToPixelPoint(Point gridPoint) {
        return new Point(GRID_PIXEL_SIZE - gridPoint.x * TILE_SIZE, gridPoint.y * TILE_SIZE);
    }

    public void applyBrush(Tile tile) {
        TemplateUtil.applyBrush(tile, currentTemplate);
    }

    @Subscribe
    public void onToolSelected(EditorToolSelectedEvent event) {
        EditorTool selectedTool = event.getEditorTool();
        switch (selectedTool) {
            case SELECT:
                setCurrentTool(selectToolDelegate);
                break;
            case DRAW_PATH:
                setCurrentTool(pathToolDelegate);
                break;
            case STAMP:
                setCurrentTool(stampToolDelegate);
                break;
            case INSPECT:
                setCurrentTool(inspectToolDelegate);
                break;
            case PLACE_DOOR_WINDOW:
                setCurrentTool(placeDoorWindowDelegate);
                break;
        }
    }


    @Subscribe
    public void onTerrainTemplateUpdate(TerrainTemplateUpdateEvent event) {
        this.currentTemplate = TemplateUtil.merge(currentTemplate, event);
    }

    @Subscribe
    public void onTerrainPresetSelected(TerrainPresetSelectedEvent event) {
        this.currentTemplate = event.getTemplate();
    }

    public static int roundToNearest10(int num) {
        float f = num / 10f;
        return Math.round(f) * 10;
    }
}
