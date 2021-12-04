package advent.of.code.io;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Convenience wrapper over any OutputStream.
 */
class OutputImpl implements Output {
    private final PrintStream printer;
    
    public OutputImpl(OutputStream out) {
        this.printer = new PrintStream(out);
    }
    
    @Override
    public OutputImpl write(Object x) {
        printer.print(x);
        return this;
    }
    
    @Override
    public OutputImpl writef(String format, Object... args) {
        printer.printf(format, args);
        return this;
    }
    
    @Override
    public OutputImpl writeln(Object x) {
        printer.println(x);
        return this;
    }
    
    @Override
    public OutputImpl writeln() {
        printer.println();
        return this;
    }
    
    @Override
    public void close() {
        try (printer) {
            printer.flush();
        }
    }
}
