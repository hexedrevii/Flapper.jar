package xyz.itseve.flapper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import java.lang.reflect.InvocationTargetException;

import xyz.itseve.flapper.Flapper;
import xyz.itseve.flapper.entities.EntityPool;
import xyz.itseve.flapper.entities.Player;
import xyz.itseve.flapper.util.ScaledRenderer;

public class Playing implements Screen {
    private final Flapper flapper;
    private final ScaledRenderer renderer;

    private final EntityPool entities = new EntityPool();

    private Texture background;

    public Playing(Flapper flapper) {
        this.flapper = flapper;
        renderer = new ScaledRenderer(flapper.GAME_SIZE);
    }

    @Override public void show() {

        background = new Texture(Gdx.files.internal("sprites/background-day.png"));

        entities.push(Player.class, flapper);
    }

    @Override public void render(float delta) {
        entities.update(delta);

        renderer.begin();
        ScreenUtils.clear(Color.SKY);

        flapper.batch().setProjectionMatrix(renderer.camera().combined);
        flapper.batch().begin();
            flapper.batch().draw(background, 0, 0);
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

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void dispose() {
        renderer.dispose();
    }
}
