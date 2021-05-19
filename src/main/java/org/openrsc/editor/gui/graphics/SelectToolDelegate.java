package org.openrsc.editor.gui.graphics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EditorToolSelectedEvent;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.action.ConvertPathToSelectionAction;
import org.openrsc.editor.event.action.CreateBuildingAction;
import org.openrsc.editor.event.action.GenerateLandscapeAction;
import org.openrsc.editor.event.selection.SelectRegionUpdateEvent;
import org.openrsc.editor.gui.graphics.stroke.DashedStrokeGenerator;
import org.openrsc.editor.gui.graphics.visitor.CreateBuildingVisitorListener;
import org.openrsc.editor.gui.graphics.visitor.GenerateLandscapeVisitorListener;
import org.openrsc.editor.gui.graphics.visitor.PathVisitor;
import org.openrsc.editor.model.EditorTool;
import org.openrsc.editor.model.SelectRegion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class SelectToolDelegate extends ToolDelegate {
    private static final EventBus eventBus = EventBusFactory.getEventBus();

    private Point dragOrigin = null;
    private SelectRegion selectRegion;
    private final EditorCanvas editorCanvas;
    private final DashedStrokeGenerator dashedStrokeGenerator;

    public SelectToolDelegate(EditorCanvas editorCanvas) {
        super(EditorTool.SELECT);
        this.editorCanvas = editorCanvas;
        this.dashedStrokeGenerator = new DashedStrokeGenerator();
        eventBus.register(this);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        setSelectRegion(null);
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        dragOrigin = editorCanvas.snapPixelPointToGridPoint(evt.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        dragOrigin = null;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        if (dragOrigin != null) {
            Point dragEnd = editorCanvas.snapPixelPointToGridPoint(evt.getPoint());

            // Reorient to 2 points where start.x,y < end.x,y
            int minX = Math.min(dragOrigin.x, dragEnd.x);
            int minY = Math.min(dragOrigin.y, dragEnd.y);
            int maxX = Math.max(dragOrigin.x, dragEnd.x);
            int maxY = Math.max(dragOrigin.y, dragEnd.y);
            Point upperR = new Point(minX, minY);
            Point lowerR = new Point(minX, maxY);
            Point lowerL = new Point(maxX, maxY);
            Point upperL = new Point(maxX, minY);

            setSelectRegion(
                    SelectRegion.builder()
                            .point(upperR)
                            .point(lowerR)
                            .point(lowerL)
                            .point(upperL)
                            // Complete the polygon by drawing back to the origin
                            .point(upperR)
                            .build()
            );
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void render(Graphics2D g) {
        if (selectRegion != null) {
            List<Point> points = selectRegion.getPoints();
            Polygon polygon = Util.constructPolygon(
                    points.stream().map(editorCanvas::gridPointToPixelPoint).collect(Collectors.toList())
            );
//            for (Point point : points) {
//                Optional.ofNullable(editorCanvas.getTileByGridCoords(point.x, point.y))
//                        .ifPresent(editorCanvas::drawTileBorder);
//            }

            g.setColor(Color.WHITE);
            g.setStroke(dashedStrokeGenerator.get());
            g.draw(polygon);
        }
    }

    @Override
    public void onToolMount() {

    }

    @Override
    public void onToolUnmount() {

    }

    public void setSelectRegion(SelectRegion selectRegion) {
        this.selectRegion = selectRegion;
        eventBus.post(
                SelectRegionUpdateEvent.builder()
                        .selectRegion(selectRegion)
                        .selectionPresent(selectRegion != null)
                        .build()
        );
    }

    @Subscribe
    public void onConvertPathToSelection(ConvertPathToSelectionAction action) {
        eventBus.post(new EditorToolSelectedEvent(EditorTool.SELECT));
        setSelectRegion(SelectRegion.builder().points(action.getPoints()).build());
    }

    @Subscribe
    public void onCreateBuildingAction(CreateBuildingAction evt) {
        PathVisitor pathVisitor = new PathVisitor(editorCanvas);
        Thread t = new Thread(() -> pathVisitor.visit(
                evt.getSelectRegion().getPoints(),
                new CreateBuildingVisitorListener(evt.getConfiguration())
        ));
        t.start();
    }

    @Subscribe
    public void onGenerateLandscapeAction(GenerateLandscapeAction evt) {
        PathVisitor pathVisitor = new PathVisitor(editorCanvas);
        Thread t = new Thread(() -> pathVisitor.visit(
                evt.getSelectRegion().getPoints(),
                new GenerateLandscapeVisitorListener(evt.getConfiguration())
        ));
        t.start();
    }
}
