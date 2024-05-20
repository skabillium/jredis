
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectionContext {
    private InputStream in;
    private OutputStream out;

    ConnectionContext(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void write(String message) throws IOException {
        out.write(message.getBytes());
    }

    public void writeln(String message) throws IOException {
        out.write((message + "\n").getBytes());
    }
}
