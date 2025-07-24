package xyz.itseve.flapper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import xyz.itseve.flapper.Flapper;
import xyz.itseve.flapper.entities.EntityPool;
import xyz.itseve.flapper.entities.Ground;
import xyz.itseve.flapper.entities.Player;
import xyz.itseve.flapper.entities.Pipe;
import xyz.itseve.flapper.util.MathF;
import xyz.itseve.flapper.util.ScaledRenderer;

public class Playing implements Screen {
    //#region Fields
    private final Flapper flapper;
    private final ScaledRenderer renderer;

    private final EntityPool entities = new EntityPool();
    private final EntityPool pipes = new EntityPool();

    private float pipeSpawnTime = 0;
    private final float PIPE_SPAWN_TIMEOUT = 1.5f;

    private Texture background;
    //#endregion

    public Playing(Flapper flapper) {
        this.flapper = flapper;
        renderer = new ScaledRenderer(flapper.GAME_SIZE);
    }

    @Override public void show() {
        background = new Texture(Gdx.files.internal("sprites/background-day.png"));

        // Two ground :3
        entities.push(Ground.class, flapper, false);
        entities.push(Ground.class, flapper, true);

        entities.push(Player.class, flapper);

        pipes.setOnEntityUpdate(e -> {
            Pipe self = (Pipe)e;
            if (self.position.x <= -self.getTextureWidth()) {
                pipes.queueRemove(e);
            }
        });
    }

    @Override public void render(float delta) {
        entities.update(delta);
        handlePipes(delta);

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

    private void handlePipes(float delta) {
        pipeSpawnTime += delta;
        if (pipeSpawnTime >= PIPE_SPAWN_TIMEOUT) {
            Pipe firstPipe = new Pipe(flapper, false);
            Pipe secondPipe = new Pipe(flapper, true);

            float pipeY = MathF.nextFloat(new Random(), -120, 110);
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
}
