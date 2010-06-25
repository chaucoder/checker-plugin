package checkers.eclipse.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import checkers.eclipse.Activator;

/**
 * Creates a JSR308 marker in a runnable window.
 */
public class MarkerReporter implements IWorkspaceRunnable
{
    public static final String NAME = Activator.PLUGIN_ID + ".marker";

    private final IResource resource;
    private final int startLine;
    private final String message;
    private final int startChar;
    private final int endChar;

    public MarkerReporter(IResource resource, int startLine, String message,
            int startChar, int endChar)
    {
        this.startLine = startLine;
        this.resource = resource;
        this.message = message;
        this.startChar = startChar;
        this.endChar = endChar;
    }

    @Override
    public void run(IProgressMonitor monitor) throws CoreException
    {

        if (Activator.DEBUG)
        {
            System.out.println("Creating marker for " + resource.getLocation());
        }

        IMarker marker = resource.createMarker(NAME);
        if (Activator.DEBUG)
        {
            System.out.println("Setting attibutes for marker in "
                    + resource.getLocation());
        }

        marker.setAttribute(IMarker.LINE_NUMBER, startLine);
        marker.setAttribute(IMarker.MESSAGE, message);
        marker.setAttribute(IMarker.CHAR_START, startChar);
        marker.setAttribute(IMarker.CHAR_END, endChar);
        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
    }
}
