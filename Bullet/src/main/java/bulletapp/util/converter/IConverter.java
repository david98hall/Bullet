package bulletapp.util.converter;

/**
 * Can convert an object of one type to an object of another.
 *
 * @param <A> The type of what's to be converted.
 * @param <B> The type of the converted object.
 * @author David Hall
 */
public interface IConverter<A, B> {

    /**
     * Converts A to B.
     *
     * @param a The conversion target
     * @return The conversion.
     */
    B convert(A a);

}
