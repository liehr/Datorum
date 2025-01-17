package de.tudl.playground.datorum.ui.view.nav;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import lombok.Getter;

import java.util.List;

@Getter
public class BottomNavigationBar extends HBox {

    public BottomNavigationBar(List<Button> navButtons) {
        // Set spacing and alignment for the HBox
        super(10); // Spacing between buttons
        setAlignment(Pos.BOTTOM_CENTER);
        setPrefHeight(70); // Height of the navigation bar
        getStyleClass().add("navigation-bar");

        // Configures passed buttons
        navButtons.forEach(this::configureNavButton);

        // Add buttons to the HBox
        getChildren().addAll(navButtons);
    }

    private void configureNavButton(Button button)
    {
        button.setPrefSize(50, 50); // Button size
        button.setShape(new Circle(25)); // Circular shape
        button.getStyleClass().add("nav-button");
        button.setFocusTraversable(false); // Remove focus outline
        if (button.getTooltip() == null) {
            button.setTooltip(new Tooltip(button.getText())); // Set tooltip if not provided
        }
    }
}
