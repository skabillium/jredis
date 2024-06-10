package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import errors.NotFoundException;
import errors.WrongTypeException;

public class Database {
    HashMap<String, Obj> keys = new HashMap<String, Obj>();

    public ArrayList<String> listKeys(String pattern) {
        var p = Pattern.compile(createRegexFromGlob(pattern));
        var keys = new ArrayList<String>();
        for (var entry : this.keys.entrySet()) {
            if (p.matcher(entry.getKey()).find()) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public void set(String key, String value, int expires) {
        var obj = new KVObj(value, 0);
        if (expires != 0) {
            obj.expireIn(expires);
        }
        keys.put(key, obj);
    }

    public String get(String key) throws WrongTypeException, NotFoundException {
        var obj = getObj(key);
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

    public int setAdd(String key, String[] values) throws WrongTypeException {
        var obj = getObj(key);
        var found = obj != null;
        if (!found) {
            obj = new SetObj();
        }

        var setobj = asSetObj(obj);
        var added = 0;
        for (var i = 0; i < values.length; i++) {
            added += setobj.add(values[i]) ? 1 : 0;
        }

        if (!found) {
            keys.put(key, obj);
        }

        return added;
    }

    public int setRemove(String key, String[] values) throws WrongTypeException, NotFoundException {
        var obj = getObj(key);
        if (obj == null) {
            throw new NotFoundException();
        }
        var setobj = asSetObj(obj);
        return setobj.remove(values);
    }

    public boolean setIsMember(String key, String value) throws WrongTypeException, NotFoundException {
        var obj = getObj(key);
        if (obj == null) {
            throw new NotFoundException();
        }
        var setobj = asSetObj(obj);
        return setobj.isMember(value);
    }

    public int setCardinality(String key) throws WrongTypeException, NotFoundException {
        var obj = getObj(key);
        if (obj == null) {
            throw new NotFoundException();
        }
        var setobj = asSetObj(obj);
        return setobj.getCardinality();
    }

    public ArrayList<String> setIntersection(String[] keys) throws WrongTypeException, NotFoundException {
        var sets = new ArrayList<HashSet<String>>();
        for (var key : keys) {
            var obj = getObj(key);
            if (obj == null) {
                throw new NotFoundException();
            }
            var set = switch (obj) {
                case SetObj s -> s.set;
                default -> throw new WrongTypeException();
            };
            sets.add(set);
        }

        var intersection = new HashSet<String>(sets.getFirst());
        for (var set : sets) {
            intersection.retainAll(set);
        }

        return new ArrayList<String>(intersection);
    }

    private Obj getObj(String key) {
        var obj = keys.get(key);
        if (obj == null) {
            return null;
        }
        if (obj.isExpired()) {
            keys.remove(key);
            return null;
        }
        return obj;
    }

    private SetObj asSetObj(Obj obj) throws WrongTypeException {
        return switch (obj) {
            case SetObj set -> set;
            default -> throw new WrongTypeException();
        };
    }

    private String createRegexFromGlob(String glob) {
        String out = "^";
        for (int i = 0; i < glob.length(); ++i) {
            final char c = glob.charAt(i);
            switch (c) {
                case '*':
                    out += ".*";
                    break;
                case '?':
                    out += '.';
                    break;
                case '.':
                    out += "\\.";
                    break;
                case '\\':
                    out += "\\\\";
                    break;
                default:
                    out += c;
            }
        }
        out += '$';
        return out;
    }

}
