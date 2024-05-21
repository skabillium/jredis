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

    public int listLength(String key) throws NotFoundException, WrongTypeException {
        var listObj = keys.get(key);
        if (listObj == null) {
            throw new NotFoundException();
        }

        var list = switch (listObj) {
            case ListObj l -> l.list;
            default -> throw new WrongTypeException();
        };

        return list.length;
    }

    public int listLPush(String key, String[] values) throws WrongTypeException {
        var listObj = keys.get(key);
        var found = listObj != null;
        if (!found) {
            listObj = new ListObj(0);
        }
        var list = switch (listObj) {
            case ListObj l -> l.list;
            default -> throw new WrongTypeException();
        };
        for (var value : values) {
            list.prepend(value);
        }
        if (!found) {
            keys.put(key, listObj);
        }
        return values.length;
    }

    public int listRPush(String key, String[] values) throws WrongTypeException {
        var listObj = keys.get(key);
        var found = listObj != null;
        if (!found) {
            listObj = new ListObj(0);
        }
        var list = switch (listObj) {
            case ListObj l -> l.list;
            default -> throw new WrongTypeException();
        };
        for (var value : values) {
            list.append(value);
        }
        if (!found) {
            keys.put(key, listObj);
        }
        return values.length;
    }

    public String listLPop(String key) throws NotFoundException, WrongTypeException {
        var listObj = keys.get(key);
        if (listObj == null) {
            throw new NotFoundException();
        }
        var list = switch (listObj) {
            case ListObj l -> l.list;
            default -> throw new WrongTypeException();
        };
        return list.popHead();
    }

    public String listRPop(String key) throws NotFoundException, WrongTypeException {
        var listObj = keys.get(key);
        if (listObj == null) {
            throw new NotFoundException();
        }
        var list = switch (listObj) {
            case ListObj l -> l.list;
            default -> throw new WrongTypeException();
        };
        return list.popTail();
    }
}
