package xyz.itseve.flapper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import xyz.itseve.flapper.Flapper;
import xyz.itseve.flapper.components.Collider;
import xyz.itseve.flapper.entities.Bound;
import xyz.itseve.flapper.entities.Entity;
import xyz.itseve.flapper.entities.EntityPool;
import xyz.itseve.flapper.entities.Ground;
import xyz.itseve.flapper.entities.Player;
import xyz.itseve.flapper.entities.Pipe;
import xyz.itseve.flapper.observers.Observer;
import xyz.itseve.flapper.observers.ObserverID;
import xyz.itseve.flapper.observers.Subject;
import xyz.itseve.flapper.util.MathF;
import xyz.itseve.flapper.util.ScaledRenderer;

public class Playing implements Screen, Observer, Subject {
    //#region Fields
    private final Flapper flapper;
    private final ScaledRenderer renderer;

    private final EntityPool entities = new EntityPool();
    private final EntityPool pipes = new EntityPool();

    private float pipeSpawnTime = 0;
    private final static float PIPE_SPAWN_TIMEOUT = 1.5f;

    private Texture background;

    private GameState state = GameState.PLAYING;
    private final List<Observer> observers = new ArrayList<>();
    //#endregion

    public Playing(Flapper flapper) {
        this.flapper = flapper;
        renderer = new ScaledRenderer(flapper.GAME_SIZE);
    }

    @Override public void show() {
        background = new Texture(Gdx.files.internal("sprites/background-day.png"));

        // Two ground :3
        Ground first = new Ground(flapper, false);
        pushObserver(first);
        Ground second = new Ground(flapper, true);
        pushObserver(second);

        entities.addAll(first, second);

        Player player = new Player(flapper);
        player.pushObserver(this);
        entities.addAll(player);

        pipes.push(Bound.class, 1);

        pipes.setOnEntityUpdate(e -> {
            try {
                Pipe self = (Pipe) e;
                if (self.position.x <= -self.getTextureWidth()) {
                    pipes.queueRemove(e);
                }
            } catch (ClassCastException ex) {
                // This can be safely ignored
            }
        });
    }

    @Override public void render(float delta) {
        switch (state) {
            case PLAYING:
                entities.update(delta);
                handlePipes(delta);
                handleCollision();

                break;

            case IN_FALL:
                // Still updating the player until it hits the ground.
                entities.update(delta);
                handleCollision();
                break;

            case DEAD:
                // TODO: Panel animations
                break;
        }

        renderer.begin();
        ScreenUtils.clear(Color.SKY);

        flapper.batch().setProjectionMatrix(renderer.camera().combined);
        flapper.batch().begin();
            flapper.batch().draw(background, 0, 0);

            pipes.render(delta);
            entities.render(delta);
        flapper.batch().end();

        renderer.end();

        ScreenUtils.clear(Color.BLACK);
        renderer.render(flapper.batch());
    }

    @Override public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
    }

    private void handleCollision() {
        for (int i = 0; i < entities.entities().size(); i++) {
            Entity entity = entities.entities().get(i);
            Optional<Collider> entityCollider = entity.get(Collider.class);
            if (entityCollider.isEmpty()) continue;

            for (int j = 0; j < pipes.entities().size(); j++) {
                Entity other = pipes.entities().get(j);
                Optional<Collider> otherCollider = other.get(Collider.class);
                if (otherCollider.isEmpty()) continue;

                Collider entityColliderVal = entityCollider.get();
                if (entityColliderVal.overlaps(otherCollider.get())) {
                    if (entityColliderVal.onCollide != null) {
                        entityColliderVal.onCollide.accept(other);
                    }
                }
            }
        }
    }

    private void handlePipes(float delta) {
        pipeSpawnTime += delta;
        if (pipeSpawnTime >= PIPE_SPAWN_TIMEOUT) {
            Pipe firstPipe = new Pipe(flapper, false);
            Pipe secondPipe = new Pipe(flapper, true);

            float pipeY = MathF.nextFloat(new Random(), -170, 80);
            firstPipe.position.y = pipeY;

            int pipeGap = 80;
            pipes.addAll(firstPipe, secondPipe);
            secondPipe.setPositionY(pipeY + secondPipe.getTextureHeight() + pipeGap);

            pipeSpawnTime = 0;
        }

        pipes.update(delta);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void dispose() {
        renderer.dispose();
    }

    @Override public void raise(int id) {
        if (id == ObserverID.GROUND.id()) {
            state = GameState.DEAD;
            return;
        }

        notify(0);
        state = GameState.IN_FALL;
    }

    @Override public void pushObserver(Observer o) {
        observers.add(o);
    }

    @Override public void notify(int id) {
        for (Observer observer : observers) {
            observer.raise(id);
        }
    }
}
