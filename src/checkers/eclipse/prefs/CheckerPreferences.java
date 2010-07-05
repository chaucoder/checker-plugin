package checkers.eclipse.prefs;

/**
 * This class keeps static information about preferences for the checker plugin
 * @author asumu
 *
 */
public final class CheckerPreferences {

	/** A key for determining if individual class prefs should be checked */
	public static final String PREF_CHECKER_PREFS_SET = "checker_prefs";
	
	/** A key for additional arguments to pass the checker */
	public static final String PREF_CHECKER_ARGS = "checker_args";
	
	/** Key for classes to skip in processing */
	public static final String PREF_CHECKER_A_SKIP_CLASSES = "checker_a_skip_classes";
	
	/** Key for -Alint options */
	public static final String PREF_CHECKER_A_LINT = "checker_a_lint";
	
	/** Key for -Awarns */
	public static final String PREF_CHECKER_A_WARNS = "checker_a_warns";
	
	/** Key for -Afilenames */
	public static final String PREF_CHECKER_A_FILENAMES = "checker_a_filenames";
	
	/** Key for -Anomsgtext */
	public static final String PREF_CHECKER_A_NO_MSG_TEXT = "checker_no_msg_text";
	
	/** Key for -Ashowchecks */
	public static final String PREF_CHECKER_A_SHOW_CHECKS = "checker_show_checks";
	
}
