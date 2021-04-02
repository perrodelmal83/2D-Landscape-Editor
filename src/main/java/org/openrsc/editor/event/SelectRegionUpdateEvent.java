package org.openrsc.editor.event;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.SelectRegion;

@Builder
@Getter
public class SelectRegionUpdateEvent {
    private final SelectRegion selectRegion;
    private final boolean selectionPresent;
}
