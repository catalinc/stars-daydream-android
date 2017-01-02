package catalinc.daydream.stars;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

class RenderThread extends Thread {
    private static final int FPS = 1;
    private static final double DEGREES_144 = Math.toRadians(144);

    private SurfaceHolder mSurfaceHolder;
    private Universe mUniverse;
    private Paint mPaint;
    private volatile boolean mRunning;

    RenderThread(Context context, SurfaceHolder mSurfaceHolder) {
        this.mSurfaceHolder = mSurfaceHolder;

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        mUniverse = new UniverseBuilder()
                .setPreferences(preferences)
                .setHeight(displayMetrics.heightPixels)
                .setWidth(displayMetrics.widthPixels)
                .create();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    void go() {
        mRunning = true;
        start();
    }

    @Override
    public void run() {
        long msPerFrame = 1000 / FPS;
        while (mRunning) {
            long startTime = System.currentTimeMillis();
            mUniverse.update(startTime);
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                drawUniverse(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            long sleepTime = startTime + msPerFrame - System.currentTimeMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    void terminate() {
        mRunning = false;
        interrupt();
        try {
            join();
        } catch (InterruptedException e) {
            // ignore
        }
    }

    private void drawUniverse(Canvas canvas) {
        // draw background
        mPaint.setColor(mUniverse.getBackgroundColor());
        canvas.drawPaint(mPaint);

        // draw stars
        for (Star star : mUniverse.getStars()) {
            drawStar(canvas, star);
        }
    }

    private void drawStar(Canvas canvas, Star star) {
        mPaint.setColor(star.getColor());
        Path path = new Path();
        int x = star.getX();
        int y = star.getY();
        path.moveTo(x, y);
        double angle = 0;
        for (int i = 0; i < 5; i++) {
            int x2 = x + (int) (Math.cos(angle) * 40);
            int y2 = y + (int) (Math.sin(-angle) * 40);
            path.lineTo(x2, y2);
            x = x2;
            y = y2;
            angle -= DEGREES_144;
        }
        path.close();
        canvas.drawPath(path, mPaint);
    }
}
