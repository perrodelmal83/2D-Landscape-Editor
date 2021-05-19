package org.openrsc.editor.event.action;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.SelectRegion;
import org.openrsc.editor.model.configuration.GenerateLandscapeConfiguration;

@Builder(toBuilder = true)
@Getter
public class GenerateLandscapeAction {
    private final SelectRegion selectRegion;
    private final GenerateLandscapeConfiguration configuration;
}
