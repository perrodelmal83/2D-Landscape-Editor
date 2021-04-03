package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainPresetSelectedEvent;
import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.event.action.CreateBuildingAction;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.definition.WallDefinition;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import javax.swing.JOptionPane;
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
import java.util.List;
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

    public static Tile[][] tileGrid;
    private Graphics2D g2d;
    private BufferedImage offscreenImage;
    public static Graphics2D offscreenGraphics;
    private ToolDelegate currentToolDelegate;

    // Controlled by tool delegates
    public TerrainTemplate currentTemplate = TerrainTemplate.builder().build();

    public EditorCanvas() {
        super();
        this.selectToolDelegate = new SelectToolDelegate(this);
        this.pathToolDelegate = new PathToolDelegate(this);
        this.stampToolDelegate = new StampToolDelegate(this);
        this.inspectToolDelegate = new InspectToolDelegate(this);

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

        if (debugTile != null) {
            drawTileBorder(debugTile, Color.GREEN);
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

    protected Tile getTileByGridCoords(final int x, final int y) {
        try {
            return EditorCanvas.tileGrid[x][y];
        } catch (Exception ex) {
            return null;
        }
    }

    public static void drawLine(int x1, int y1, int x2, int y2, Color color) {
        offscreenGraphics.setColor(color);
        offscreenGraphics.setStroke(new BasicStroke(2));
        offscreenGraphics.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    public void drawTileBorder(Tile tile) {
        drawTileBorder(tile, Color.YELLOW);
    }

    public void drawTileBorder(Tile tile, Color color) {
        drawLine(tile, LineLocation.BORDER_TOP, color);
        drawLine(tile, LineLocation.BORDER_BOTTOM, color);
        drawLine(tile, LineLocation.BORDER_RIGHT, color);
        drawLine(tile, LineLocation.BORDER_LEFT, color);
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
        }
    }

    @Subscribe
    public void onCreateBuildingAction(CreateBuildingAction evt) {
        String input = JOptionPane.showInputDialog("Enter wall int");
        WallDefinition wallDefinition = WallDefinition.NORMAL.get(Integer.parseInt(input));
        CreateBuildingAction wallTest = CreateBuildingAction
                .builder()
                .buildingFloor(TerrainTemplate.builder().value(TerrainProperty.GROUND_OVERLAY, 1).build())
                .eastWall(TerrainTemplate.builder().value(TerrainProperty.WALL_EAST, wallDefinition.getEastWall()).build())
                .northWall(TerrainTemplate.builder().value(TerrainProperty.WALL_NORTH, wallDefinition.getNorthWall()).build())
                .diagonalWall(TerrainTemplate.builder().value(TerrainProperty.WALL_DIAGONAL, wallDefinition.getDiagonalWallForward()).build())
                .reverseDiagonalWall(TerrainTemplate.builder().value(TerrainProperty.WALL_DIAGONAL, wallDefinition.getDiagonalWallBackward()).build())
                .build();
        Thread t = new Thread(() -> traversePoints(
                evt.getSelectRegion().getPoints(),
                wallTest
        ));
        t.start();
    }

    public Tile debugTile = null;

    public void traversePoints(List<Point> gridPoints, CreateBuildingAction action) {
        // Walls
        gridPoints.stream().reduce((pointA, pointB) -> {
            traverseLine(action, pointA, pointB);
            return pointB;
        });

        // Floors
        // Figure out points contained by polygon... seems difficult
    }

    public void traverseLine(CreateBuildingAction action, Point a, Point b) {
        try {
            double deltaY = b.getY() - a.getY();
            double deltaX = b.getX() - a.getX();
            if (deltaX == 0) {
                // Vertical line
                // Don't use mathematical slope (divide by zero)
                // Just simply iterate with y+1 each step until reach point b
                Point currentPoint = new Point(a.x, Math.min(a.y, b.y));
                Point destinationPoint = new Point(a.x, Math.max(a.y, b.y));
                while (!currentPoint.equals(destinationPoint)) {
                    Tile updatedTile = getTileByGridCoords(currentPoint.x, currentPoint.y);
                    debugTile = updatedTile;
                    TemplateUtil.applyBrush(updatedTile, action.getEastWall());
                    currentPoint = new Point(currentPoint.x, currentPoint.y + 1);
                    Thread.sleep(50);
                }
            } else if (deltaY == 0) {
                Point currentPoint = new Point(Math.min(a.x, b.x), a.y);
                Point destinationPoint = new Point(Math.max(a.x, b.x), a.y);
                while (!currentPoint.equals(destinationPoint)) {
                    Tile updatedTile = getTileByGridCoords(currentPoint.x, currentPoint.y);
                    debugTile = updatedTile;
                    TemplateUtil.applyBrush(updatedTile, action.getNorthWall());
                    currentPoint = new Point(currentPoint.x + 1, currentPoint.y);
                    Thread.sleep(50);
                }
            } else {
                Point currentPoint = new Point(a);
                double calculatedX = currentPoint.x;
                double calculatedY = currentPoint.y;
                int necessarySteps = (int) Math.max(Math.abs(deltaY), Math.abs(deltaX));
                // Express slope in x,y per step
                double slopeX = deltaX / necessarySteps;
                double slopeY = deltaY / necessarySteps;

                /*
                 * Vertex is positioned at the top-right corner of selected tile (origin).
                 * If the slope is negative, the user will expect it to draw from the vertex, away
                 * from the current tile (not inclusive).
                 */
                int xAdjuster = slopeX < 0 ? -1 : 0;
                int yAdjuster = slopeY < 0 ? -1 : 0;
                for (int steps = 0; steps <= necessarySteps; steps++) {
                    debugTile = getTileByGridCoords(currentPoint.x, currentPoint.y);
                    Point nextPoint = new Point(
                            (int) (calculatedX + .5 + (steps * slopeX)) + xAdjuster,
                            (int) (calculatedY + .5 + (steps * slopeY)) + yAdjuster
                    );

                    Point tilePoint = new Point(currentPoint.x, currentPoint.y);
                    Tile updatedTile = getTileByGridCoords(tilePoint.x, tilePoint.y);

                    if (steps == 0 && (slopeX < 0 || slopeY < 0)) {
                        currentPoint = nextPoint;
                        continue;
                    }

                    if (nextPoint.x != currentPoint.x && nextPoint.y != currentPoint.y) {
                        if (slopeX / slopeY < 0) {
                            TemplateUtil.applyBrush(updatedTile, action.getReverseDiagonalWall());
                        } else {
                            TemplateUtil.applyBrush(updatedTile, action.getDiagonalWall());
                        }
                        // Both x and y changed, diagonal wall
                    } else if (nextPoint.x != currentPoint.x) {
                        if (slopeY < 0) {
                            updatedTile = getTileByGridCoords(tilePoint.x, tilePoint.y + 1);
                        }
                        TemplateUtil.applyBrush(updatedTile, action.getNorthWall());
                        // Only x changed, horizontal wall
                    } else if (nextPoint.y != currentPoint.y) {
                        TemplateUtil.applyBrush(updatedTile, action.getEastWall());
                        // Only y changed, vertical wall
                    }
                    Thread.sleep(300);
                    currentPoint = nextPoint;
                }
            }
        } catch (Exception e) {
        }

        Util.sectorModified = true;
        debugTile = null;
    }

    @Subscribe
    public void onTerrainTemplateUpdate(TerrainTemplateUpdateEvent event) {
        this.currentTemplate = TemplateUtil.merge(currentTemplate, event);
    }

    @Subscribe
    public void onTerrainPresetSelected(TerrainPresetSelectedEvent event) {
        this.currentTemplate = event.getTemplate();
    }
}
