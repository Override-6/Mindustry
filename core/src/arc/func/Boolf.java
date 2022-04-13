package arc.func;

import java.io.Serializable;

public interface Boolf<T>  extends Serializable {
    boolean get(T t);
}
