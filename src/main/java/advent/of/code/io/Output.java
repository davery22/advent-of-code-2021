package advent.of.code.io;

import java.io.OutputStream;

public interface Output extends AutoCloseable {
    Output write(Object x);
    Output writef(String format, Object... args);
    Output writeln(Object x);
    Output writeln();
    void close();
    
    static Output of(OutputStream out) {
        return new OutputImpl(out);
    }
}
