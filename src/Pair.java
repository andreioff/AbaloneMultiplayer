public class Pair<K, V> {
    private final K k;
    private final V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K first() {
        return k;
    }

    public V second() {
        return v;
    }
}
