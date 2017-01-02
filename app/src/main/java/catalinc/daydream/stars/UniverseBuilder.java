package catalinc.daydream.stars;

import android.content.SharedPreferences;
import android.graphics.Color;

class UniverseBuilder {
    private int width;
    private int height;
    private int maxStars;
    private int maxStarLifespan;    // seconds
    private int spawnInterval;      // seconds
    private int[] colorPalette;

    UniverseBuilder() {
        width = 600;
        height = 800;
        maxStars = 100;
        maxStarLifespan = 60;
        spawnInterval = 10;
        colorPalette = new int[]{Color.BLACK, Color.GREEN, Color.YELLOW,
                Color.MAGENTA, Color.CYAN, Color.RED};
    }

    UniverseBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    UniverseBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    UniverseBuilder setMaxStars(int maxStars) {
        this.maxStars = maxStars;
        return this;
    }

    UniverseBuilder setMaxStarLifespan(int maxStarLifespan) {
        this.maxStarLifespan = maxStarLifespan;
        return this;
    }

    UniverseBuilder setSpawnInterval(int spawnInterval) {
        this.spawnInterval = spawnInterval;
        return this;
    }

    UniverseBuilder setColorPalette(int[] colorPalette) {
        this.colorPalette = colorPalette;
        return this;
    }

    UniverseBuilder setPreferences(SharedPreferences preferences) {
        String s = preferences.getString("colorPalette", "");
        if (s.length() > 0) {
            this.colorPalette = parseColorPalette(s);
        }
        return this;
    }

    private int[] parseColorPalette(String s) {
        String[] colorHex = s.split(",");
        int[] colorPalette = new int[colorHex.length];
        for (int i = 0; i < colorHex.length; i++) {
            colorPalette[i] = Color.parseColor(colorHex[i]);
        }
        return colorPalette;
    }

    Universe create() {
        return new Universe(width, height, maxStars,
                maxStarLifespan, spawnInterval, colorPalette);
    }
}
