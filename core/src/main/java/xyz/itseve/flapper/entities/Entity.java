package xyz.itseve.flapper.entities;

import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import xyz.itseve.flapper.components.Component;

public abstract class Entity {
    protected int id = 0;
    public final int id() {
        return id;
    }

    private final List<Component> components = new ArrayList<>();

    public <T extends Component> void pushComponent(Class<T> t, Object... params) {
        try {
            Class<?>[] types = new Class<?>[params.length];
            for (int i = 0; i < types.length; i++) types[i] = params[i].getClass();
            Constructor<T> ctor = t.getDeclaredConstructor(types);
            T component = ctor.newInstance(params);
            component.show();

            components.add(component);
        } catch (
            InvocationTargetException | NoSuchMethodException |
            InstantiationException | IllegalAccessException e
        ) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Component> Optional<T> get(Class<T> t) {
        return components.stream()
            .filter(t::isInstance)
            .map(t::cast)
            .findFirst();
    }

    public <T extends Component> Optional<T> get(Class<T> t,  int id) {
        return components.stream()
            .filter(
                component -> component.id() == id && t.isInstance(component)
            ).map(t::cast)
            .findFirst();
    }

    public abstract void show();

    public abstract void update(float delta);
    public abstract void render(float delta);

    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();

    public void process(float delta) {
        update(delta);

        for (Component component : components) {
            component.update(delta);
        }
    }

    public void draw(float delta) {
        render(delta);

        for (Component component : components) {
            component.render(delta);
        }
    }
}
