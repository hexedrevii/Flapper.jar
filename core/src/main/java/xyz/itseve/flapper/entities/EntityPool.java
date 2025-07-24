package xyz.itseve.flapper.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityPool {
    private final List<Entity> entities = new ArrayList<>();

    private Consumer<Entity> onEntityUpdate;
    public void setOnEntityUpdate(Consumer<Entity> action) {
        onEntityUpdate = action;
    }

    private final List<Entity> removeQueue = new ArrayList<>();

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

    public void addAll(Entity... en) {
        for (Entity e : en) {
            e.show();
            entities.add(e);
        }
    }

    public<T extends Entity> void queueRemove(T e) {
        removeQueue.add(e);
    }

    public void update(float delta) {
        for (Entity e : entities) {
            e.update(delta);
            if (onEntityUpdate != null) onEntityUpdate.accept(e);
        }

        if (!removeQueue.isEmpty()) {
            entities.removeAll(removeQueue);
            removeQueue.clear();
        }
    }

    public void render(float delta) {
        for (Entity e : entities) e.render(delta);
    }
}
