package org.openrsc.editor.event;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.SelectPath;

@Builder
@Getter
public class SelectPathUpdateEvent {
    private SelectPath selectPath;
    private boolean isPresent;
}
