package org.openrsc.editor.gui.graphics.visitor;

import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.event.action.CreateBuildingAction;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.util.Comparator;
import java.util.List;

public class CreateBuildingVisitorListener extends PathVisitorListener {
    private final CreateBuildingAction action;

    public CreateBuildingVisitorListener(CreateBuildingAction action) {
        this.action = action;
    }

    @Override
    protected void onNorthWallVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, action.getNorthWall());
    }

    @Override
    protected void onEastWallVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, action.getEastWall());
    }

    @Override
    protected void onForwardDiagonalVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, action.getDiagonalWall());
    }

    @Override
    protected void onBackwardDiagonalVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, action.getReverseDiagonalWall());
    }

    @Override
    protected void onFillTileVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, action.getFloor());
        TemplateUtil.applyBrush(tile, action.getRoof());
    }

    @Override
    protected void onComplete(List<Tile> visited) {
        // Level building to highest selected point
        int maxElevation = visited.stream().map(Tile::getGroundElevationInt).max(Comparator.naturalOrder()).orElse(0);
        visited.forEach(tile -> TemplateUtil.applyBrush(tile,
                TerrainTemplate.builder()
                        .value(TerrainProperty.GROUND_ELEVATION, maxElevation)
                        .build()
        ));
    }
}
