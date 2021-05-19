package org.openrsc.editor.model.data;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.awt.Point;

@Jacksonized
@Builder(toBuilder = true)
@Getter
public class NpcLoc {
	private final int id;
	private final Point max;
	private final Point min;
	private final Point start;
}
