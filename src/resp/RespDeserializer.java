package resp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RespDeserializer {
    private final BufferedReader reader;

    public RespDeserializer(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public Object deserialize() throws IOException {
        var c = reader.read();
        if (c == -1) {
            return null;
        }

        var type = (char) c;
        return switch (type) {
            case '$' -> deserializeBulkString();
            case '*' -> deserializeArray();
            default -> type + reader.readLine();
        };
    }

    private List<Object> deserializeArray() throws IOException {
        var length = Integer.parseInt(reader.readLine());
        if (length == -1) {
            return null;
        }
        var elements = new ArrayList<>(length);
        for (var i = 0; i < length; i++) {
            elements.add(deserialize());
        }
        return elements;
    }

    private String deserializeBulkString() throws IOException {
        var length = Integer.parseInt(reader.readLine());
        if (length == -1) {
            return null;
        }
        var buffer = new char[length];
        reader.read(buffer, 0, length);
        reader.readLine();
        return new String(buffer);
    }
}
