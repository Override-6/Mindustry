package arc.func;

import java.io.Serializable;

public interface Boolf3<P1, P2, P3>  extends Serializable {
    boolean get(P1 a, P2 b, P3 c);
}
