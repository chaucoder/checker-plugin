package checkers.eclipse.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import checkers.eclipse.CheckerPlugin;

/**
 * This class manages the current checkers that can be run. Also keeps track of
 * custom checkers that are made by the user.
 * 
 * @author asumu
 * 
 */
public class CheckerActionManager
{
    private List<CheckerProcessor> processors;

    /**
     * Singleton constructor, should only be called once for the instance
     */
    private CheckerActionManager()
    {
        // add built-in checkers
        processors = new ArrayList<CheckerProcessor>();
        processors.add(new CheckerProcessor(
                CheckerActions.NULLNESS_ACTION_LABEL,
                CheckerActions.NULLNESS_ACTION_CLASS));
        processors.add(new CheckerProcessor(CheckerActions.JAVARI_ACTION_LABEL,
                CheckerActions.JAVARI_ACTION_CLASS));
        processors.add(new CheckerProcessor(
                CheckerActions.INTERNING_ACTION_LABEL,
                CheckerActions.INTERNING_ACTION_CLASS));
        processors.add(new CheckerProcessor(CheckerActions.IGJ_ACTION_LABEL,
                CheckerActions.IGJ_ACTION_CLASS));
    }

    private static class Holder
    {
        private static final CheckerActionManager INSTANCE = new CheckerActionManager();
    }

    /**
     * get the static instance of the manager
     * 
     * @return the static instance
     */
    public static CheckerActionManager getInstance()
    {
        return Holder.INSTANCE;
    }

    /**
     * Get the list of checkers currently registered with the manager
     * 
     * @return a list of labels for processors
     */
    public List<String> getCheckerLabels()
    {
        ArrayList<String> results = new ArrayList<String>();
        for (CheckerProcessor processor : processors)
        {
            results.add(processor.getLabel());
        }

        return results;
    }

    /**
     * Get the list of checker classes to call from the compiler
     * 
     * @return list of class names
     */
    public List<String> getClassNames()
    {
        ArrayList<String> results = new ArrayList<String>();
        for (CheckerProcessor processor : processors)
        {
            results.add(processor.getClassName());
        }

        return results;
    }

    /**
     * Check to see which classes have been selected and return those that have
     * been selected
     * 
     * @return a list of classes to run
     */
    public List<String> getSelectedNames()
    {
        // TODO: should check the precondition that the
        // PREF_CHECK_PREFS is set to true, otherwise these
        // preferences may be uninitialized (might be harmless
        // since that defaults to false)
        List<String> selected = new ArrayList<String>();

        IPreferenceStore store = CheckerPlugin.getDefault().getPreferenceStore();

        for (CheckerProcessor processor : processors)
        {
            String label = processor.getLabel();
            boolean selection = store.getBoolean(label);
            if (selection)
                selected.add(processor.getClassName());
        }

        return selected;
    }
}
