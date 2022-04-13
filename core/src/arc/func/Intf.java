package arc.func;

import java.io.Serializable;

public interface Intf<T> extends Serializable {
    int get(T t);
}
