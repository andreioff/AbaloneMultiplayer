package abalone.Game;

public class Pair {
    private final Integer k;
    private final Integer v;

    public Pair(Integer k, Integer v) {
        this.k = k;
        this.v = v;
    }

    public static Pair add(Pair p1, Pair p2) {
        return new Pair(p1.first() + p2.first(), p1.second() + p2.second());
    }

    public static Pair opposite(Pair p) {
        return new Pair((-1) * p.first(), (-1) * p.second());
    }

    public Integer first() {
        return k;
    }

    public Integer second() {
        return v;
    }

    @Override
    public int hashCode() {
        return 31*k + 29*v;
    }

    @Override
    public boolean equals(Object object) {
        if (getClass() != object.getClass())
            return false;
        Pair p = (Pair) object;
        return p.k == k && p.v == v;
    }

    @Override
    public String toString() {
        return "(" + k + ", " + v + ")";
    }
}
