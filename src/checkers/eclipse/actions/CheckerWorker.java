package checkers.eclipse.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import checkers.eclipse.Activator;
import checkers.eclipse.javac.CommandlineJavacRunner;
import checkers.eclipse.javac.JavacError;
import checkers.eclipse.util.MarkerUtil;
import checkers.eclipse.util.Paths;
import checkers.eclipse.util.ResourceUtils;
import checkers.eclipse.util.Paths.ClasspathBuilder;

public class CheckerWorker extends Job
{

    private final IJavaElement element;
    private final List<String> checkerNames;

    public CheckerWorker(IJavaElement element, String checkerName)
    {
        super("Running checker on " + element.getElementName());
        this.element = element;
        this.checkerNames = new ArrayList<String>();
        this.checkerNames.add(checkerName);
    }

    public CheckerWorker(IJavaElement element, List<String> checkerNames)
    {
        super("Running checker on " + element.getElementName());
        this.element = element;
        this.checkerNames = checkerNames;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        try
        {
            work(monitor);
        }catch (Throwable e)
        {
            Activator.logException(e, "Analysis exception");
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    private void work(IProgressMonitor pm) throws CoreException
    {
        pm.beginTask("Running checkers " + checkerNames.toString() + " on "
                + element.getElementName(), 10);

        pm.setTaskName("Removing old markers");
        MarkerUtil.removeMarkers(element.getJavaProject().getResource());
        pm.worked(1);

        pm.setTaskName("Running checker");
        List<JavacError> callJavac = runChecker(element, checkerNames);
        pm.worked(6);

        pm.setTaskName("Updating problem list");
        markErrors(element.getJavaProject(), callJavac);
        pm.worked(3);

        pm.done();
    }

    private List<JavacError> runChecker(IJavaElement element,
            List<String> checkerNames) throws CoreException, JavaModelException
    {
        List<String> javaFileNames = ResourceUtils.sourceFilesOf(element);
        String cp = classPathOf(element.getJavaProject());

        // XXX it is very annoying that we run commandline javac rather than
        // directly. But otherwise there's a classpath hell.
        List<JavacError> callJavac = new CommandlineJavacRunner().callJavac(
                javaFileNames, checkerNames, cp);
        return callJavac;
    }

    /**
     * Mark errors for this project in the appropriate files
     * 
     * @param project
     * @param errors
     */
    private void markErrors(IJavaProject project, List<JavacError> errors)
    {
        for (JavacError error : errors)
        {
            IResource file = ResourceUtils.getFile(project, error.file);
            if (file == null)
                continue;
            MarkerUtil.addMarker(error.message, project.getProject(), file,
                    error.lineNumber);
        }
    }

    private String pathOf(IClasspathEntry cp, IJavaProject project)
            throws JavaModelException
    {
        int entryKind = cp.getEntryKind();
        switch (entryKind)
        {
        case IClasspathEntry.CPE_SOURCE:
            return ResourceUtils.outputLocation(cp, project);
        case IClasspathEntry.CPE_LIBRARY:
            return Paths.absolutePathOf(cp);
        case IClasspathEntry.CPE_PROJECT:
            // TODO unimplemented!
            break;
        case IClasspathEntry.CPE_CONTAINER:
            IClasspathContainer c = JavaCore.getClasspathContainer(
                    cp.getPath(), project);
            if (c.getKind() == IClasspathContainer.K_DEFAULT_SYSTEM
                    || c.getKind() == IClasspathContainer.K_SYSTEM)
                break;
            for (IClasspathEntry entry : c.getClasspathEntries())
            {
                if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY)
                {
                    return entry.getPath().makeAbsolute().toFile()
                            .getAbsolutePath();
                }
                else
                {
                    // TODO unimplemented!
                }
            }
            return "";
        case IClasspathEntry.CPE_VARIABLE:
            // TODO unimplemented!
            return "";
        }
        return "";
    }

    /**
     * Returns the project's classpath in a format suitable for javac
     * 
     * @param project
     * @return the project's classpath as a string
     * @throws JavaModelException
     */
    private String classPathOf(IJavaProject project) throws JavaModelException
    {
        ClasspathBuilder classpath = new ClasspathBuilder();

        for (IClasspathEntry cp : project.getRawClasspath())
        {
            String path = pathOf(cp, project);
            classpath.append(path);
        }

        classpath.append(CommandlineJavacRunner.checkersJARlocation());
        return classpath.toString();
    }
}
