package de.tudl.playground.datorum.ui.view.tile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define a TileView renderer for a specific type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TileView {
    Class<?> value(); // The type this TileView handles
}

