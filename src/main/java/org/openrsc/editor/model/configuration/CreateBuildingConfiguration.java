package org.openrsc.editor.model.configuration;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.template.TerrainTemplate;

@Getter
@Builder(toBuilder = true)
public class CreateBuildingConfiguration {
    private final boolean levelFloor;
    private final TerrainTemplate roof;
    private final TerrainTemplate floor;
    private final TerrainTemplate diagonalWall;
    private final TerrainTemplate reverseDiagonalWall;
    private final TerrainTemplate northWall;
    private final TerrainTemplate eastWall;
}
