
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import resp.RespSerializer;
import resp.RespSimpleString;

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

    public void nil() throws IOException {
        out.write(RespSerializer.serializeNull().getBytes());
    }

    public void simple(String message) throws IOException {
        out.write(RespSerializer.serializeSimple(message).getBytes());
    }

    public void write(String message) throws IOException {
        out.write(RespSerializer.serialize(message).getBytes());
    }

    public void write(int i) throws IOException {
        out.write(RespSerializer.serialize(i).getBytes());
    }

    public void write(Exception e) throws IOException {
        out.write(RespSerializer.serialize(e).getBytes());
    }

    public void write(Collection<String> col) throws IOException {
        out.write(RespSerializer.serialize(col).getBytes());
    }

    public void write(RespSimpleString string) throws IOException {
        out.write(RespSerializer.serialize(string).getBytes());
    }

    public void writeln(String message) throws IOException {
        out.write((message + "\n").getBytes());
    }

    public InputStream getInputStream() {
        return in;
    }

    public OutputStream getOutputStream() {
        return out;
    }
}
