package database;

import java.util.HashSet;

public class SetObj extends Obj {
    HashSet<String> set = new HashSet<String>();

    SetObj() {
        this.expiresAt = 0;
    }

    SetObj(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    boolean add(String value) {
        return set.add(value);
    }

    int remove(String[] values) {
        var removed = 0;
        for (var i = 0; i < values.length; i++) {
            removed += set.remove(values[i]) ? 1 : 0;
        }

        return removed;
    }

    boolean isMember(String value) {
        return set.contains(value);
    }

    int getCardinality() {
        return set.size();
    }
}
