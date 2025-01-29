package de.tudl.playground.datorum.ui.view.dashboard;

import de.tudl.playground.datorum.ui.controller.DashboardController;
import de.tudl.playground.datorum.ui.view.tile.TileComponent;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class DashboardView {

    private final GridPane root = new GridPane();
    private static final int TILE_SIZE = 300;
    private static final int TILE_PADDING = 20;

    private final DashboardController controller;
    private Scene scene;

    public DashboardView(DashboardController controller) {
        this.controller = controller;
        root.setPadding(new Insets(20));
        root.setHgap(TILE_PADDING);
        root.setVgap(TILE_PADDING);
    }

    /**
     * Call this method **after** JavaFX has started to properly initialize the UI.
     */
    public void initialize() {
        Platform.runLater(this::updateGrid);

        // Listen for tile changes
        controller.tilesProperty().addListener((ListChangeListener<TileComponent<?>>) change -> updateGrid());
    }

    public Node createNode() {
        return root;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        scene.widthProperty().addListener((ChangeListener<Number>) (obs, oldWidth, newWidth) -> updateGrid());
        scene.heightProperty().addListener((ChangeListener<Number>) (obs, oldHeight, newHeight) -> updateGrid());
    }

    private void updateGrid() {
        Platform.runLater(() -> {
            root.getChildren().clear();
            root.getColumnConstraints().clear();
            root.getRowConstraints().clear();

            int columns = getColumnCount();
            for (int i = 0; i < columns; i++) {
                root.getColumnConstraints().add(new ColumnConstraints((double) TILE_SIZE + TILE_PADDING));
            }

            var tiles = controller.tilesProperty();
            for (int i = 0; i < tiles.size(); i++) {
                int row = i / columns;
                int col = i % columns;
                root.add(tiles.get(i), col, row);
            }
        });
    }

    private int getColumnCount() {
        double screenWidth = scene != null ? scene.getWidth() : Screen.getPrimary().getBounds().getWidth();
        return Math.max(1, (int) ((screenWidth - TILE_PADDING * 2) / (TILE_SIZE + TILE_PADDING)));
    }
}
