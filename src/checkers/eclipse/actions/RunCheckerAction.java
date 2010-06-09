package checkers.eclipse.actions;

import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import checkers.eclipse.Activator;
import checkers.eclipse.util.MutexSchedulingRule;

/**
 * Superclass of all checker actions.
 */
public abstract class RunCheckerAction implements IObjectActionDelegate
{

    private final String checkerName;
    private boolean usePrefs;

    /** The current selection. */
    protected IStructuredSelection selection;

    /** true if this action is used from editor */
    protected boolean usedInEditor;

    protected RunCheckerAction()
    {
        super();
        this.checkerName = null;
        this.usePrefs = true;
    }

    protected RunCheckerAction(Class<?> checker)
    {
        this(checker.getCanonicalName());
    }

    protected RunCheckerAction(String checkerName)
    {
        super();
        this.checkerName = checkerName;
        this.usePrefs = false;
    }

    /**
     * If constructed with a no-arg constructor, then we get the list of classes
     * to use from the preferences system
     */
    private List<String> getClassNameFromPrefs()
    {
        boolean checkSelection = Activator.getDefault().getPreferenceStore()
                .getBoolean(Activator.PREF_CHECKER_PREFS_SET);

        // if preferences for individual checkers has not been set up
        // yet then by default run them all
        // TODO: the better behaviour would be to warn that none have
        // been selected yet and the preferences should be consulted
        // or to stop running
        if (!checkSelection)
        {
            return CheckerActionManager.getInstance().getClassNames();
        }
        else
        {
            return CheckerActionManager.getInstance().getSelectedNames();
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection newSelection)
    {
        if (!usedInEditor && (newSelection instanceof IStructuredSelection))
        {
            this.selection = (IStructuredSelection) newSelection;
        }
        else
            this.selection = null;
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
        // do nothing
    }

    private IJavaProject project()
    {
        if (selection != null && !selection.isEmpty())
            return (IJavaProject) selection.getFirstElement();
        return null;
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    @Override
    public void run(IAction action)
    {
        IJavaProject project = project();
        if (project != null)
        {
            Job checkerJob;

            // TODO: this should handle the case of multiple checkers
            if (!usePrefs)
            {
                checkerJob = new CheckerWorker(project, checkerName);
            }
            else
            {
                List<String> names = getClassNameFromPrefs();
                checkerJob = new CheckerWorker(project, names);
            }
            checkerJob.setUser(true);
            checkerJob.setPriority(Job.BUILD);
            checkerJob.setRule(new MutexSchedulingRule());
            checkerJob.schedule();
        }
    }
}
