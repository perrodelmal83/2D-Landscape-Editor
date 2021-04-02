package org.openrsc.editor.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.openrsc.editor.model.DisplayConfigurationProperty;

import java.util.Map;

@Builder
@Getter
public class DisplayConfigurationUpdateEvent {
    @Singular
    private final Map<DisplayConfigurationProperty, Boolean> updatedProperties;
}
