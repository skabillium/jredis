package resp;

import java.util.Collection;

public class RespSerializer {
    public static String serialize(String str) {
        return String.format("$%d\r\n%s\r\n", str.length(), str);
    }

    public static String serialize(Exception e) {
        return String.format("-%s\r\n", e.getMessage());
    }

    public static String serialize(int i) {
        return String.format(":%d\r\n", i);
    }

    public static String serialize(Collection<String> strs) {
        var res = String.format("*%d\r\n", strs.size());
        for (var str : strs) {
            res = res.concat(serialize(str));
        }
        return res;
    }

    public static String serializeNull() {
        return "$-1\r\n";
    }

    public static String serializeSimple(String str) {
        return "+" + str + "\r\n";
    }
}
