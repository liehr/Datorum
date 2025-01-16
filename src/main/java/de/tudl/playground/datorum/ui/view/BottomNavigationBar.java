package de.tudl.playground.datorum.ui.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import lombok.Getter;

@Getter
public class BottomNavigationBar extends Region {

    // Expose the HBox for advanced customizations
    private final HBox navigationBar;

    public BottomNavigationBar() {
        // Navigation Buttons
        Button homeButton = createNavButton("🏠", "Home");
        Button textButton = createNavButton("T", "Text");
        Button imageButton = createNavButton("🖼️", "Image");
        Button clipboardButton = createNavButton("📋", "Clipboard");
        Button gridButton = createNavButton("🗂️", "Grid");

        // Navigation Container
        navigationBar = new HBox(10, homeButton, textButton, imageButton, clipboardButton, gridButton);
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setPrefHeight(70); // Height of the navigation bar
        navigationBar.getStyleClass().add("navigation-bar");

        // Add to component
        getChildren().add(navigationBar);
    }

    // Helper method to create navigation buttons
    private Button createNavButton(String icon, String tooltipText) {
        Button button = new Button(icon);
        button.setPrefSize(50, 50); // Button size
        button.setShape(new Circle(25)); // Circular shape
        button.getStyleClass().add("nav-button");
        button.setFocusTraversable(false); // Remove focus outline
        return button;
    }

}