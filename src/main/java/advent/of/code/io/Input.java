package advent.of.code.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public interface Input extends AutoCloseable {
    String readLine() throws IOException;
    Stream<String> lines();
    void close() throws IOException;
    
    static Input of(InputStream in) {
        return new InputImpl(in);
    }
    
    static Input of(Class<?> clazz, String filename) {
        return new InputImpl(clazz.getResourceAsStream(filename));
    }
}
