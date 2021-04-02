package org.openrsc.editor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SectorHeight {
    GROUND_FLOOR(0),
    UPSTAIRS(1),
    SECOND_LEVEL(2),
    UNDERGROUND(3);

    private final int height;
}
