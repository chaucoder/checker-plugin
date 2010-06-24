package checkers.eclipse.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

import checkers.eclipse.natures.CheckerBuildNature;
import checkers.nullness.quals.Nullable;

public class ConvertProjectAction implements IActionDelegate
{

    private @Nullable
    IStructuredSelection selection;

    public ConvertProjectAction()
    {
        this.selection = null;
    }

    @Override
    public void run(IAction action)
    {
        setNature(element());
    }

    private void setNature(IJavaElement element)
    {
        IProject project = element.getJavaProject().getProject();
        IProjectDescription desc;
        try
        {
            desc = project.getDescription();
        }catch (CoreException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
        String[] natures = desc.getNatureIds();

        for (String nature : natures)
        {
            if (CheckerBuildNature.NATURE_ID.equals(nature))
            {
                return;
            }
        }

        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 0, natures.length);
        newNatures[newNatures.length - 1] = CheckerBuildNature.NATURE_ID;

        desc.setNatureIds(newNatures);
        try
        {
            project.setDescription(desc, null);
        }catch (CoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
