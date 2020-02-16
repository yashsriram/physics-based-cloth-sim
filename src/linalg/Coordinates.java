package linalg;

import java.util.Objects;

public class Coordinates {
    int i;
    int j;

    public static Coordinates of(int i, int j) {
        return new Coordinates(i, j);
    }

    private Coordinates(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return i == that.i &&
                j == that.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}
