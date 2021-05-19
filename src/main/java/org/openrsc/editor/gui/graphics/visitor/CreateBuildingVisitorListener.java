package org.openrsc.editor.gui.graphics.visitor;

import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.configuration.CreateBuildingConfiguration;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.util.Comparator;
import java.util.List;

public class CreateBuildingVisitorListener extends PathVisitorListener {
    private final CreateBuildingConfiguration configuration;

    public CreateBuildingVisitorListener(CreateBuildingConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void onNorthWallVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, configuration.getNorthWall());
    }

    @Override
    protected void onEastWallVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, configuration.getEastWall());
    }

    @Override
    protected void onForwardDiagonalVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, configuration.getDiagonalWall());
    }

    @Override
    protected void onBackwardDiagonalVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, configuration.getReverseDiagonalWall());
    }

    @Override
    protected void onFillTileVisited(Tile tile) {
        TemplateUtil.applyBrush(tile, configuration.getFloor());
        TemplateUtil.applyBrush(tile, configuration.getRoof());
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
