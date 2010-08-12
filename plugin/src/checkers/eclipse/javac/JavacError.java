package checkers.eclipse.javac;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

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
    private static final Pattern errorCountPattern = Pattern
            .compile("^[0-9]+ (error|warning)*s?$");
    private static final Pattern messagePattern = Pattern
            .compile("(.*):(\\d*): (?:warning|error): (.*)");

    public static List<JavacError> parse(String javacoutput)
    {
        if (VERBOSE)
            System.out.println("javac output:\n" + javacoutput);
        if (javacoutput == null)
            return null;

        List<JavacError> result = new ArrayList<JavacError>();
        List<String> lines = Arrays.asList(javacoutput.split(Util.NL));

        File errorFile = null;
        int lineNum = 0;
        StringBuilder messageBuilder = new StringBuilder();

        for (String line : lines)
        {
            Matcher matcher = messagePattern.matcher(line);
            if (matcher.matches() && matcher.groupCount() == 3)
            {
                if (errorFile != null)
                {
                    JavacError error = new JavacError(errorFile, lineNum,
                            messageBuilder.toString());
                    result.add(error);
                }
                errorFile = new File(matcher.group(1));
                lineNum = Integer.parseInt(matcher.group(2));
                messageBuilder = new StringBuilder().append(matcher.group(3));
                messageBuilder.append(Util.NL);
            }
            else
            {
                if (errorCountPattern.matcher(line).matches())
                {
                    JavacError error = new JavacError(errorFile, lineNum,
                            messageBuilder.toString());
                    result.add(error);
                }
                else if (!line.trim().equals("^"))
                {
                    messageBuilder.append(line);
                    messageBuilder.append(Util.NL);
                }
            }
        }

        return result;
    }
}
