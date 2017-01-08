package catalinc.daydream.stars;

import java.util.*;

class Universe {
    private int width;
    private int height;
    private int maxStars;
    private int maxStarLifespan;
    private int spawnInterval;
    private int[] colorPalette;
    private Set<Star> stars;
    private long lastSpawn;
    private Random random;
    private int maxStarSize;

    Universe(int width, int height,
             int maxStars, int maxStarLifespan,
             int spawnInterval, int[] colorPalette) {
        this.width = width;
        this.height = height;
        this.maxStars = maxStars;
        this.maxStarLifespan = maxStarLifespan;
        this.spawnInterval = spawnInterval * 1000;
        this.colorPalette = colorPalette;
        this.stars = new HashSet<>(maxStars);
        this.lastSpawn = -1L;
        this.random = new Random();
        this.maxStarSize = Math.min(width, height) / 10;
    }

    void update(long now) {
        if (now - lastSpawn > spawnInterval) {
            fillWithStars();
            lastSpawn = now;
        }
        updateStars();
    }

    Set<Star> getStars() {
        return stars;
    }

    int getColor() {
        return colorPalette[0];
    }

    int getStarSize(Star star) {
        double p = Math.min(1.0, (double) (maxStarLifespan - star.getLifespan()) / maxStarLifespan);
        return (int) (p * maxStarSize);
    }

    float getStarGlowSize() {
        return maxStarSize / 3.0f;
    }

    boolean shouldGlow(Star star) {
        return star.getLifespan() < maxStarLifespan / 3;
    }

    private void updateStars() {
        Iterator<Star> it = stars.iterator();
        while (it.hasNext()) {
            Star star = it.next();
            if (star.isDead()) {
                it.remove();
            } else {
                star.update();
            }
        }
    }

    private void fillWithStars() {
        while (stars.size() < maxStars) {
            spawnStar();
        }
    }

    private void spawnStar() {
        Star star = randomStar();
        while (stars.contains(star)) {
            star = randomStar();
        }
        stars.add(star);
    }

    private Star randomStar() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int index = 1 + random.nextInt(colorPalette.length - 1);
        int color = colorPalette[index];
        int lifespan = random.nextInt(maxStarLifespan);
        return new Star(x, y, color, lifespan);
    }
}
