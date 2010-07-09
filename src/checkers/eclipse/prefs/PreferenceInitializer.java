package checkers.eclipse.prefs;

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
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_PREFS_SET, false);
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_ARGS, "");
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_A_SKIP_CLASSES, "");
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_A_LINT, "");
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_A_WARNS, true);
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_A_FILENAMES, false);
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_A_NO_MSG_TEXT, false);
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_A_SHOW_CHECKS, false);
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_AUTO_BUILD, true);
        prefs.setDefault(CheckerPreferences.PREF_CHECKER_IMPLICIT_IMPORTS,
                false);
    }

}
