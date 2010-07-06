package checkers.eclipse.util;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import checkers.eclipse.Activator;
import checkers.eclipse.marker.MarkerReporter;

/**
 * Utility methods for Eclipse markers.
 */
public final class MarkerUtil
{

    private MarkerUtil()
    {
        throw new AssertionError("Shouldn't be initialized");
    }

    /**
     * Remove all FindBugs problem markers for given resource.
     */
    public static void removeMarkers(IResource res) throws CoreException
    {
        // remove any markers added by our builder
        // This triggers resource update on IResourceChangeListener's
        // (BugTreeView)
        if (Activator.DEBUG)
        {
            System.out.println("Removing JSR 308 markers in "
                    + res.getLocation());
        }
        res.deleteMarkers(MarkerReporter.NAME, true, IResource.DEPTH_INFINITE);
    }

    public static void addMarker(Diagnostic<? extends JavaFileObject> diag,
            IProject project, IResource resource)
    {
        if (Activator.DEBUG)
        {
            System.out.println("Creating marker for " + resource.getLocation()
                    + ": line " + diag.getLineNumber() + " "
                    + diag.getMessage(null));
        }

        try
        {
            project.getWorkspace().run(new MarkerReporter(resource, diag),
                    null, 0, null);
        }catch (CoreException e)
        {
            Activator.logException(e, "Core exception on add marker");
        }
    }

}
