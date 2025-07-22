package xyz.itseve.flapper;

import com.badlogic.gdx.Game;

import xyz.itseve.flapper.screens.Playing;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Flapper extends Game {
    @Override
    public void create() {
        setScreen(new Playing(this));
    }
}
