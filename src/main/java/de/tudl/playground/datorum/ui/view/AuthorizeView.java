package de.tudl.playground.datorum.ui.view;

import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class AuthorizeView extends StackPane {

    private final AuthTokenProvider tokenProvider;
    private List<String> roles;
    private Supplier<Node> authorizedContent;
    private Supplier<Node> notAuthorizedContent;

    public AuthorizeView(AuthTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public void setRoles(String... roles) {
        this.roles = Arrays.asList(roles);
        updateView();
    }

    public void setAuthorizedContent(Supplier<Node> contentSupplier) {
        this.authorizedContent = contentSupplier;
        updateView();
    }

    public void setNotAuthorizedContent(Supplier<Node> contentSupplier) {
        this.notAuthorizedContent = contentSupplier;
        updateView();
    }

    private void updateView() {
        this.getChildren().clear();

        boolean isAuthorized = tokenProvider != null
                && tokenProvider.getToken().Roles().stream().anyMatch(roles::contains);

        if (isAuthorized && authorizedContent != null) {
            this.getChildren().add(authorizedContent.get());
        } else if (!isAuthorized && notAuthorizedContent != null) {
            this.getChildren().add(notAuthorizedContent.get());
        }
    }
}

