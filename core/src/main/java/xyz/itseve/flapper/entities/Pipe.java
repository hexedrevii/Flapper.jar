package xyz.itseve.flapper.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import xyz.itseve.flapper.Flapper;
import xyz.itseve.flapper.components.Collider;

public class Pipe extends Entity {
    private Texture texture;
    private Sprite sprite;

    private final boolean flipped;
    private final static float SPEED = 170;

    private final Flapper flapper;
    public Pipe(Flapper flapper, Boolean flipped) {
        this.flapper = flapper; this.flipped = flipped;
    }

    public int getTextureWidth() {
        return texture.getWidth();
    }
    public int getTextureHeight() {
        return texture.getHeight();
    }


    public void setPositionY(float y) {
        position.y = y;
        sprite.setPosition(position.x, position.y);
    }

    @Override public void show() {
        texture = new Texture(Gdx.files.internal("sprites/pipe-green.png"));

        sprite = new Sprite(texture);
        sprite.setFlip(false, flipped);

        position.x = flapper.GAME_SIZE.x + texture.getWidth();
        sprite.setPosition(position.x, position.y);

        pushComponent(Collider.class, position, new Vector2(
            texture.getWidth(), texture.getHeight()
        ));
    }

    @Override public void update(float delta) {
        position.x -= SPEED * delta;
        sprite.setPosition(position.x, position.y);
    }

    @Override public void render(float delta) {
        sprite.draw(flapper.batch());
    }
}
