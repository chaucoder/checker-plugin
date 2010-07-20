package checkers.eclipse.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

import checkers.eclipse.CheckerPlugin;
import checkers.eclipse.natures.CheckerBuildNature;

public class ConvertProjectAction implements IActionDelegate
{

    private IStructuredSelection selection;

    public ConvertProjectAction()
    {
        this.selection = null;
    }

    @Override
    public void run(IAction action)
    {
        try
        {
            IProject project = element().getJavaProject().getProject();
            IProjectDescription desc = project.getDescription();
            String[] natures = desc.getNatureIds();

            if (!hasNature(natures))
                setNature(project, desc, natures);
            else
                removeNature(project, desc, natures);
        }catch (CoreException e)
        {
            CheckerPlugin.logException(e, e.getMessage());
        }
    }

    private boolean hasNature(String[] natures)
    {
        for (String nature : natures)
        {
            if (CheckerBuildNature.NATURE_ID.equals(nature))
            {
                return true;
            }
        }

        return false;
    }

    private void removeNature(IProject project, IProjectDescription desc,
            String[] natures) throws CoreException
    {
        int skipIndex = 0;
        String[] newNatures = new String[natures.length - 1];

        for (int i = 0; i < natures.length; i++)
        {
            if (CheckerBuildNature.NATURE_ID.equals(natures[i]))
            {
                skipIndex = i;
            }
        }

        System.arraycopy(natures, 0, newNatures, 0, skipIndex);
        System.arraycopy(natures, skipIndex + 1, newNatures, skipIndex,
                newNatures.length - skipIndex);

        desc.setNatureIds(newNatures);
        project.setDescription(desc, null);
    }

    private void setNature(IProject project, IProjectDescription desc,
            String[] natures) throws CoreException
    {

        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 0, natures.length);
        newNatures[newNatures.length - 1] = CheckerBuildNature.NATURE_ID;

        desc.setNatureIds(newNatures);

        project.setDescription(desc, null);
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection)
    {
        if (!selection.isEmpty() && selection instanceof IStructuredSelection)
            this.selection = (IStructuredSelection) selection;
        else
            this.selection = null;
    }

    private IJavaElement element()
    {
        Object element = selection.getFirstElement();

        if (selection != null && element instanceof IJavaElement)
        {
            return (IJavaElement) element;
        }

        return null;
    }

}
