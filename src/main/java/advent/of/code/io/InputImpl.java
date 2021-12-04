package advent.of.code.io;

import java.io.*;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Convenience wrapper over any InputStream.
 */
class InputImpl implements Input {
    private final Scanner scanner;
    
    public InputImpl(InputStream in) {
        this.scanner = new Scanner(in);
    }
    
    @Override
    public String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
    
    @Override
    public Stream<String> lines() {
        return scanner.useDelimiter(IO.EOL).tokens();
    }
    
    @Override
    public Scanner scanner() {
        return scanner;
    }
    
    @Override
    public void close() {
        scanner.close();
    }
}
