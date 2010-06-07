package checkers.eclipse.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.jobs.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;

import checkers.eclipse.Activator;
import checkers.eclipse.util.*;

/**
 * Superclass of all checker actions.
 */
public abstract class RunCheckerAction implements IObjectActionDelegate {

    private final String checkerName;
    private boolean usePrefs;

    /** The current selection. */
    protected IStructuredSelection selection;

    /** true if this action is used from editor */
    protected boolean usedInEditor;

    protected RunCheckerAction(){
        super();
        this.checkerName = null;
        this.usePrefs = true;
    }

    protected RunCheckerAction(Class<?> checker) {
        this(checker.getCanonicalName());
    }

    protected RunCheckerAction(String checkerName) {
        super();
        this.checkerName = checkerName;
        this.usePrefs = false;
    }

    /**
     * If constructed with a no-arg constructor, then we get the list of classes to use from the preferences system
     */
    private List<String> getClassNameFromPrefs(){
        String checkers = Activator.getDefault().getPreferenceStore()
                .getString(Activator.CHECKER_CLASS_PREFERENCE);

        if (checkers.equals(Activator.CHECKER_CLASS_ALL)){
            return CheckerActionManager.getInstance().getCheckerNames();
        }else{
            List<String> ret = new ArrayList<String>();
            ret.add(checkers);
            return ret;
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection newSelection) {
        if (!usedInEditor && (newSelection instanceof IStructuredSelection)) {
            this.selection = (IStructuredSelection) newSelection;
        } else
            this.selection = null;
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // do nothing
    }

    private IJavaProject project() {
        if (selection != null && !selection.isEmpty())
            return (IJavaProject) selection.getFirstElement();
        return null;
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    @Override
    public void run(IAction action) {
        IJavaProject project = project();
        if (project != null) {
            Job checkerJob;
            
        	// TODO: this should handle the case of multiple checkers
            if (!usePrefs) {
                checkerJob = new CheckerWorker(project, checkerName);
            } else {
                String name = getClassNameFromPrefs().get(0);
                checkerJob = new CheckerWorker(project, name);
            }
            checkerJob.setUser(true);
            checkerJob.setPriority(Job.BUILD);
            checkerJob.setRule(new MutexSchedulingRule());
            checkerJob.schedule();
        }
    }
}
