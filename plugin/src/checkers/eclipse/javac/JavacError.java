package checkers.eclipse.javac;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.commons.lang3.SystemUtils;

import checkers.eclipse.util.Util;

/**
 * Error reported by javac. Created by parsing javac output.
 */
public class JavacError
{
    private static final boolean VERBOSE = true;
    public final File file;
    public final int lineNumber;
    public final String message;
    public final Diagnostic<? extends JavaFileObject> diag;

    public JavacError(File file, int lineNumber, String message)
    {
        this.file = file;
        this.lineNumber = lineNumber;
        this.message = message;
        this.diag = null;
    }

    @Override
    public String toString()
    {
        return file.getPath() + ":" + lineNumber + ": " + message;
    }

    /**
     * Parses javac output and converts to a list of errors.
     */
    // XXX very nasty code - needs cleanup
    private static final Pattern errorCountPattern = Pattern
            .compile("^[0-9]+ (errors|warnings)*$");

    public static List<JavacError> parse(String javacoutput)
    {
        if (VERBOSE)
            System.out.println("javac output:\n" + javacoutput);
        if (javacoutput == null)
            return null;

        List<JavacError> result = new ArrayList<JavacError>();
        List<String> lines = Arrays.asList(javacoutput.split(Util.NL));
        Iterator<String> iter = lines.iterator();
        String line;

        if (!iter.hasNext())
            return result;
        else
            line = iter.next();

        do
        {
            String[] segments = line.split(":");
            if (segments.length < 3 || segments.length > 5)
            {
                if (iter.hasNext())
                    line = iter.next();
                continue; // ??
            }
            try
            {
                int lineNumber = getLineNum(segments);

                // Reconstruct error message
                StringBuilder msg = new StringBuilder();
                if (segments.length > 3)
                    msg.append(segments[3].trim());
                if (segments.length > 4)
                {
                    msg.append(": ");
                    msg.append(segments[4].trim());
                }

                // Loop and capture all of this error's message until
                // we hit the next error (or run out of chars)
                boolean foundNextEntry = false;
                while (!foundNextEntry && iter.hasNext())
                {
                    line = iter.next();
                    String[] newSegments = line.split(":");
                    foundNextEntry = hasFoundNextEntry(newSegments);
                    if (!foundNextEntry
                            && !errorCountPattern.matcher(line).matches()
                            && !line.trim().equals("^"))
                    {
                        msg.append(Util.NL).append(line);
                    }
                }
                File f = fileFromSegments(segments);
                result.add(new JavacError(f, lineNumber, msg.toString()));
            }catch (NumberFormatException e)
            {
                if (iter.hasNext())
                    line = iter.next();
                continue;
            }
        }while (iter.hasNext());
        return result;
    }

    private static int getLineNum(String[] segments)
    {
        if (SystemUtils.IS_OS_WINDOWS)
            return Integer.parseInt(segments[2]);
        else
            return Integer.parseInt(segments[1]);
    }

    private static boolean fileExists(String[] segments)
    {
        if (SystemUtils.IS_OS_WINDOWS)
            return new File(segments[0] + ":" + segments[1]).exists();
        else
            return new File(segments[0]).exists();
    }

    private static File fileFromSegments(String[] segments)
    {
        if (SystemUtils.IS_OS_WINDOWS)
            return new File(segments[0] + ":" + segments[1]);
        else
            return new File(segments[0]);
    }

    private static boolean hasFoundNextEntry(String[] segments)
    {
        int splitLen = segments.length;
        boolean result = (splitLen >= 3 && splitLen <= 5)
                && fileExists(segments);
        return result;
    }
}
