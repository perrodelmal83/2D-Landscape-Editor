package org.openrsc.editor.model.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Arrays;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
public class DisplayConfiguration {
    public static final DisplayConfiguration DEFAULT_DISPLAY_CONFIGURATION;

    static {
        DisplayConfiguration.DisplayConfigurationBuilder builder = DisplayConfiguration.builder();
        Arrays.stream(DisplayConfigurationProperty.values()).forEach(
                property -> builder.property(property, property.isDefaultValue())
        );
        DEFAULT_DISPLAY_CONFIGURATION = builder.build();
    }

    @Singular
    Map<DisplayConfigurationProperty, Boolean> properties;
}
