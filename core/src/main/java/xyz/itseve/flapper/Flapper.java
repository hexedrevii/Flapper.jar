package xyz.itseve.flapper;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import xyz.itseve.flapper.screens.Playing;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Flapper extends Game {
    private SpriteBatch batch;
    public SpriteBatch batch() {
        return batch;
    }

    public final Vector2 GAME_SIZE = new Vector2(288, 512);

    @Override public void create() {
        batch = new SpriteBatch();

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.graphics.setWindowedMode(
                (int)(GAME_SIZE.x * 1.5f), (int)(GAME_SIZE.y * 1.5f)
            );
        }

        setScreen(new Playing(this));
    }
}
