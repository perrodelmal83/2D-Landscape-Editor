package org.openrsc.editor.gui.graphics.visitor;

import org.openrsc.editor.Util;
import org.openrsc.editor.gui.graphics.EditorCanvas;
import org.openrsc.editor.model.Tile;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathVisitor {
    private final EditorCanvas editorCanvas;

    private Tile debugTile = null;
    private List<Point> debugPoints = new ArrayList<>();

    public PathVisitor(EditorCanvas editorCanvas) {
        this.editorCanvas = editorCanvas;
    }

    public void render() {
        debugPoints.stream().map(editorCanvas::getTileByGridCoords).forEach(tile -> {
            editorCanvas.drawTileBorder(tile, Color.PINK);
        });
        if (debugTile != null) {
            editorCanvas.drawTileBorder(debugTile, Color.GREEN);
        }
    }

    public void visit(List<Point> gridPoints, PathVisitorListener listener) {
        // Walls
        List<Point> diagWallPoints = new ArrayList<>();
        for (int i = 0; i < gridPoints.size() - 1; i++) {
            Point pointA = gridPoints.get(i);
            Point pointB = gridPoints.get(i + 1);
            diagWallPoints.addAll(
                    traverseLine(listener, pointA, pointB)
            );
        }

        // Floors
        List<Point> pointsContainedByPolygon = new ArrayList<>();
        Polygon polygon = Util.constructPolygon(gridPoints);
        for (int i = 0; i < EditorCanvas.GRID_SIZE; i++) {
            for (int j = 0; j < EditorCanvas.GRID_SIZE; j++) {
                Point p = new Point(i, j);
                if (polygon.contains(p)) {
                    pointsContainedByPolygon.add(p);
                }
            }
        }
        pointsContainedByPolygon.addAll(diagWallPoints);

        List<Tile> tilesToApply = pointsContainedByPolygon.stream()
                .map(editorCanvas::getTileByGridCoords)
                .collect(Collectors.toList());


        tilesToApply.forEach(listener::onFillTileVisited);
        listener.onComplete(tilesToApply);
    }

    public List<Point> traverseLine(PathVisitorListener listener, Point a, Point b) {
        List<Point> diagWallPoints = new ArrayList<>();
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
                    Point tilePoint = new Point(currentPoint.x, currentPoint.y);
                    Tile updatedTile = editorCanvas.getTileByGridCoords(tilePoint.x, tilePoint.y);
                    debugTile = updatedTile;
                    listener.onEastWallVisited(updatedTile);
//                    TemplateUtil.applyBrush(updatedTile, configuration.getEastWall());
                    currentPoint = new Point(currentPoint.x, currentPoint.y + 1);
                }
            } else if (deltaY == 0) {
                Point currentPoint = new Point(Math.min(a.x, b.x), a.y);
                Point destinationPoint = new Point(Math.max(a.x, b.x), a.y);
                while (!currentPoint.equals(destinationPoint)) {
                    Point tilePoint = new Point(currentPoint.x, currentPoint.y);
                    Tile updatedTile = editorCanvas.getTileByGridCoords(tilePoint.x, tilePoint.y);
                    debugTile = updatedTile;
                    listener.onNorthWallVisited(updatedTile);
//                    TemplateUtil.applyBrush(updatedTile, configuration.getNorthWall());
                    currentPoint = new Point(currentPoint.x + 1, currentPoint.y);
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
                    debugTile = editorCanvas.getTileByGridCoords(currentPoint.x, currentPoint.y);
                    Point nextPoint = new Point(
                            (int) (calculatedX + (steps * slopeX)) + xAdjuster,
                            (int) (calculatedY + (steps * slopeY)) + yAdjuster
                    );

                    Point tilePoint = new Point(currentPoint.x, currentPoint.y);
                    Tile updatedTile = editorCanvas.getTileByGridCoords(tilePoint.x, tilePoint.y);

                    if (steps == 0 && (slopeX < 0 || slopeY < 0)) {
                        currentPoint = nextPoint;
                        continue;
                    }

                    if (nextPoint.x != currentPoint.x && nextPoint.y != currentPoint.y) {
                        // Both x and y changed, diagonal wall

                        if (slopeX / slopeY < 0) {
                            listener.onBackwardDiagonalVisited(updatedTile);
//                            TemplateUtil.applyBrush(updatedTile, configuration.getReverseDiagonalWall());
                        } else {
                            listener.onForwardDiagonalVisited(updatedTile);
//                            TemplateUtil.applyBrush(updatedTile, configuration.getDiagonalWall());
                        }
                        diagWallPoints.add(tilePoint);

                    } else if (nextPoint.x != currentPoint.x) {
                        // Only x changed, horizontal wall
                        if (slopeY < 0) {
                            updatedTile = editorCanvas.getTileByGridCoords(tilePoint.x, tilePoint.y + 1);
                        }
                        listener.onNorthWallVisited(updatedTile);
//                        TemplateUtil.applyBrush(updatedTile, configuration.getNorthWall());
                    } else if (nextPoint.y != currentPoint.y) {
                        // Only y changed, vertical wall
                        listener.onEastWallVisited(updatedTile);
//                        TemplateUtil.applyBrush(updatedTile, configuration.getEastWall());
                    }
                    currentPoint = nextPoint;
                }
            }
        } catch (Exception e) {
        }

        Util.sectorModified = true;
        debugTile = null;
        return diagWallPoints;
    }
}
