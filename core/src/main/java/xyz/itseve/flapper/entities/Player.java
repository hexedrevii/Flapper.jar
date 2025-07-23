package xyz.itseve.flapper.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

import xyz.itseve.flapper.Flapper;
import xyz.itseve.flapper.util.MathF;

@SuppressWarnings("FieldCanBeLocal")
public class Player extends Entity {
    //#region Fields
    private TextureRegion sprite;
    private final List<Texture> sprites = new ArrayList<>();
    private float switchTime = 0.15f;
    private int spriteIdx = 0;
    private boolean reverse = false;
    private float time = 0;

    private final float GRAVITY = 450.5f;
    private final float TERMINAL_VELOCITY = 700.5f;
    private final float JUMP_VELOCITY = 200.0f;

    private final float MAX_DOWNWARD_ANGLE = -40;
    private final float MAX_UPWARDS_ANGLE = 20;

    private final float FLAPPING_SPEED = 0.05f;
    private final float FALLING_SPEED = 0.15f;

    private float rotation = 0;
    private final float ROTATION_SPEED = 400;
    private float rotationAngle;
    //#endregion

    private final Flapper flapper;
    public Player(Flapper flapper) {
        this.flapper = flapper;
    }

    private void jump() {
        velocity.y = JUMP_VELOCITY;
        rotationAngle = MAX_UPWARDS_ANGLE;
        switchTime = FLAPPING_SPEED;
    }

    @Override public void show() {
        sprites.add(new Texture(Gdx.files.internal("sprites/yellowbird-downflap.png")));
        sprites.add(new Texture(Gdx.files.internal("sprites/yellowbird-midflap.png")));
        sprites.add(new Texture(Gdx.files.internal("sprites/yellowbird-upflap.png")));

        sprite = new TextureRegion(sprites.get(spriteIdx));

        // Move self to the middle of the screen offset on the X axis
        int offset = 10;
        position.x = flapper.GAME_SIZE.x * 0.5f - sprite.getTexture().getWidth() - offset;
        position.y = flapper.GAME_SIZE.y * 0.5f;
    }

    @Override public void update(float delta) {
        // Cycle animation
        time += delta;
        if (time >= switchTime) {
            spriteIdx += reverse ? -1 : 1;
            if (spriteIdx >= sprites.size() - 1) reverse = true;
            else if (spriteIdx <= 0) reverse = false;

            sprite.setTexture(sprites.get(spriteIdx));
            time = 0;
        }

        //#region Movement and Gravity
        velocity.y = MathF.moveTowards(velocity.y, -TERMINAL_VELOCITY, GRAVITY * delta);
        rotation = MathF.moveTowards(rotation, rotationAngle, ROTATION_SPEED * delta);

        // PC jumping
        if (
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
        ) jump();

        // Mobile jumping
        if (Gdx.input.justTouched()) jump();

        // Bonk
        if (position.y >= flapper.GAME_SIZE.y + 10) {
            position.y = flapper.GAME_SIZE.y + 9;
            velocity.y = 0;
        }

        if (velocity.y < -20) {
            rotationAngle = MAX_DOWNWARD_ANGLE;
            switchTime = FALLING_SPEED;
        }

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        //#endregion
    }

    @Override public void render(float delta) {
        flapper.batch().draw(
            sprite,
            position.x, position.y,
            sprite.getTexture().getWidth() * 0.5f, sprite.getTexture().getHeight() * 0.5f,
            sprite.getTexture().getWidth(), sprite.getTexture().getHeight(),
            1, 1, rotation
        );
    }
}
