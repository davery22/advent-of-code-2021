package advent.of.code.io;

import java.io.*;
import java.util.stream.Stream;

/**
 * Convenience wrapper over any InputStream.
 */
class InputImpl implements Input {
    @FunctionalInterface
    private interface Closer {
        void run() throws IOException;
    }
    
    private final InputStream in;
    private Closer onClose;
    
    public InputImpl(InputStream in) {
        this.in = in;
        this.onClose = in::close;
    }
    
    @Override
    public Stream<String> lines() {
        var reader = new BufferedReader(new InputStreamReader(in));
        var prevClose = onClose;
        onClose = () -> {
            try (reader) {
                prevClose.run();
            }
        };
        return reader.lines();
    }
    
    @Override
    public void close() throws IOException {
        onClose.run();
    }
}
