package org.openrsc.editor.event.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrsc.editor.model.SelectPath;
import org.openrsc.editor.model.configuration.StrokePathConfiguration;

@AllArgsConstructor
@Getter
public class StrokePathAction {
    private final SelectPath selectPath;
    private final StrokePathConfiguration configuration;
}
