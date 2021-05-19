package org.openrsc.editor.gui.graphics.visitor;

import org.openrsc.editor.model.Tile;

import java.util.List;

public abstract class PathVisitorListener {
    protected abstract void onNorthWallVisited(Tile tile);

    protected abstract void onEastWallVisited(Tile tile);

    protected abstract void onForwardDiagonalVisited(Tile tile);

    protected abstract void onBackwardDiagonalVisited(Tile tile);

    protected abstract void onFillTileVisited(Tile tile);

    protected abstract void onComplete(List<Tile> visited);
}
