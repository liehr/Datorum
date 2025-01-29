package de.tudl.playground.datorum.ui.view.tile;

import javafx.scene.Node;

/**
 * Interface for defining how different data types should be visualized in a tile.
 */
public interface TileRenderer<T> {
    Node render(T data);
}

