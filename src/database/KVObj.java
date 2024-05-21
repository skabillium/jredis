package database;

public class KVObj extends Obj {
    String value;

    KVObj(String value, long expiresAt) {
        this.value = value;
        this.expiresAt = expiresAt;
    }
}
