package checkers.eclipse.ui;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.*;
import checkers.eclipse.*;
import checkers.eclipse.actions.*;

public class PreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage{

    // TODO: this should get labels from the manager
    @Override
    protected void createFieldEditors(){
        this.addField(new ComboFieldEditor(Activator.CHECKER_CLASS_PREFERENCE,
                "Active checker:", CheckerActions.ACTION_LABELS_AND_NAMES, this
                        .getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench){
        //
    }

    @Override
    protected IPreferenceStore doGetPreferenceStore(){
        return Activator.getDefault().getPreferenceStore();
    }

}