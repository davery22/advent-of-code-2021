package advent.of.code.io;

import java.io.IOException;
import java.io.UncheckedIOException;

public class IO {
    @FunctionalInterface
    public interface IORunnable {
        void run() throws IOException;
    }
    @FunctionalInterface
    public interface IOCallable<T> {
        T call() throws IOException;
    }
    
    private IO() {
        throw new AssertionError();
    }
    
    // I don't want to require Input/Output methods to use unchecked exceptions,
    // but I do appreciate having ergonomic ways to do the wrapping ad-hoc when needed.
    
    public static void unchecked(IORunnable runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public static <T> T unchecked(IOCallable<T> callable) {
        try {
            return callable.call();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
