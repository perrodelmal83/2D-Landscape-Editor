package org.openrsc.editor.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.awt.Point;
import java.util.List;

@Builder
@Getter
public class SelectPath {
    @Singular
    private final List<Point> points;
}
