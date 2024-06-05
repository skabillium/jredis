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
}
