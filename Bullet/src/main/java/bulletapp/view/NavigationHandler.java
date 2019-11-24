package bulletapp.view;

import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.INavigationListener;

import java.util.ArrayList;
import java.util.List;

public class NavigationHandler {

    private final static List<INavigationListener> navigationListenerList = new ArrayList<>();
    private static List<NavigationTarget> navigationHistory = new ArrayList<>();
    private static int navigationHistoryPointer = -1;
    private static NavigationHandler instance;

    private NavigationHandler() {

    }

    public NavigationHandler getInstance() {
        if (instance == null) {
            instance = new NavigationHandler();
        }
        return instance;
    }

    /**
     * Changes the current navigationTarget to be the previous element in the navigation history and notify listeners.
     */
    public static void goBack() {
        if (canGoBack()) {
            navigationHistoryPointer--;
            notifyListeners(navigationHistory.get(navigationHistoryPointer));
        }
    }

    /**
     * Changes the current navigationTarget to be the subsequent element in the navigation history and notify listeners.
     */
    public static void goForward() {
        if (canGoForward()) {
            navigationHistoryPointer++;
            notifyListeners(navigationHistory.get(navigationHistoryPointer));
        }
    }
    /**
     * Returns true if the current Navigation target have a previous target in the NavigationHistory
     */
    public static boolean canGoBack() {
        return navigationHistoryPointer > 0;
    }

    /**
     * Returns true if the current Navigation target have a subsequent target in the NavigationHistory
     */
    public static boolean canGoForward() {
        return (navigationHistoryPointer < navigationHistory.size()) && navigationHistoryPointer != -1;
    }

    /**
     * Adds a INavigationListener which will be notified whenever the value of the NavigationTarget changes.
     *
     * @param navigationListener The listener to register
     */
    public static void addNavigationListener(INavigationListener navigationListener) {
        navigationListenerList.add(navigationListener);
    }

    /**
     * Changes the current navigation target and notify all the listeners.
     *
     * @param target the new target
     */
    public static void navigateTo(NavigationTarget target) {

        if (navigationHistory.size() > navigationHistoryPointer && navigationHistoryPointer != -1) {
            List<NavigationTarget> temp = navigationHistory.subList(0, navigationHistoryPointer);
            temp.add(navigationHistory.get(navigationHistoryPointer));
            navigationHistory = temp;
        }

        notifyListeners(target);
    }


    /**
     * Invoked when an NavigationTarget change occurs and notify all the listeners of the occurrence.
     *
     * @param target the new target
     */
    private static void notifyListeners(NavigationTarget target) {
        navigationHistoryPointer++;

        navigationHistory.add(navigationHistoryPointer, target);

        navigationListenerList.forEach(listener -> {
            listener.navigateTo(target);
        });
    }
}
