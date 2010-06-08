package checkers.eclipse.actions;

import java.util.*;

import checkers.basic.*;
import checkers.igj.*;
import checkers.interning.*;
import checkers.javari.*;
import checkers.nullness.*;

/**
 * This class manages the current checkers that can be run. Also keeps track of custom checkers that are made by the user.
 * 
 * @author asumu
 * 
 */
public class CheckerActionManager{

    private final Class<?>[] initialClasses = { NullnessChecker.class,
            JavariChecker.class, InterningChecker.class, IGJChecker.class};
    private final List<String> checkerNames;

    private CheckerActionManager(){
        checkerNames = new ArrayList<String>();
        for (Class<?> cls : initialClasses){
            checkerNames.add(cls.getCanonicalName());
        }
    }

    private static class Holder{
        private static final CheckerActionManager INSTANCE = new CheckerActionManager();
    }

    /**
     * get the static instance of the manager
     * 
     * @return the static instance
     */
    public static CheckerActionManager getInstance(){
        return Holder.INSTANCE;
    }

    /**
     * Get the list of checkers currently registered with the manager
     * 
     * @return a list of class names
     */
    public List<String> getCheckerNames(){
        return checkerNames;
    }    

}
