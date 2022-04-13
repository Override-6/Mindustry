package arc.func;

import java.io.Serializable;

public interface Func<P, R> extends Serializable {
    R get(P param);
}
