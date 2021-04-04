package org.openrsc.editor.event.action;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.SelectRegion;
import org.openrsc.editor.model.template.TerrainTemplate;

@Getter
@Builder
public class CreateBuildingAction {
    private final SelectRegion selectRegion;
    private final int stories = 1;
    private final boolean withBasement = false;
    private final TerrainTemplate roof;
    private final TerrainTemplate floor;
    private final TerrainTemplate diagonalWall;
    private final TerrainTemplate reverseDiagonalWall;
    private final TerrainTemplate northWall;
    private final TerrainTemplate eastWall;
}
