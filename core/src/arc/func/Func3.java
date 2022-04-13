package arc.func;

import java.io.Serializable;

public interface Func3<P1, P2, P3, R> extends Serializable {
    R get(P1 param1, P2 param2, P3 param3);
}
