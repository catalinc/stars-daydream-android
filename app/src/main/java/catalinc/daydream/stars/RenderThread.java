package catalinc.daydream.stars;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

class RenderThread extends Thread {
    private static final int FPS = 1;
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
            mPaint.setColor(star.getColor());
            canvas.drawCircle(star.getX(), star.getY(), 2, mPaint);
        }
    }
}
