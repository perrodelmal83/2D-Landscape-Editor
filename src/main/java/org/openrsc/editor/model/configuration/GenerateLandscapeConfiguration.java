package org.openrsc.editor.model.configuration;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.GeneratorAlgorithm;

@Builder(toBuilder = true)
@Getter
public class GenerateLandscapeConfiguration {
    private final long seed;
    private final int width;
    private final int height;
    private final GeneratorAlgorithm algorithm;
}
