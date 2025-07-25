package xyz.itseve.flapper.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import xyz.itseve.flapper.components.Collider;

public class Bound extends Entity {
    public Bound(Integer id) {
        this.id = id;
    }

    @Override public void show() {
        Texture holder = new Texture(Gdx.files.internal("sprites/base.png"));
        pushComponent(Collider.class, position, new Vector2(holder.getWidth(), holder.getHeight()));
    }

    @Override public void update(float delta) {}

    @Override public void render(float delta) {}
}
