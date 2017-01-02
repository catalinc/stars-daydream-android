package catalinc.daydream.stars;

import android.service.dreams.DreamService;

public class StarsDreamService extends DreamService {
    @Override
    public void onDreamingStarted() {
        setInteractive(false);
        setFullscreen(true);
        setContentView(R.layout.main);
    }
}
