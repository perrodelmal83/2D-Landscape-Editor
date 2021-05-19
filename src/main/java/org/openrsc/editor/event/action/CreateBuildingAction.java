package org.openrsc.editor.event.action;

import lombok.Builder;
import lombok.Getter;
import org.openrsc.editor.model.SelectRegion;
import org.openrsc.editor.model.configuration.CreateBuildingConfiguration;

@Getter
@Builder(toBuilder = true)
public class CreateBuildingAction {
    private final SelectRegion selectRegion;
    private final CreateBuildingConfiguration configuration;
}
