package de.tudl.playground.datorum.ui.view.tile;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry that manages tile visualizations for different data types.
 */
@Component
public class TileViewRegistry {

    private final Map<Class<?>, TileRenderer<?>> renderers = new HashMap<>();

    public TileViewRegistry(ApplicationContext context) {
        Map<String, Object> beans = context.getBeansWithAnnotation(TileView.class);
        beans.values().forEach(bean -> {
            Class<?> type = bean.getClass().getAnnotation(TileView.class).value();
            if (bean instanceof TileRenderer<?>) {
                renderers.put(type, (TileRenderer<?>) bean);
            }
        });
    }

    /**
     * Retrieves the renderer for a given type.
     */
    public <T> TileRenderer<T> getRenderer(Class<T> type) {
        return (TileRenderer<T>) renderers.get(type);
    }
}

