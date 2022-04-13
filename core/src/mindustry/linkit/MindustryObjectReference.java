package mindustry.linkit;

import fr.linkit.api.gnom.reference.NetworkObjectReference;

import java.util.Objects;

public class MindustryObjectReference implements NetworkObjectReference {

    private final String name;

    public MindustryObjectReference(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "@mindustry/" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MindustryObjectReference that = (MindustryObjectReference) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
