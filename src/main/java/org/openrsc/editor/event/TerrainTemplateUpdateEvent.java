package org.openrsc.editor.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.TerrainTemplate;

@AllArgsConstructor
@Getter
public class TerrainTemplateUpdateEvent {
    private final TerrainTemplate template;
}
