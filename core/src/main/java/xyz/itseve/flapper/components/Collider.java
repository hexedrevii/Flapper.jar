package xyz.itseve.flapper.components;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.lang.ref.Reference;
import java.util.function.Consumer;

import xyz.itseve.flapper.Flapper;
import xyz.itseve.flapper.entities.Entity;

public class Collider extends Component {
    private final Rectangle area;
    private final Vector2 position;

    public Consumer<Entity> onCollide;

    public Collider(Vector2 pos, Vector2 size) {
        area = new Rectangle(pos.x, pos.y, size.x, size.y);
        position = pos;
    }

    public boolean overlaps(Collider other) {
        return area.overlaps(other.area);
    }

    @Override public void update(float delta) {
        area.x = position.x;
        area.y = position.y;
    }

    @Override public void show() {}
    @Override public void render(float delta) {
    }
}
