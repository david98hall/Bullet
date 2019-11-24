package bulletapp.util.bulletpoint;

import bulletapp.util.selection.ISelectionListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Hall
 */
public final class BulletPointZoomHandler {

    @Getter
    private final static BulletPointZoomHandler instance = new BulletPointZoomHandler();

    private final List<ISelectionListener<String>> zoomListeners;

    private BulletPointZoomHandler() {
        zoomListeners = new ArrayList<>(1);
    }

    public void addZoomListener(ISelectionListener<String> listener) {
        zoomListeners.add(listener);
    }

    public void removeZoomListener(ISelectionListener<String> listener) {
        zoomListeners.remove(listener);
    }

    public void zoomToBulletPoint(String bulletPointId) {
        zoomListeners.forEach(listener -> listener.onSelection(bulletPointId));
    }

}
