package checkers.eclipse.javac;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.tools.*;

import org.eclipse.core.runtime.*;
import org.osgi.framework.*;

import checkers.eclipse.*;

import com.sun.source.util.*;
import com.sun.tools.javac.api.*;
import com.sun.tools.javac.file.*;

/**
 * This class is used to run the checker from the Sun Compiler API rather
 * than using the commandline.
 * 
 * @author asumu
 */
public class JavacRunner{
    public static final String CHECKERS_LOCATION = "lib/checkers.jar";
    public static final String JAVAC_LOCATION = "lib/javac.jar";
    public static final List<String> IMPLICIT_ARGS = Arrays.asList(
            "checkers.nullness.quals.*", "checkers.igj.quals.*",
            "checkers.javari.quals.*", "checkers.interning.quals.*");

    private DiagnosticCollector<JavaFileObject> collector;

    public JavacRunner(){
        collector = new DiagnosticCollector<JavaFileObject>();
    }

    /**
     * Runs the compiler on the selected files using the given processor
     * 
     * @param fileNames
     * @param processor
     * @param classpath
     * 
     * TODO: adapt to the multiple processor setup used for the commandline
     * version of this class
     */
    public void run(List<String> fileNames, String processor, String classpath){
        Iterable<String> opts;

        try{
            opts = getOptions(processor, classpath);

            // JavaCompiler compiler = ServiceLoader.load(JavaCompiler.class).iterator().next();
            // JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            JavacTool tool = JavacTool.create();
            JavacFileManager manager = tool.getStandardFileManager(null, null,
                    null);
            // StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> fileObjs = manager
                    .getJavaFileObjectsFromStrings(fileNames);

            JavacTask task = tool.getTask(null, null, collector, opts, null,
                    fileObjs);
            // CompilationTask task = compiler.getTask(null, null, collector, opts, null, fileObjs);

            task.call();
        }catch (Exception e){
            Activator.logException(e, "Error in running javac");
        }
    }

    private Iterable<String> getOptions(String processor, String classpath)
            throws IOException{
        List<String> opts = new ArrayList<String>();
        opts.add("-verbose");
        // opts.add("-ea:com.sun.tools");
        opts.add("-proc:only");
        opts.add("-Xbootclasspath/p:" + javacJARLocation());
        // opts.add("-jar");
        // opts.add(javacJARLocation());
        opts.add("-processor");
        opts.add(processor);
        opts.add("-cp");
        opts.add(classpath);

        return opts;
    }

    private String javacJARLocation() throws IOException{
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);

        Path javacJAR = new Path(JAVAC_LOCATION);
        URL javacJarURL = FileLocator.toFileURL(FileLocator.find(bundle,
                javacJAR, null));
        return javacJarURL.getPath();
    }

    public List<Diagnostic<? extends JavaFileObject>> getErrors(){
        return collector.getDiagnostics();
    }
}
