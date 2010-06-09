package checkers.eclipse.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import checkers.eclipse.Activator;
import checkers.eclipse.actions.CheckerActionManager;

public class CheckerPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage
{
    private Table table;

    @Override
    public void init(IWorkbench workbench)
    {
        //
    }

    @Override
    protected IPreferenceStore doGetPreferenceStore()
    {
        return Activator.getDefault().getPreferenceStore();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        Composite tableComposite = new Composite(parent, SWT.None);
        GridLayout layout = new GridLayout();
        tableComposite.setLayout(layout);

        Label lbl = new Label(tableComposite, SWT.LEFT);
        lbl.setText("Checkers:");
        GridData data = new GridData();
        data.verticalAlignment = SWT.BEGINNING;
        lbl.setLayoutData(data);

        table = new Table(tableComposite, SWT.CHECK | SWT.MULTI | SWT.BORDER);
        GridData data2 = new GridData(SWT.FILL, SWT.FILL, true, true);
        table.setLayoutData(data2);

        for (String label : CheckerActionManager.getInstance()
                .getCheckerLabels())
        {
            TableItem item = new TableItem(table, SWT.None);
            item.setText(label);
        }

        return tableComposite;
    }

}