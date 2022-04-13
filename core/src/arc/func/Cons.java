package arc.func;

import java.io.Serializable;

public interface Cons<T>  extends Serializable {
    void get(T t);
}
