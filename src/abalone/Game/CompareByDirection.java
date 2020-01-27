package abalone.Game;

import java.util.Comparator;

public class CompareByDirection implements Comparator<Pair> {
    Pair direction;

    public CompareByDirection(Pair direction) {
        this.direction = direction;
    }

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
