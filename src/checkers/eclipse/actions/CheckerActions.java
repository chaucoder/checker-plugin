package checkers.eclipse.actions;

import checkers.basic.*;
import checkers.eclipse.*;
import checkers.igj.*;
import checkers.interning.*;
import checkers.javari.*;
import checkers.nullness.*;

public class CheckerActions{
    // TODO: this representation may need to change if custom
    // checkers are supported
    public static final String[][] ACTION_LABELS_AND_NAMES = {
            { "All", Activator.CHECKER_CLASS_ALL },
            { "Nullness checker", NullnessChecker.class.toString() },
            { "Interning checker", InterningChecker.class.toString() },
            { "IGJ checker", IGJChecker.class.toString() },
            { "Basic checker", BasicChecker.class.toString() },
            { "Javari checker", JavariChecker.class.toString() } };
    public static final String[] ACTION_CLASS_NAMES = {
            Activator.CHECKER_CLASS_ALL, NullnessChecker.class.toString(),
            JavariChecker.class.toString(), InterningChecker.class.toString(),
            IGJChecker.class.toString(), BasicChecker.class.toString() };

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
