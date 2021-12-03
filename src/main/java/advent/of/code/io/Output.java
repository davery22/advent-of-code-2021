package advent.of.code.io;

import java.io.IOException;
import java.io.OutputStream;

public interface Output extends AutoCloseable {
    void write(Object x) throws IOException;
    void writeln(Object x) throws IOException;
    void close() throws IOException;
    
    static Output of(OutputStream out) {
        return new OutputImpl(out);
    }
}
