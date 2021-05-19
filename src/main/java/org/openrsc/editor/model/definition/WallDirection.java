package org.openrsc.editor.model.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.gui.graphics.LineLocation;

@AllArgsConstructor
@Getter
public enum WallDirection {
    NORTH(LineLocation.BORDER_TOP),
    EAST(LineLocation.BORDER_RIGHT),
    DIAGONAL_FORWARD(LineLocation.DIAGONAL_FROM_TOP_RIGHT),
    DIAGONAL_BACKWARD(LineLocation.DIAGONAL_FROM_TOP_LEFT);

    private final LineLocation lineLocation;
}
