package checkers.eclipse.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import checkers.eclipse.CheckerPlugin;

/**
 * Creates a JSR308 marker in a runnable window.
 */
public class MarkerReporter implements IWorkspaceRunnable
{
    public static final String NAME = CheckerPlugin.PLUGIN_ID + ".marker";

    /*
     * private final IResource resource; private final Diagnostic<? extends
     * JavaFileObject> diag;
     */

    private final IResource resource;
    private final int startLine;
    private final String message;

    public MarkerReporter(IResource resource, int startLine, String message)
    {
        this.startLine = startLine;
        this.resource = resource;
        this.message = message;
    }

    /*
     * public MarkerReporter(IResource resource, Diagnostic<? extends
     * JavaFileObject> diag) { this.resource = resource; this.diag = diag; }
     */

    @Override
    public void run(IProgressMonitor monitor) throws CoreException
    {

        if (CheckerPlugin.DEBUG)
        {
            System.out.println("Creating marker for " + resource.getLocation());
        }

        IMarker marker = resource.createMarker(NAME);
        if (CheckerPlugin.DEBUG)
        {
            System.out.println("Setting attibutes for marker in "
                    + resource.getLocation());
        }

        marker.setAttribute(IMarker.LINE_NUMBER, startLine);
        marker.setAttribute(IMarker.MESSAGE, message);
        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
    }

    /*
     * @Override public void run(IProgressMonitor monitor) throws CoreException
     * {
     * 
     * if (Activator.DEBUG) { System.out.println("Creating marker for " +
     * resource.getLocation()); }
     * 
     * IMarker marker = resource.createMarker(NAME); if (Activator.DEBUG) {
     * System.out.println("Setting attibutes for marker in " +
     * resource.getLocation()); }
     * 
     * marker.setAttribute(IMarker.LINE_NUMBER, (int) diag.getLineNumber());
     * marker.setAttribute(IMarker.MESSAGE, diag.getMessage(null));
     * 
     * if (diag.getPosition() != Diagnostic.NOPOS) {
     * marker.setAttribute(IMarker.CHAR_START, (int) diag.getStartPosition());
     * marker.setAttribute(IMarker.CHAR_END, (int) diag.getEndPosition()); }
     * 
     * if (diag.getKind().equals(Diagnostic.Kind.ERROR)) {
     * marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR); } else if
     * (diag.getKind().equals(Diagnostic.Kind.WARNING) ||
     * diag.getKind().equals(Diagnostic.Kind.MANDATORY_WARNING)) {
     * marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING); } else {
     * marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO); } }
     */
}
