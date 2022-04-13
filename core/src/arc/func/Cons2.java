package arc.func;

import java.io.Serializable;

public interface Cons2<T, N>  extends Serializable {
    void get(T t, N n);
}
