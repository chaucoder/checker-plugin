package checkers.eclipse.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import checkers.eclipse.Activator;
import checkers.eclipse.actions.CheckerActionManager;

public class CheckerPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage
{
    private Table procTable;
    private Text argText;

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

        procTable = new Table(tableComposite, SWT.CHECK | SWT.MULTI
                | SWT.BORDER);
        GridData data2 = new GridData(SWT.FILL, SWT.FILL, true, true);
        procTable.setLayoutData(data2);

        for (String label : CheckerActionManager.getInstance()
                .getCheckerLabels())
        {
            TableItem item = new TableItem(procTable, SWT.None);
            item.setText(label);
        }

        Group group = new Group(tableComposite, SWT.None);
        group.setText("Javac Arguments");
        FillLayout fill = new FillLayout();
        fill.marginWidth = fill.marginHeight = 5;
        group.setLayout(fill);

        // TODO: it would be nice if this had a label warning the
        // user about overriding checker arguments
        argText = new Text(group, SWT.SINGLE | SWT.BORDER);
        argText.setTextLimit(Text.LIMIT);

        GridData data3 = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        group.setLayoutData(data3);

        initValues();

        return tableComposite;
    }

    /**
     * Initialize the values in the table to the preference values
     */
    private void initValues()
    {
        IPreferenceStore store = doGetPreferenceStore();
        List<TableItem> selected = new ArrayList<TableItem>();

        for (TableItem item : procTable.getItems())
        {
            if (store.getBoolean(item.getText()))
            {
                selected.add(item);
                item.setChecked(true);
            }
        }

        argText.setText(store.getString(Activator.PREF_CHECKER_ARGS));
    }

    public boolean performOk()
    {
        IPreferenceStore store = doGetPreferenceStore();

        for (TableItem item : procTable.getItems())
        {
            // TODO: make sure uninitialized or removed checkers
            // won't screw this up
            store.setValue(item.getText(), item.getChecked());
        }

        store.setValue(Activator.PREF_CHECKER_PREFS_SET, true);
        store.setValue(Activator.PREF_CHECKER_ARGS, argText.getText());

        return true;
    }
}