package org.openrsc.editor.model.configuration;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.definition.WallDefinition;

@Builder(toBuilder = true)
@Getter
public class StrokePathConfiguration {
    private final WallDefinition wallDefinition;
}
