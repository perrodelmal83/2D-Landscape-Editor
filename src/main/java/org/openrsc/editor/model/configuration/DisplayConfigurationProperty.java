package org.openrsc.editor.model.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisplayConfigurationProperty {
    SHOW_ROOFS("Show Roofs", true),
    SHOW_NPCS("Show NPCs", true),
    SHOW_OBJECTS("Show Objects", true),
    SHOW_ITEMS("Show Items", true),
    SHOW_HEATMAP("Show Heatmap", false);

    private final String label;
    private final boolean defaultValue;
}
