package advent.of.code.io;

import java.util.regex.Pattern;

public class IO {
    // This is the exact regex that Scanner internally uses for line separators.
    public static final Pattern EOL = Pattern.compile("\r\n|[\n\r\u2028\u2029\u0085]");
    
    private IO() {
        throw new AssertionError();
    }
}
