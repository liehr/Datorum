package de.tudl.playground.datorum.ui.view.tile;

import de.tudl.playground.datorum.modulith.budget.command.data.Budget;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

/**
 * Tile view renderer for Budget type.
 */
@TileView(Budget.class)
@Component
public class BudgetTileRenderer implements TileRenderer<Budget> {
    @Override
    public Node render(Budget budget) {
        VBox box = new VBox();
        Label amountLabel = new Label(String.valueOf(budget.getAmount()));
        amountLabel.getStyleClass().add("tile-value");
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(amountLabel);
        return box;
    }
}

