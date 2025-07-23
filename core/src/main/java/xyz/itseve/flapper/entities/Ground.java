package xyz.itseve.flapper.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import xyz.itseve.flapper.Flapper;

public class Ground extends Entity {
    private Texture sprite;

    private final float SPEED = 200f;

    private final Flapper flapper;
    private final boolean second;
    public Ground(Flapper flapper, Boolean second) {
        this.flapper = flapper;
        this.second = second;
    }

    @Override public void show() {
        sprite = new Texture(Gdx.files.internal("sprites/base.png"));
        if (second) {
            position.x = sprite.getWidth();
        }
    }

    @Override public void update(float delta) {
        position.x -= SPEED * delta;
        if (position.x <= -sprite.getWidth()) {
            position.x += sprite.getWidth() * 2;
        }
    }

    @Override public void render(float delta) {
        flapper.batch().draw(sprite, position.x, position.y);
    }
}
