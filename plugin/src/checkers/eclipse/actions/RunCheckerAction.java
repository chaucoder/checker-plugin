package checkers.eclipse.actions;

import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import checkers.eclipse.CheckerPlugin;
import checkers.eclipse.prefs.CheckerPreferences;
import checkers.eclipse.util.JavaUtils;
import checkers.eclipse.util.MutexSchedulingRule;

/**
 * Superclass of all checker actions.
 */
public abstract class RunCheckerAction implements IObjectActionDelegate
{
    private final String checkerName;
    protected boolean usePrefs;
    protected boolean useCustom;

    /** The current selection. */
    protected IStructuredSelection selection;

    /** true if this action is used from editor */
    protected boolean usedInEditor;

    protected RunCheckerAction()
    {
        super();
        this.checkerName = null;
        this.usePrefs = true;
        this.useCustom = false;
    }

    protected RunCheckerAction(Class<?> checker)
    {
        this(checker.getCanonicalName());
    }

    protected RunCheckerAction(String checkerName)
    {
        super();
        this.checkerName = checkerName;
        this.useCustom = false;
        this.usePrefs = false;
    }

    /**
     * If constructed with a no-arg constructor, then we get the list of classes
     * to use from the preferences system
     */
    private List<String> getClassNameFromPrefs()
    {

        return CheckerActionManager.getInstance().getSelectedNames();
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

    private IJavaElement element()
    {
        if (selection != null && !selection.isEmpty())
        {
            return (IJavaElement) selection.getFirstElement();
        }

        return null;
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    @Override
    public void run(IAction action)
    {
        IJavaElement element = element();

        if (element != null)
        {
            Job checkerJob;
            String customClasses = CheckerPlugin.getDefault()
                    .getPreferenceStore()
                    .getString(CheckerPreferences.PREF_CHECKER_CUSTOM_CLASSES);

            // Depending on how this runner was created, we will either:
            // * just run one particular checker
            // * use the custom configured checkers
            // * run "selected" checkers using the action or auto build
            if (!usePrefs && !useCustom)
            {
                checkerJob = new CheckerWorker(element, checkerName);
            }
            else if (!usePrefs)
            {
                checkerJob = new CheckerWorker(element, customClasses);
            }
            else
            {

                List<String> names = getClassNameFromPrefs();

                checkerJob = new CheckerWorker(element, JavaUtils.join(",",
                        names));
            }

            checkerJob.setUser(true);
            checkerJob.setPriority(Job.BUILD);
            checkerJob.setRule(new MutexSchedulingRule());
            checkerJob.schedule();
        }
    }
}
