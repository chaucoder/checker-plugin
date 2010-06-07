package checkers.eclipse.core;

import org.eclipse.core.runtime.preferences.*;
import org.eclipse.jface.preference.*;

import checkers.eclipse.*;

public class PreferenceInitializer extends AbstractPreferenceInitializer{

    public PreferenceInitializer(){
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initializeDefaultPreferences(){
        // TODO: using default scope temporarily
        // IEclipsePreferences node = new DefaultScope().getNode(Activator.PLUGIN_ID);
        // node.put(Activator.CHECKER_CLASS_PREFERENCE, Activator.CHECKER_CLASS_ALL);

        IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
        prefs.setDefault(Activator.CHECKER_CLASS_PREFERENCE,
                Activator.CHECKER_CLASS_ALL);
    }

}
