package advent.of.code.io;

import java.io.*;
import java.util.stream.Stream;

/**
 * Convenience wrapper over any InputStream.
 */
class InputImpl implements Input {
    private final InputStream in;
    private BufferedReader reader;
    
    public InputImpl(InputStream in) {
        this.in = in;
    }
    
    @Override
    public String readLine() throws IOException {
        return getReader().readLine();
    }
    
    @Override
    public Stream<String> lines() {
        return getReader().lines();
    }
    
    @Override
    public void close() throws IOException {
        if (reader == null) {
            in.close();
        } else {
            try (var ignored = reader) {
                in.close();
            }
        }
    }
    
    private BufferedReader getReader() {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(in));
        }
        return reader;
    }
}
