package checkers.eclipse.javac;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import checkers.eclipse.Activator;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;

/**
 * This class is used to run the checker from the Sun Compiler API rather than
 * using the commandline.
 * 
 * @author asumu
 */
public class JavacRunner
{
    public static final String CHECKERS_LOCATION = "lib/checkers.jar";
    public static final String JAVAC_LOCATION = "lib/javac.jar";
    public static final String JDK_LOCATION = "lib/jdk.jar";
    public static final List<String> IMPLICIT_ARGS = Arrays.asList(
            "checkers.nullness.quals.*", "checkers.igj.quals.*",
            "checkers.javari.quals.*", "checkers.interning.quals.*");

    // TODO: commented out for reasons documented below
    // private static final SourceVersion REQUIRED_RELEASE =
    // SourceVersion.RELEASE_7;

    private final DiagnosticCollector<JavaFileObject> collector;

    public JavacRunner()
    {
        collector = new DiagnosticCollector<JavaFileObject>();
    }

    /**
     * Runs the compiler on the selected files using the given processor
     * 
     * @param fileNames
     *            files that need to be type checked
     * @param processors
     *            Type processors to run
     * @param classpath
     *            The classpath to reference in compilation
     */
    public void run(List<String> fileNames, List<String> processors,
            String classpath)
    {
        Iterable<String> opts;

        try
        {
            opts = getOptions(processors, classpath);

            // TODO: the following is the correct way to use the Compiler API,
            // but unfortunately it's difficult to get working because of
            // runtime classpath issues.

            // Load installed compilers and make sure to select one that
            // supports the correct release
            /*
             * JavaCompiler compiler = null; ServiceLoader<JavaCompiler>
             * compilers = ServiceLoader .load(JavaCompiler.class);
             * 
             * for (JavaCompiler candidateCompiler : compilers) { if
             * (candidateCompiler.getSourceVersions().contains(
             * REQUIRED_RELEASE)) { compiler = candidateCompiler; break; } }
             */

            // TODO: raise error here that compiler can't be found
            /*
             * if (compiler == null) return;
             */

            /*
             * StandardJavaFileManager manager =
             * compiler.getStandardFileManager( null, null, null); Iterable<?
             * extends JavaFileObject> fileObjs = manager
             * .getJavaFileObjectsFromStrings(fileNames);
             * 
             * CompilationTask task = compiler.getTask(null, null, collector,
             * opts, processors, fileObjs);
             */

            // The following code uses the compiler's internal APIs, which are
            // volatile. (see warning in JavacTool source)
            JavacTool tool = JavacTool.create();
            JavacFileManager manager = tool.getStandardFileManager(collector,
                    null, null);

            Iterable<? extends JavaFileObject> fileObjs = manager
                    .getJavaFileObjectsFromStrings(fileNames);

            JavacTask task = tool.getTask(null, null, collector, opts,
                    processors, fileObjs);

            task.call();
            manager.close();
        }catch (Exception e)
        {
            Activator.logException(e, "Error in running javac");
        }

    }

    private Iterable<String> getOptions(List<String> processors,
            String classpath) throws IOException
    {
        List<String> opts = new ArrayList<String>();
        opts.add("-verbose");
        opts.add("-Xlint:all");
        opts.add("-proc:only");
        opts.add("-Xbootclasspath/p:" + getLocation(JAVAC_LOCATION) + ":"
                + getLocation(JDK_LOCATION));
        opts.add("-XprintProcessorInfo");
        opts.add("-Xprefer:source");

        // Build the processor arguments, comma separated
        StringBuilder processorStr = new StringBuilder();
        Iterator<String> itr = processors.iterator();

        while (itr.hasNext())
        {
            processorStr.append(itr.next());
            if (itr.hasNext())
            {
                processorStr.append(",");
            }
        }

        opts.add("-processor");
        opts.add(processorStr.toString());

        opts.add("-cp");
        opts.add(classpath);

        return opts;
    }

    private String getLocation(String path) throws IOException
    {
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);

        Path javacJAR = new Path(path);
        URL javacJarURL = FileLocator.toFileURL(FileLocator.find(bundle,
                javacJAR, null));
        return javacJarURL.getPath();
    }

    public List<Diagnostic<? extends JavaFileObject>> getErrors()
    {
        return collector.getDiagnostics();
    }
}
