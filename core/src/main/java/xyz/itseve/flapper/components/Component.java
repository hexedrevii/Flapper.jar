package xyz.itseve.flapper.components;

public abstract class Component {
    protected int id;
    public final int id() {
        return id;
    }

    public abstract void show();

    public abstract void update(float delta);
    public abstract void render(float delta);
}
