package xyz.itseve.flapper.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    abstract void update(float delta);
    abstract void render(float delta);

    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();
}
