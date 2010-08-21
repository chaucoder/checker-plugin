package checkers.eclipse.actions;

import checkers.fenum.FenumChecker;
import checkers.i18n.I18nChecker;
import checkers.igj.IGJChecker;
import checkers.interning.InterningChecker;
import checkers.javari.JavariChecker;
import checkers.linear.LinearChecker;
import checkers.lock.LockChecker;
import checkers.nullness.NullnessChecker;
import checkers.regex.RegexChecker;
import checkers.tainting.TaintingChecker;

public class CheckerActions
{
    // TODO: this could maybe be built at runtime via reflection since
    // the built-in checker classes may change in the future
    public static final String NULLNESS_ACTION_LABEL = "Nullness checker";
    public static final Class<?> NULLNESS_ACTION_CLASS = NullnessChecker.class;
    public static final String JAVARI_ACTION_LABEL = "Javari checker";
    public static final Class<?> JAVARI_ACTION_CLASS = JavariChecker.class;
    public static final String INTERNING_ACTION_LABEL = "Interning checker";
    public static final Class<?> INTERNING_ACTION_CLASS = InterningChecker.class;
    public static final String IGJ_ACTION_LABEL = "IGJ checker";
    public static final Class<?> IGJ_ACTION_CLASS = IGJChecker.class;
    public static final String FENUM_ACTION_LABEL = "Fenum checker";
    public static final Class<?> FENUM_ACTION_CLASS = FenumChecker.class;
    public static final Class<?> LINEAR_ACTION_CLASS = LinearChecker.class;
    public static final String LINEAR_ACTION_LABEL = "Linear checker";
    public static final Class<?> LOCK_ACTION_CLASS = LockChecker.class;
    public static final String LOCK_ACTION_LABEL = "Lock checker";
    public static final Class<?> REGEX_ACTION_CLASS = RegexChecker.class;
    public static final String REGEX_ACTION_LABEL = "Regex checker";
    public static final Class<?> TAINTING_ACTION_CLASS = TaintingChecker.class;
    public static final String TAINTING_ACTION_LABEL = "Tainting checker";
    public static final Class<?> I18N_ACTION_CLASS = I18nChecker.class;
    public static final String I18N_ACTION_LABEL = "I18n checker";

    private CheckerActions()
    {
        throw new AssertionError("not to be instantiated");
    }

    public static class CurrentAction extends RunCheckerAction
    {
        public CurrentAction()
        {
            super();
        }
    }

    public static class NullnessAction extends RunCheckerAction
    {
        public NullnessAction()
        {
            super(NULLNESS_ACTION_CLASS);
        }
    }

    public static class JavariAction extends RunCheckerAction
    {
        public JavariAction()
        {
            super(JAVARI_ACTION_CLASS);
        }
    }

    public static class InterningAction extends RunCheckerAction
    {
        public InterningAction()
        {
            super(INTERNING_ACTION_CLASS);
        }
    }

    public static class IGJAction extends RunCheckerAction
    {
        public IGJAction()
        {
            super(IGJ_ACTION_CLASS);
        }
    }

    public static class FenumAction extends RunCheckerAction
    {
        public FenumAction()
        {
            super(FENUM_ACTION_CLASS);
        }
    }

    public static class LinearAction extends RunCheckerAction
    {
        public LinearAction()
        {
            super(LINEAR_ACTION_CLASS);
        }
    }

    public static class LockAction extends RunCheckerAction
    {
        public LockAction()
        {
            super(LOCK_ACTION_CLASS);
        }
    }

    public static class TaintingAction extends RunCheckerAction
    {
        public TaintingAction()
        {
            super(TAINTING_ACTION_CLASS);
        }
    }

    public static class I18nAction extends RunCheckerAction
    {
        public I18nAction()
        {
            super(I18N_ACTION_CLASS);
        }
    }

    public static class RegexAction extends RunCheckerAction
    {
        public RegexAction()
        {
            super(REGEX_ACTION_CLASS);
        }
    }

    public static class CustomAction extends RunCheckerAction
    {
        public CustomAction()
        {
            useCustom = true;
            usePrefs = false;
        }
    }
}
