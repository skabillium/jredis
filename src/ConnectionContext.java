
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import resp.RespSerializer;

public class ConnectionContext {
    private InputStream in;
    private OutputStream out;

    ConnectionContext(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void ok() throws IOException {
        out.write(RespSerializer.serializeSimple("OK").getBytes());
    }

    public void write(String message) throws IOException {
        out.write(RespSerializer.serialize(message).getBytes());
    }

    public void write(Exception e) throws IOException {
        out.write(RespSerializer.serialize(e).getBytes());
    }

    public void writeln(String message) throws IOException {
        out.write((message + "\n").getBytes());
    }
}
