package de.tudl.playground.datorum.ui.view;

import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.*;
import java.util.function.Supplier;

/**
 * The {@code AuthorizeView} class provides a dynamic UI component for displaying content
 * based on user roles. It allows mapping roles to specific content, displaying all relevant
 * content for the current user's roles, and a fallback for unauthorized users.
 *
 * <p><strong>Usage:</strong>
 * <ul>
 *   <li>Call {@link #setAuthorizedContent(String, Supplier)} to set role-based content.</li>
 *   <li>Call {@link #setNotAuthorizedContent(Supplier)} to set fallback content visible when
 *       the user has no matching roles or a valid token.</li>
 * </ul>
 *
 * <p><strong>Behavior:</strong>
 * <ul>
 *   <li>Displays all content corresponding to the user's roles.</li>
 *   <li>Ensures fallback content is always set and displayed if no roles match or no token exists.</li>
 * </ul>
 */
public class AuthorizeView extends StackPane {

    private final AuthTokenProvider tokenProvider;
    private final Map<Set<String>, Supplier<Node>> roleContentMap = new HashMap<>();
    private Supplier<Node> notAuthorizedContent;

    /**
     * Constructs an {@code AuthorizeView} with the given token provider.
     *
     * @param tokenProvider the provider for user authentication tokens
     * @throws IllegalArgumentException if the token provider is null
     */
    public AuthorizeView(AuthTokenProvider tokenProvider) {
        if (tokenProvider == null) {
            throw new IllegalArgumentException("AuthTokenProvider cannot be null");
        }
        this.tokenProvider = tokenProvider;
    }

    /**
     * Sets the content to display for specific roles.
     *
     * @param roles a comma-separated list of roles (e.g., "ADMIN, USER")
     * @param contentSupplier the supplier providing the content for the roles
     * @throws IllegalArgumentException if roles or contentSupplier is null
     */
    public void setAuthorizedContent(String roles, Supplier<Node> contentSupplier) {
        if (roles == null || contentSupplier == null) {
            throw new IllegalArgumentException("roles and contentSupplier cannot be null");
        }
        Set<String> roleSet = new HashSet<>(Arrays.asList(roles.split(", *")));
        roleContentMap.put(roleSet, contentSupplier);
        updateView();
    }

    /**
     * Sets the fallback content to display when the user is unauthorized.
     * This method must be called before the application runs.
     *
     * @param contentSupplier the supplier providing the fallback content
     * @throws IllegalArgumentException if contentSupplier is null
     */
    public void setNotAuthorizedContent(Supplier<Node> contentSupplier) {
        if (contentSupplier == null) {
            throw new IllegalArgumentException("NotAuthorizedContent cannot be null");
        }
        this.notAuthorizedContent = contentSupplier;
        updateView();
    }

    /**
     * Updates the view by displaying content based on the user's roles or fallback content.
     */
    private void updateView() {
        this.getChildren().clear();

        if (tokenProvider.getToken() != null) {
            Set<String> userRoles = new HashSet<>(tokenProvider.getToken().roles());

            // Collect all matching nodes for the user roles
            List<Node> authorizedNodes = roleContentMap.entrySet().stream()
                    .filter(entry -> !Collections.disjoint(userRoles, entry.getKey()))
                    .map(Map.Entry::getValue)
                    .map(Supplier::get)
                    .toList();

            // Add all authorized nodes to the StackPane
            if (!authorizedNodes.isEmpty()) {
                this.getChildren().addAll(authorizedNodes);
                return;
            }
        }

        // If no role matches or no token, use the "not authorized" content
        if (notAuthorizedContent != null) {
            this.getChildren().add(notAuthorizedContent.get());
        }
    }
}