package xyz.itseve.flapper.screens;

import com.badlogic.gdx.Screen;

import xyz.itseve.flapper.Flapper;

public class Playing implements Screen {
    private final Flapper flapper;

    public Playing(Flapper flapper) {
        this.flapper = flapper;
    }

    @Override public void show() {
    }

    @Override public void render(float delta) {
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
    }
}
