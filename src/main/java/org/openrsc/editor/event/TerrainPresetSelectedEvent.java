package org.openrsc.editor.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.brush.TerrainTemplate;

@AllArgsConstructor
@Getter
public class TerrainPresetSelectedEvent {
    TerrainTemplate template;
}
