package abalone.Game;

public class Pair {
    private final Integer k;
    private final Integer v;

    /**
     * Creates a new Pair object with the given values.
     * @param k = the first value of the pair
     * @param v = the second value of the pair
     */
    public Pair(Integer k, Integer v) {
        this.k = k;
        this.v = v;
    }

    /**
     * Adds two Pair objects and return a new Pair object with the result (the addition of the two first values as the
     * first value and the addition of the two second values as the second value)
     * @param p1 = the first pair that should be added
     * @param p2 = the second pair that should be added
     * @returns a Pair object with the result of the addition of the two given pairs
     */
    public static Pair add(Pair p1, Pair p2) {
        return new Pair(p1.first() + p2.first(), p1.second() + p2.second());
    }

    /**
     * Returns a new Pair object with the opposite pair of the given pair (the opposite of each value, for example
     * if the pair (1, 1) is given, the method returns the pair (-1, -1)
     * @param p = the pair to be used to calculate the opposite
     * @returns a new Pair object with the opposite values of the given pair
     */
    public static Pair opposite(Pair p) {
        return new Pair((-1) * p.first(), (-1) * p.second());
    }

    /**
     * Returns the first element of the pair
     * @returns the first element of the pair
     */
    public Integer first() {
        return k;
    }

    /**
     * Returns the second element of the pair
     * @returns the second element of the pair
     */
    public Integer second() {
        return v;
    }

    /**
     * A custom hashCode method that overrides the default method so the objects of this class can be keys in a map
     * @returns a hash code of this object based on the values k and v. Pairs with the same value will have the same
     * hash code.
     */
    @Override
    public int hashCode() {
        //multiplying the values with to different prime numbers to ensure unique hash codes for each different pair
        return 31*k + 29*v;
    }

    /**
     * Compares if the given object is equal to this object.
     * @param object = the object to be compared
     * @returns true if this object is equal with the given object and false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (getClass() != object.getClass())
            return false;
        Pair p = (Pair) object;
        return p.k == k && p.v == v;
    }

    /**
     * Returns the string representation of this pair
     * @returns a String object with the string representation of this pair
     */
    @Override
    public String toString() {
        return "(" + k + ", " + v + ")";
    }
}
