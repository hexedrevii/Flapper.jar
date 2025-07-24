package xyz.itseve.flapper.util;

import java.util.Random;

public class MathF {
    public static float moveTowards(float from, float to, float delta) {
        if (Math.abs(to - from) <= delta) return to;

        return from + Math.signum(to - from) * delta;
    }

    public static float nextFloat(Random random, float min, float max) {
        return min + random.nextFloat() * (max - min);
    }
}
