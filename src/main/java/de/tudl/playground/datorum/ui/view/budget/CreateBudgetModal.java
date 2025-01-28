package de.tudl.playground.datorum.ui.view.budget;

import de.tudl.playground.datorum.ui.controller.BudgetController;
import de.tudl.playground.datorum.ui.view.ModalView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CreateBudgetModal implements ModalView {

    private final BudgetController controller;

    public CreateBudgetModal(BudgetController controller) {
        this.controller = controller;
    }

    @Override
    public Scene createScene() {
        // Title
        Text title = createTitle();

        // Form
        GridPane formLayout = createFormLayout();

        // Root Layout
        VBox root = new VBox(15, title, formLayout);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("modal-root");

        // Create Scene and Add Stylesheet
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/CreateBudgetModal.css")).toExternalForm());

        return scene;
    }

    @Override
    public String getTitle() {
        return "Budget Creation";
    }

    private Text createTitle() {
        Text title = new Text("Create Budget");
        title.getStyleClass().add("modal-title");
        return title;
    }

    private GridPane createFormLayout() {
        // Name Field
        Label nameLabel = new Label("Budget Name:");
        TextField nameField = createTextField("Enter budget name", "Enter the name of your budget.");

        // Description Field
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionField = createTextArea();

        // Amount Field
        Label amountLabel = new Label("Amount:");
        TextField amountField = createTextField("Enter amount", "Enter the budget amount as a number (e.g., 100.50).");

        // Buttons
        HBox buttonBox = createButtonBox(nameField, descriptionField, amountField);

        // Form Layout
        GridPane formLayout = new GridPane();
        formLayout.setHgap(10);
        formLayout.setVgap(10);
        formLayout.setPadding(new Insets(15));
        formLayout.getColumnConstraints().addAll(createLabelColumnConstraints(), createFieldColumnConstraints());

        // Add form fields
        formLayout.add(nameLabel, 0, 0);
        formLayout.add(nameField, 1, 0);
        formLayout.add(descriptionLabel, 0, 1);
        formLayout.add(descriptionField, 1, 1);
        formLayout.add(amountLabel, 0, 2);
        formLayout.add(amountField, 1, 2);

        // Add buttons aligned with labels
        formLayout.add(buttonBox, 1, 3);

        return formLayout;
    }

    private TextField createTextField(String promptText, String tooltipText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setTooltip(new Tooltip(tooltipText));
        return textField;
    }

    private TextArea createTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter a short description");
        textArea.setTooltip(new Tooltip("Enter a brief description of the budget."));
        textArea.setPrefRowCount(3);
        return textArea;
    }

    private HBox createButtonBox(TextField nameField, TextArea descriptionField, TextField amountField) {
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("primary-button");
        saveButton.setTooltip(new Tooltip("Click to save the budget."));
        saveButton.setOnAction(e -> handleSave(nameField, descriptionField, amountField));

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("secondary-button");
        cancelButton.setTooltip(new Tooltip("Click to cancel and close the modal."));
        cancelButton.setOnAction(event -> ((Stage) cancelButton.getScene().getWindow()).close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        return buttonBox;
    }

    private void handleSave(TextField nameField, TextArea descriptionField, TextField amountField) {
        if (controller.handleCreateBudget(
                nameField.getText(),
                descriptionField.getText(),
                amountField.getText())) {
            ((Stage) nameField.getScene().getWindow()).close();
        }

        
    }

    private ColumnConstraints createLabelColumnConstraints() {
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER); // Labels take as much space as needed
        col1.setMinWidth(Region.USE_COMPUTED_SIZE);
        return col1;
    }

    private ColumnConstraints createFieldColumnConstraints() {
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS); // Fields grow to fill the remaining space
        col2.setMinWidth(150);
        return col2;
    }
}