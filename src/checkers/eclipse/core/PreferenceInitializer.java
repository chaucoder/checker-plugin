package checkers.eclipse.core;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import checkers.eclipse.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{

    public PreferenceInitializer()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
        prefs.setDefault(Activator.PREF_CHECKER_PREFS_SET, false);
        prefs.setDefault(Activator.PREF_CHECKER_ARGS, "");
    }

}
