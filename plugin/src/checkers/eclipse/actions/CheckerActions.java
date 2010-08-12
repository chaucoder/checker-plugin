package checkers.eclipse.actions;

import checkers.basic.BasicChecker;
import checkers.fenum.FenumChecker;
import checkers.igj.IGJChecker;
import checkers.interning.InterningChecker;
import checkers.javari.JavariChecker;
import checkers.nullness.NullnessChecker;

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

    public static class CustomAction extends RunCheckerAction
    {
        public CustomAction()
        {
            super(BasicChecker.class);
        }
    }
}
