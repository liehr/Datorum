package de.tudl.playground.datorum.ui.view.tile;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import lombok.Getter;

/**
 * A generic TileComponent that dynamically renders different data types.
 */
public class TileComponent<T> extends VBox {

    private final VBox contentContainer;
    private final TileViewRegistry tileViewRegistry;

    @Getter
    private T data;

    /**
     * Constructs a TileComponent with a title and a registry for visualization lookup.
     *
     * @param title The title of the tile.
     * @param registry The TileViewRegistry used to find renderers for data types.
     */
    public TileComponent(String title, TileViewRegistry registry) {
        this.tileViewRegistry = registry;

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("tile-title");

        contentContainer = new VBox();
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setVisible(false);
        contentContainer.setManaged(false);

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(titleLabel, contentContainer);
        this.getStyleClass().add("tile-container");
    }

    /**
     * Sets the main data of the tile and updates its visualization.
     */
    public void setData(T data) {
        this.data = data;
        updateVisualization();
    }

    /**
     * Updates the visualization based on the registered renderer.
     */
    private void updateVisualization() {
        contentContainer.getChildren().clear();

        if (data == null) {
            contentContainer.setVisible(false);
            contentContainer.setManaged(false);
            return;
        }

        TileRenderer<T> renderer = tileViewRegistry.getRenderer((Class<T>) data.getClass());
        if (renderer != null) {
            Node content = renderer.render(data);
            contentContainer.getChildren().add(content);
            contentContainer.setVisible(true);
            contentContainer.setManaged(true);
        }
    }
}
