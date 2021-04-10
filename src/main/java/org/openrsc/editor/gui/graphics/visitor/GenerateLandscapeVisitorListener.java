package org.openrsc.editor.gui.graphics.visitor;

import org.openrsc.editor.TemplateUtil;
import org.openrsc.editor.Util;
import org.openrsc.editor.generator.DiamondSquare;
import org.openrsc.editor.generator.PerlinNoise;
import org.openrsc.editor.generator.TerrainGenerator;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.configuration.GenerateLandscapeConfiguration;
import org.openrsc.editor.model.template.TerrainProperty;
import org.openrsc.editor.model.template.TerrainTemplate;

import java.util.List;
import java.util.Optional;

public class GenerateLandscapeVisitorListener extends PathVisitorListener {

    private TerrainGenerator terrainGenerator;

    public GenerateLandscapeVisitorListener(GenerateLandscapeConfiguration configuration) {
        switch (configuration.getAlgorithm()) {
            case DIAMOND_SQUARE:
                terrainGenerator = new DiamondSquare(configuration.getSeed());
                break;
            case PERLIN_NOISE:
                terrainGenerator = new PerlinNoise((int) configuration.getSeed());
                break;
        }
    }

    @Override
    protected void onNorthWallVisited(Tile tile) {

    }

    @Override
    protected void onEastWallVisited(Tile tile) {

    }

    @Override
    protected void onForwardDiagonalVisited(Tile tile) {

    }

    @Override
    protected void onBackwardDiagonalVisited(Tile tile) {

    }

    @Override
    protected void onFillTileVisited(Tile tile) {
        Optional.ofNullable(terrainGenerator).ifPresent(generator -> {
            float data = generator.getData(tile.getGridX(), tile.getGridY());
            int derivedElevation = (byte) (int) (data * 254);
            int derivedTexture = (byte) ((int) (data * -170) + 200);

            TemplateUtil.applyBrush(
                    tile,
                    TerrainTemplate.builder()
                            .value(TerrainProperty.GROUND_ELEVATION, derivedElevation)
                            .value(TerrainProperty.GROUND_TEXTURE, derivedTexture)
                            .build()
            );
            Util.sectorModified = true;
        });
    }

    @Override
    protected void onComplete(List<Tile> visited) {

    }
}
