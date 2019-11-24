package bulletapp.util.removable;

/**
 * @param <T> The type of what's to be removed at request
 * @author David Hall
 */
public interface IRemoveRequestListener<T> {

    /**
     * Acts on a removal request
     *
     * @param t The object to remove
     */
    void onRemovalRequest(T t);

}
