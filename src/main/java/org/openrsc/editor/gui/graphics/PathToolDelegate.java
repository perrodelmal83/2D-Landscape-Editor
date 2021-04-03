package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.SelectPathUpdateEvent;
import org.openrsc.editor.event.action.ClearPathAction;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.SelectPath;
import org.openrsc.editor.model.Tile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathToolDelegate extends ToolDelegate {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    private final List<Point> vertices;
    private Point hoverPoint;
    private final EditorCanvas editorCanvas;

    public PathToolDelegate(EditorCanvas canvas) {
        super(EditorTool.DRAW_PATH);
        vertices = new ArrayList<>();
        this.editorCanvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        if (hoverPoint != null) {
            addVertex(hoverPoint);
        }
    }

    @Override
    public void mousePressed(MouseEvent evt) {

    }

    @Override
    public void mouseReleased(MouseEvent evt) {

    }

    @Override
    public void mouseEntered(MouseEvent evt) {

    }

    @Override
    public void mouseExited(MouseEvent evt) {
        this.hoverPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent evt) {

    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        this.hoverPoint = editorCanvas.snapPixelPointToGridPoint(evt.getPoint());
    }

    @Override
    public void render(Graphics2D g) {
        // Draw lines from each vertex
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        vertices.stream().map(editorCanvas::gridPointToPixelPoint).reduce((vertex1, vertex2) -> {
            g.drawLine(vertex1.x, vertex1.y, vertex2.x, vertex2.y);
            return vertex2;
        });

        for (Point vertex : vertices) {
            Tile tile = editorCanvas.getTileByGridCoords(vertex.x, vertex.y);
            editorCanvas.drawTileBorder(tile);
        }

        // Draw hovered vertex and line from last vertex
        if (hoverPoint != null) {
            Point hoverPixelPoint = editorCanvas.gridPointToPixelPoint(hoverPoint);

            if (vertices.size() >= 1) {
                Point lastVertex = editorCanvas.gridPointToPixelPoint(vertices.get(vertices.size() - 1));
                g.drawLine(lastVertex.x, lastVertex.y, hoverPixelPoint.x, hoverPixelPoint.y);
            }

            g.setColor(Color.GREEN);
            int size = 6;
            Ellipse2D.Float hoverRectangle = new Ellipse2D.Float(
                    hoverPixelPoint.x - size / 2.0f,
                    hoverPixelPoint.y - size / 2.0f,
                    size,
                    size
            );
            g.fill(hoverRectangle);
            g.draw(hoverRectangle);
        }
    }

    public void addVertex(Point point) {
        this.vertices.add(point);
        postSelectPathUpdateEvent();
    }

    public void clearPath() {
        this.vertices.clear();
        postSelectPathUpdateEvent();
    }

    public void postSelectPathUpdateEvent() {
        eventBus.post(
                SelectPathUpdateEvent.builder()
                        .selectPath(
                                SelectPath.builder()
                                        .points(Collections.unmodifiableList(vertices))
                                        .build()
                        )
                        .isPresent(!vertices.isEmpty())
                        .build()
        );
    }

    @Override
    public void onToolMount() {

    }

    @Override
    public void onToolUnmount() {
        clearPath();
        hoverPoint = null;
    }

    @Subscribe
    public void onClearPathAction(ClearPathAction action) {
        clearPath();
    }
}
