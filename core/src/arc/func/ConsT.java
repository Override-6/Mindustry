package arc.func;

import java.io.Serializable;

/** A cons that throws something. */
public interface ConsT<T, E extends Throwable>  extends Serializable {
    void get(T t) throws E;
}
