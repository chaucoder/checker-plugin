package checkers.eclipse.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import checkers.eclipse.prefs.CheckerPreferences;

public class CheckerPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage
{
    private Table procTable;
    private Text argText;
    private Text optSkipClasses;
    private Text optALint;
    private Button optAutoBuild;
    private Button optWarning;
    private Button optFilenames;
    private Button optNoMsgText;
    private Button optShowChecks;
    private Button optImplicitImports;

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
        // Layout for pref page
        Composite tableComposite = new Composite(parent, SWT.None);
        GridLayout layout = new GridLayout();
        tableComposite.setLayout(layout);

        // Option table for checker processors
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

        // UI/Eclipse options
        Group uiGroup = new Group(tableComposite, SWT.None);
        uiGroup.setText("Eclipse options");
        FillLayout uiLayout = new FillLayout(SWT.VERTICAL);
        uiLayout.marginWidth = uiLayout.marginHeight = 5;
        uiGroup.setLayout(uiLayout);

        optAutoBuild = new Button(uiGroup, SWT.CHECK);
        optAutoBuild.setText("Automatically process annotations");

        GridData uiGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        uiGroup.setLayoutData(uiGridData);

        // Processor options
        Group procGroup = new Group(tableComposite, SWT.None);
        procGroup.setText("Processor/build options");
        FillLayout procLayout = new FillLayout(SWT.VERTICAL);
        procLayout.marginWidth = procLayout.marginHeight = 5;
        procGroup.setLayout(procLayout);

        Label skipLabel = new Label(procGroup, SWT.None);
        skipLabel.setText("Classes to skip:");
        optSkipClasses = new Text(procGroup, SWT.SINGLE | SWT.BORDER);
        optSkipClasses
                .setToolTipText("Classes to skip during type checking (-AskipClasses)");
        Label lintLabel = new Label(procGroup, SWT.None);
        lintLabel.setText("Lint options:");
        optALint = new Text(procGroup, SWT.SINGLE | SWT.BORDER);
        optALint.setToolTipText("Enable or disable optional checks (-Alint)");
        optWarning = new Button(procGroup, SWT.CHECK);
        optWarning.setText("Show errors as warnings (-Awarns)");
        optFilenames = new Button(procGroup, SWT.CHECK);
        optFilenames.setText("Print the name of each file (-Afilenames)");
        optNoMsgText = new Button(procGroup, SWT.CHECK);
        optNoMsgText.setText("Use message keys instead of text (-Anomsgtext)");
        optShowChecks = new Button(procGroup, SWT.CHECK);
        optShowChecks
                .setText("Print debugging info for pseudo-checks (-Ashowchecks)");
        optImplicitImports = new Button(procGroup, SWT.CHECK);
        optImplicitImports
                .setText("Use implicit imports for annotation classes");

        GridData procGridData = new GridData(SWT.FILL, SWT.BEGINNING, true,
                false);
        procGroup.setLayoutData(procGridData);

        // Additional arguments to javac
        Group javacGroup = new Group(tableComposite, SWT.None);
        javacGroup.setText("Additional compiler parameters");
        FillLayout javacLayout = new FillLayout();
        javacLayout.marginWidth = javacLayout.marginHeight = 5;
        javacGroup.setLayout(javacLayout);

        argText = new Text(javacGroup, SWT.SINGLE | SWT.BORDER);
        argText.setTextLimit(Text.LIMIT);

        GridData javacGridData = new GridData(SWT.FILL, SWT.BEGINNING, true,
                false);
        javacGroup.setLayoutData(javacGridData);

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

        argText.setText(store.getString(CheckerPreferences.PREF_CHECKER_ARGS));
        optAutoBuild.setSelection(store
                .getBoolean(CheckerPreferences.PREF_CHECKER_AUTO_BUILD));
        optSkipClasses.setText(store
                .getString(CheckerPreferences.PREF_CHECKER_A_SKIP_CLASSES));
        optALint.setText(store
                .getString(CheckerPreferences.PREF_CHECKER_A_LINT));
        optWarning.setSelection(store
                .getBoolean(CheckerPreferences.PREF_CHECKER_A_WARNS));
        optFilenames.setSelection(store
                .getBoolean(CheckerPreferences.PREF_CHECKER_A_FILENAMES));
        optNoMsgText.setSelection(store
                .getBoolean(CheckerPreferences.PREF_CHECKER_A_NO_MSG_TEXT));
        optShowChecks.setSelection(store
                .getBoolean(CheckerPreferences.PREF_CHECKER_A_SHOW_CHECKS));
        optImplicitImports.setSelection(store
                .getBoolean(CheckerPreferences.PREF_CHECKER_IMPLICIT_IMPORTS));
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

        store.setValue(CheckerPreferences.PREF_CHECKER_PREFS_SET, true);
        store.setValue(CheckerPreferences.PREF_CHECKER_ARGS, argText.getText());
        store.setValue(CheckerPreferences.PREF_CHECKER_AUTO_BUILD,
                optAutoBuild.getSelection());
        store.setValue(CheckerPreferences.PREF_CHECKER_A_SKIP_CLASSES,
                optSkipClasses.getText());
        store.setValue(CheckerPreferences.PREF_CHECKER_A_LINT,
                optALint.getText());
        store.setValue(CheckerPreferences.PREF_CHECKER_A_WARNS,
                optWarning.getSelection());
        store.setValue(CheckerPreferences.PREF_CHECKER_A_FILENAMES,
                optFilenames.getSelection());
        store.setValue(CheckerPreferences.PREF_CHECKER_A_NO_MSG_TEXT,
                optNoMsgText.getSelection());
        store.setValue(CheckerPreferences.PREF_CHECKER_A_SHOW_CHECKS,
                optShowChecks.getSelection());
        store.setValue(CheckerPreferences.PREF_CHECKER_IMPLICIT_IMPORTS,
                optImplicitImports.getSelection());

        return true;
    }
}