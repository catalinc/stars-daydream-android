package catalinc.daydream.stars;

class Star {
    private int x;
    private int y;
    private int color;
    private int lifespan;

    Star(int x, int y, int color, int lifespan) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.lifespan = lifespan;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getColor() {
        return color;
    }

    int getLifespan() {
        return lifespan;
    }

    void update() {
        this.lifespan -= 1;
    }

    boolean isDead() {
        return lifespan < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Star star = (Star) o;

        if (x != star.x) return false;
        return y == star.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
