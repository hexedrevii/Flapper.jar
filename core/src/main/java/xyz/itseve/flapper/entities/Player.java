package xyz.itseve.flapper.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import xyz.itseve.flapper.Flapper;
import xyz.itseve.flapper.components.Collider;
import xyz.itseve.flapper.util.MathF;

public class Player extends Entity {
    //#region Fields
    private TextureRegion sprite;
    private final List<Texture> sprites = new ArrayList<>();
    private float switchTime = 0.15f;
    private int spriteIdx = 0;
    private boolean reverse = false;
    private float time = 0;

    private final static float GRAVITY = 450.5f;
    private final static float TERMINAL_VELOCITY = 750.5f;
    private final static float JUMP_VELOCITY = 210.0f;

    private final static float MAX_DOWNWARD_ANGLE = -40;
    private final static float MAX_UPWARDS_ANGLE = 20;

    private final static float FLAPPING_SPEED = 0.05f;
    private final static float FALLING_SPEED = 0.15f;

    private float rotation = 0;
    private final static float ROTATION_SPEED = 400;
    private float rotationAngle;

    private PlayerState state = PlayerState.ALIVE;
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

    private void cycleAnimation(float delta) {
        time += delta;
        if (time >= switchTime) {
            spriteIdx += reverse ? -1 : 1;
            if (spriteIdx >= sprites.size() - 1) reverse = true;
            else if (spriteIdx <= 0) reverse = false;

            sprite.setTexture(sprites.get(spriteIdx));
            time = 0;
        }
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

        pushComponent(Collider.class, position, new Vector2(
            sprite.getTexture().getWidth(),
            sprite.getTexture().getHeight()
        ));

        Optional<Collider> collider = get(Collider.class);
        collider.ifPresent(value -> value.onCollide = (e) -> {
            // Ground
            if (e.id() == 1) {
                state = PlayerState.DEAD;
                return;
            }

            if (state != PlayerState.DEAD) {
                state = PlayerState.FALLING;
                rotationAngle = MAX_DOWNWARD_ANGLE;
            }
        });
    }

    @Override public void update(float delta) {
        rotation = MathF.moveTowards(rotation, rotationAngle, ROTATION_SPEED * delta);

        switch (state) {
            case ALIVE:
                cycleAnimation(delta);

                //#region Movement and Gravity
                velocity.y = MathF.moveTowards(velocity.y, -TERMINAL_VELOCITY, GRAVITY * delta);
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

                break;

            case DEAD:
                break;

            case FALLING:
                cycleAnimation(delta);

                velocity.y = MathF.moveTowards(velocity.y, -TERMINAL_VELOCITY, GRAVITY * delta);
                position.x += velocity.x * delta;
                position.y += velocity.y * delta;

                break;
        }
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
