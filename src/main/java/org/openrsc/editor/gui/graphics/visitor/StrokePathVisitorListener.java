package org.openrsc.editor.gui.graphics.visitor;

import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.configuration.StrokePathConfiguration;
import org.openrsc.editor.model.definition.WallDefinition;
import org.openrsc.editor.model.definition.WallDirection;

import java.util.List;

public class StrokePathVisitorListener extends PathVisitorListener {
    private final WallDefinition wallDefinition;

    public StrokePathVisitorListener(StrokePathConfiguration configuration) {
        this.wallDefinition = configuration.getWallDefinition();
    }

    @Override
    protected void onNorthWallVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, wallDefinition.toTerrainTemplate(WallDirection.NORTH));
    }

    @Override
    protected void onEastWallVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, wallDefinition.toTerrainTemplate(WallDirection.EAST));
    }

    @Override
    protected void onForwardDiagonalVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, wallDefinition.toTerrainTemplate(WallDirection.DIAGONAL_FORWARD));
    }

    @Override
    protected void onBackwardDiagonalVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, wallDefinition.toTerrainTemplate(WallDirection.DIAGONAL_BACKWARD));
    }

    @Override
    protected void onFillTileVisited(Tile tile) {
        // Do nothing, just givin' it the ole rub rub
    }

    @Override
    protected void onComplete(List<Tile> visited) {
        // Do nothing, just givin' it the ole rub rub
    }
}
