package advent.of.code.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Convenience wrapper over any OutputStream.
 */
class OutputImpl implements Output {
    private final OutputStream out;
    
    public OutputImpl(OutputStream out) {
        this.out = out;
    }
    
    @Override
    public void write(Object x) throws IOException {
        out.write(Objects.toString(x).getBytes(StandardCharsets.UTF_8));
    }
    
    @Override
    public void writeln(Object x) throws IOException {
        write(x);
        write(System.lineSeparator());
    }
    
    @Override
    public void close() throws IOException {
        try (out) {
            out.flush();
        }
    }
}
