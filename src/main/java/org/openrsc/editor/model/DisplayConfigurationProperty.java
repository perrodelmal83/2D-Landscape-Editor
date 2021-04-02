package org.openrsc.editor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisplayConfigurationProperty {
    SHOW_ROOFS("Show Roofs", true),
    SHOW_NPCS("Show NPCs", true),
    SHOW_OBJECTS("Show Objects", true),
    SHOW_ITEMS("Show Items", true);

    private final String label;
    private final boolean defaultValue;
}
