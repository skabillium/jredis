package database;

public abstract class Obj {
    long expiresAt;

    public boolean isExpired() {
        return expiresAt != 0 && System.currentTimeMillis() > expiresAt;
    }

    public void expireIn(int seconds) {
        expiresAt = System.currentTimeMillis() + (long) seconds * 1000;
    }
}
