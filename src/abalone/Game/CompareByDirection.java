package abalone.Game;

import java.util.Comparator;

public class CompareByDirection implements Comparator<Pair> {
    //A pair of coordinates representing a 2d direction vector
    Pair direction;

    /**
     * Creates a new CompareByDirection comparator
     * @param direction = a given direction
     */
    public CompareByDirection(Pair direction) {
        this.direction = direction;
    }

    /**
     * Compares two pairs of coordinates based on this.direction.
     * @param p1 = the first pair of coordinates to be compared
     * @param p2 = the second pair of coordinates to be compared
     * @returns 1 if the first pair is further than the second pair in this.direction, 0 if the pairs coordinates are
     * the same and -1 if the second pair is further than the first pair in this.direction
     */
    @Override
    public int compare(Pair p1, Pair p2) {
        Pair p3 = new Pair(p1.first() * direction.first(), p1.second() * direction.second());
        Pair p4 = new Pair(p2.first() * direction.first(), p2.second() * direction.second());
        if (p3.first() < p4.first())
            return 1;

        if (p3.first() == p4.first()) {
            if (p3.second() < p4.second()) return 1;
            if (p3.second() == p4.second()) return 0;
        }

        return -1;
    }
}
