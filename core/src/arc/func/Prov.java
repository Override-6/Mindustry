package arc.func;

import java.io.Serializable;

public interface Prov<T> extends Serializable {
    T get();
}
