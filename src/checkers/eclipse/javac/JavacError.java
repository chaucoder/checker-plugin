package checkers.eclipse.javac;

import java.io.File;

/**
 * Error reported by javac. Created by parsing javac output.
 */
public class JavacError
{
    private static final boolean VERBOSE = true;
    public final File file;
    public final int lineNumber;
    public final String message;
    public final int startChar;
    public final int endChar;

    public JavacError(File file, int lineNumber, String message, int startChar,
            int endChar)
    {
        this.file = file;
        this.lineNumber = lineNumber;
        this.message = message;
        this.startChar = startChar;
        this.endChar = endChar;
    }

    @Override
    public String toString()
    {
        return file.getPath() + ":" + lineNumber + ": " + message;
    }

}
