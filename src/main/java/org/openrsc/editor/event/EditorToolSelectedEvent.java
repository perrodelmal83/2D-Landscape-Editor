package org.openrsc.editor.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.EditorTool;

@AllArgsConstructor
@Getter
public class EditorToolSelectedEvent {
    private EditorTool editorTool;
}
