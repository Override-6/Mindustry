package arc.func;

import java.io.Serializable;

public interface Func2<P1, P2, R> extends Serializable {
    R get(P1 param1, P2 param2);
}
