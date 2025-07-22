package xyz.itseve.flapper.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityPool {
    private final List<Entity> entities = new ArrayList<>();

    public <T extends Entity> void push(Class<T> t, Object... params) {
        try {
            Class<?>[] types = new Class<?>[params.length];
            for (int i = 0; i < types.length; i++) types[i] = params[i].getClass();
            Constructor<T> ctor = t.getDeclaredConstructor(types);
            T entity = ctor.newInstance(params);
            entity.show();

            entities.add(entity);
        } catch (
            InvocationTargetException | NoSuchMethodException |
            InstantiationException | IllegalAccessException e
        ) {
            throw new RuntimeException(e);
        }
    }

    public void update(float delta) {
        for (Entity e : entities) e.update(delta);
    }

    public void render(float delta) {
        for (Entity e : entities) e.render(delta);
    }
}
