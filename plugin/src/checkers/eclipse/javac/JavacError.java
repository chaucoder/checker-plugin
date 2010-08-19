package checkers.eclipse.javac;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.eclipse.ui.statushandlers.StatusManager;

import checkers.eclipse.CheckerPlugin;
import checkers.eclipse.error.CheckerErrorStatus;
import checkers.eclipse.prefs.CheckerPreferences;
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
            .compile("(.*):(\\d*): (?:(?:warning|error)?: ?)?(.*)");
    private static final Pattern noProcessorPattern = Pattern
            .compile("^error: Annotation processor (.*) not found$");

    public static List<JavacError> parse(String javacoutput)
    {
        if (VERBOSE)
            System.out.println("javac output:\n" + javacoutput);
        if (javacoutput == null)
            return null;

        List<JavacError> result = new ArrayList<JavacError>();
        List<String> lines = Arrays.asList(javacoutput.split(Util.NL));

        // special case for missing checkers.jar (or processor class)
        Matcher procMatcher = noProcessorPattern.matcher(lines.get(0));
        if (procMatcher.matches())
        {
            CheckerErrorStatus status = new CheckerErrorStatus(
                    "Checker processor "
                            + procMatcher.group(1)
                            + " could not be found. Try adding checkers.jar to your project build path.");
            StatusManager.getManager().handle(status, StatusManager.SHOW);
            return result;
        }

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
                            messageBuilder.toString().trim());
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
                            messageBuilder.toString().trim());
                    result.add(error);
                }
                else if (!line.trim().equals("^"))
                {
                    messageBuilder.append(line);
                    messageBuilder.append(Util.NL);
                }
            }
        }

        // filter out for errors/warnings matching a regex
        String filterRegex = CheckerPlugin.getDefault().getPreferenceStore()
                .getString(CheckerPreferences.PREF_CHECKER_ERROR_FILTER_REGEX);
        if (!filterRegex.isEmpty())
        {
            Iterator<JavacError> iter = result.iterator();
            while (iter.hasNext())
            {
                JavacError err = iter.next();
                Matcher filterMatcher = Pattern.compile(filterRegex,
                        Pattern.DOTALL).matcher(err.message);
                if (filterMatcher.matches())
                {
                    iter.remove();
                }
            }
        }

        return result;
    }
}
