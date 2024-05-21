package database;

import java.util.HashMap;

import errors.NotFoundException;
import errors.WrongTypeException;

public class Database {
    HashMap<String, Obj> keys = new HashMap<String, Obj>();

    public void set(String key, String value) {
        keys.put(key, new KVObj(value, 0));
    }

    public String get(String key) throws WrongTypeException, NotFoundException {
        var obj = this.keys.get(key);
        if (obj == null) {
            throw new NotFoundException();
        }

        return switch (obj) {
            case KVObj kv -> kv.value;
            default -> throw new WrongTypeException();
        };
    }

    public int delete(String[] keys) {
        var deleted = 0;
        for (var key : keys) {
            var exists = this.keys.remove(key);
            if (exists != null) {
                deleted++;
            }
        }
        return deleted;
    }
}
