package de.tudl.playground.datorum.ui.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class MainView implements ApplicationView{
    @Override
    public Scene createScene() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPrefWidth(1280);
        root.setPrefHeight(720);

        return new Scene(root);
    }
}
