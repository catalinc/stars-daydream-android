package catalinc.daydream.stars;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DreamView extends SurfaceView implements SurfaceHolder.Callback {

    private RenderThread mRenderThread;

    public DreamView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Disable hardware acceleration so we can use BlurMaskFilter
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mRenderThread = new RenderThread(context, holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRenderThread.go();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRenderThread.terminate();
    }
}