package xyz.itseve.flapper.util;

public class MathF {
    public static float moveTowards(float from, float to, float delta) {
        if (Math.abs(to - from) <= delta) return to;

        return from + Math.signum(to - from) * delta;
    }
}
