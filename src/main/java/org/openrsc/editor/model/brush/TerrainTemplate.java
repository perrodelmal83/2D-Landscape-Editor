package org.openrsc.editor.model.brush;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.openrsc.editor.model.Template;

import java.util.Map;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
public class TerrainTemplate implements Template {
    @Singular
    private final Map<TerrainProperty, Integer> values;
}
