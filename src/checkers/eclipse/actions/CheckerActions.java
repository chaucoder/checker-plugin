package checkers.eclipse.actions;

import checkers.basic.*;
import checkers.eclipse.*;
import checkers.igj.*;
import checkers.interning.*;
import checkers.javari.*;
import checkers.nullness.*;

public class CheckerActions{
    // TODO: put these representation details inside of a class
	// once it is working and let the manager take care of it
    public static final String[][] ACTION_LABELS_AND_NAMES = {
            { "All", Activator.CHECKER_CLASS_ALL },
            { "Nullness checker", NullnessChecker.class.getCanonicalName() },
            { "Interning checker", InterningChecker.class.getCanonicalName() },
            { "IGJ checker", IGJChecker.class.getCanonicalName() },
            { "Basic checker", BasicChecker.class.getCanonicalName() },
            { "Javari checker", JavariChecker.class.getCanonicalName() } };

    private CheckerActions(){
        throw new AssertionError("not to be instantiated");
    }

    public static class CurrentAction extends RunCheckerAction{
        public CurrentAction(){
            super();
        }
    }

    public static class NullnessAction extends RunCheckerAction{
        public NullnessAction(){
            super(NullnessChecker.class);
        }
    }

    public static class JavariAction extends RunCheckerAction{
        public JavariAction(){
            super(JavariChecker.class);
        }
    }

    public static class InterningAction extends RunCheckerAction{
        public InterningAction(){
            super(InterningChecker.class);
        }
    }

    public static class IGJAction extends RunCheckerAction{
        public IGJAction(){
            super(IGJChecker.class);
        }
    }

    public static class CustomAction extends RunCheckerAction{
        public CustomAction(){
            super(BasicChecker.class);
        }
    }
}
