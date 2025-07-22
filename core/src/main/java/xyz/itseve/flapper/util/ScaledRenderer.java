package xyz.itseve.flapper.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class ScaledRenderer implements Disposable {
    private final int width, height;

    private final FrameBuffer buffer;
    private final OrthographicCamera camera;
    private final TextureRegion renderRegion;

    public final OrthographicCamera camera() {
        return camera;
    }

    public ScaledRenderer(int width, int height) {
        this.width = width;
        this.height = height;

        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);

        renderRegion = new TextureRegion(buffer.getColorBufferTexture());
        renderRegion.flip(false, true);
        renderRegion.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public ScaledRenderer(Vector2 size) {
        this((int)size.x, (int)size.y);
    }

    public void begin() {buffer.begin();}
    public void end() {buffer.end();}

    public void render(Batch batch) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float scale = Math.min(screenWidth / width, screenHeight / height);
        float renderWidth = width * scale;
        float renderHeight = height * scale;

        float x = (screenWidth - renderWidth) * 0.5f;
        float y = (screenHeight - renderHeight) * 0.5f;

        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, screenWidth, screenHeight));
        batch.begin();
            batch.draw(renderRegion, x, y, renderWidth, renderHeight);
        batch.end();
    }

    @Override public void dispose() {
        buffer.dispose();
    }
}
