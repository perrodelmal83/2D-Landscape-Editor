package org.openrsc.editor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EditorTool {
    SELECT("Select", "/icons/select.png"),
    MOVE("Move", "/icons/move.png"),
    CLONE("Clone", "/icons/copy.png"),
    STAMP("Stamp", "/icons/stamp.png"),
    DRAW_PATH("Path", "/icons/draw-path.png"),
    INSPECT("Inspect", "/icons/inspect.png");

    private final String text;
    private final String resource;
}
