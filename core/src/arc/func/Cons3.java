package arc.func;

import java.io.Serializable;

public interface Cons3<P1, P2, P3>  extends Serializable {
    void get(P1 param1, P2 param2, P3 param3);
}
