package org.openrsc.editor.event.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.Point;
import java.util.List;

@AllArgsConstructor
@Getter
public class ConvertPathToSelectionAction {
    private final List<Point> points;
}
