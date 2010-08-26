package checkers.eclipse.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import checkers.nullness.quals.Nullable;

public abstract class CheckerHandler extends AbstractHandler
{
    protected @Nullable
    IJavaElement element(ISelection selection)
    {
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (structuredSelection != null && !structuredSelection.isEmpty())
            {
                Object elem = structuredSelection.getFirstElement();
                if (elem instanceof IJavaElement)
                {
                    return (IJavaElement) structuredSelection.getFirstElement();
                }
            }
        }

        return null;
    }
}
