package org.openrsc.editor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.definition.LabeledDefinition;

@AllArgsConstructor
@Getter
public enum GeneratorAlgorithm implements LabeledDefinition {
    DIAMOND_SQUARE("Diamond Square"),
    PERLIN_NOISE("Perlin Noise");

    private final String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
