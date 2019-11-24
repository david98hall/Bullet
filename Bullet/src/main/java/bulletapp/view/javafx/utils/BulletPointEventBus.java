package bulletapp.view.javafx.utils;

import bulletapp.util.document.IDocumentCreationListener;
import bulletapp.util.user.IUserCreationListener;
import javafx.scene.input.KeyCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Hall
 */
public class BulletPointEventBus {

    @Getter
    private static final BulletPointEventBus instance = new BulletPointEventBus();

    private final List<IBulletPointKeyListener> keyListeners;
    private final List<IDocumentCreationListener> documentCreationListeners;
    private final List<IUserCreationListener> userCreationListeners;

    private BulletPointEventBus() {
        keyListeners = new ArrayList<>(1);
        documentCreationListeners = new ArrayList<>(1);
        userCreationListeners = new ArrayList<>(1);
    }

    public void addKeyListener(IBulletPointKeyListener keyListener) {
        keyListeners.add(keyListener);
    }

    public void removeKeyListener(IBulletPointKeyListener keyListener) {
        keyListeners.remove(keyListener);
    }

    public void notifyKeyPressedListeners(String bulletPointId, KeyCode keyCode) {
        keyListeners.forEach(keyListener -> keyListener.onKeyPressed(bulletPointId, keyCode));
    }

    public void addDocumentCreationListener(IDocumentCreationListener creationListener) {
        documentCreationListeners.add(creationListener);
    }

    public void removeDocumentNameChangeListener(IDocumentCreationListener nameListener) {
        documentCreationListeners.remove(nameListener);
    }

    public void notifyDocumentCreationListeners(String documentName) {
        documentCreationListeners.forEach( nameListener ->
                nameListener.onDocumentCreated(documentName));
    }

    public void addUserCreationListener(IUserCreationListener userCreationListener) {
        userCreationListeners.add(userCreationListener);
    }

    public void notifyUserCreationListeners(String username) {
        userCreationListeners.forEach(userListener ->
                userListener.onUserCreated(username));
    }



}
