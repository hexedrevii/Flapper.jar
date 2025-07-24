package xyz.itseve.flapper.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    public abstract void show();

    public abstract void update(float delta);
    public abstract void render(float delta);

    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
}
