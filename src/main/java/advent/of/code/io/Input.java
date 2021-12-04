package advent.of.code.io;

import java.io.InputStream;
import java.util.Scanner;
import java.util.stream.Stream;

public interface Input extends AutoCloseable {
    String readLine();
    Stream<String> lines();
    Scanner scanner();
    void close();
    
    static Input of(InputStream in) {
        return new InputImpl(in);
    }
    
    static Input of(Class<?> clazz, String filename) {
        return new InputImpl(clazz.getResourceAsStream(filename));
    }
}
