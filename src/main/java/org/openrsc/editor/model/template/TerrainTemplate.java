package org.openrsc.editor.model.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.With;

import java.util.Map;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@With
public class TerrainTemplate implements Template {
    @Singular
    private final Map<TerrainProperty, Integer> values;
}
