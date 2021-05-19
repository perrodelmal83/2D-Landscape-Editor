package org.openrsc.editor.model.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.awt.Point;

@Getter
@Jacksonized
@Builder(toBuilder = true)
public class GameObjectLoc {
	private final int direction;
	private final int id;
	private final int type;
	private final Point location;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@Builder.Default
	private final String owner = null;
}
