package checkers.eclipse.javac.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import checkers.eclipse.javac.JavacError;

public class ErrorParserTest
{
    private static final String SIMPLE_TEST_INPUT = new StringBuilder()
            .append("/home/asumu/gsoc-workspace-4/checker testing/src/GetStarted.java:8: warning: incompatible types.\n")
            .append("       @NonNull Integer bar = null;\n")
            .append("                              ^\n")
            .append("  found   : null\n")
            .append("  required: @NonNull Integer\n")
            .append("/home/asumu/gsoc-workspace-4/checker testing/src/GetStarted.java:16: warning: attempting to use a non-@Interned comparison operand\n")
            .append("       else if (s1 == obj)\n")
            .append("                      ^\n").append("  found: Object\n")
            .append("2 warnings").toString();
    private static final String SIMPLE_ERROR_1 = new StringBuilder()
            .append("incompatible types.\n")
            .append("       @NonNull Integer bar = null;\n")
            .append("  found   : null\n")
            .append("  required: @NonNull Integer").toString();
    private static final String SIMPLE_ERROR_2 = new StringBuilder()
            .append("attempting to use a non-@Interned comparison operand\n")
            .append("       else if (s1 == obj)\n").append("  found: Object")
            .toString();
    private static final String OTHER_TEST_INPUT = new StringBuilder()
            .append("/homes/gws/wmdietl/research/eclipse-workspaces/2010-08-icse/SwingEval/AwtSwing/java/awt/Window.java:58: warning: Disposer is internal proprietary API and may be removed in a future release\n")
            .append("import sun.java2d.Disposer;\n")
            .append("                 ^\n")
            .append("/homes/gws/wmdietl/research/eclipse-workspaces/2010-08-icse/SwingEval/AwtSwing/java/awt/Window.java:59: warning: Region is internal proprietary API and may be removed in a future release\n")
            .append("import sun.java2d.pipe.Region;\n")
            .append("                      ^\n").append("2 warnings")
            .toString();
    private static final String OTHER_ERROR_1 = new StringBuilder()
            .append("Disposer is internal proprietary API and may be removed in a future release\n")
            .append("import sun.java2d.Disposer;").toString();
    private static final String OTHER_ERROR_2 = new StringBuilder()
            .append("Region is internal proprietary API and may be removed in a future release\n")
            .append("import sun.java2d.pipe.Region;").toString();
    private static final String W_TEST_INPUT = new StringBuilder()
            .append("/foo/bar/Baz.java:35: warning: AttributesValues is internal proprietary API and may be removed in a future release\n")
            .append("  private static void applyStyle(int style, AttributeValues values) {\n")
            .append("/foo/bar/Baz.java:38: package sun.java2d.cmm does not exist\n")
            .append("import sun.java2d.cmm.ColorTransform\n")
            .append("/foo/bar/Baz.java:39: package sun.java2d.cmm does not exist\n")
            .append("import sun.java2d.cmm.CMSManager\n").append("3 warnings")
            .toString();
    private static final String W_ERROR_2 = new StringBuilder()
            .append("package sun.java2d.cmm does not exist\n")
            .append("import sun.java2d.cmm.ColorTransform").toString();

    @Test
    public void simpleParseTest()
    {
        List<JavacError> errors = JavacError.parse(SIMPLE_TEST_INPUT);

        assertEquals(errors.size(), 2);
        assertEquals(errors.get(0).lineNumber, 8);
        assertEquals(errors.get(0).message, SIMPLE_ERROR_1);
        assertEquals(errors.get(1).lineNumber, 16);
        assertEquals(errors.get(1).message, SIMPLE_ERROR_2);
    }

    @Test
    public void otherParseTest()
    {
        List<JavacError> errors = JavacError.parse(OTHER_TEST_INPUT);

        assertEquals(errors.size(), 2);
        assertEquals(errors.get(0).message, OTHER_ERROR_1);
        assertEquals(errors.get(0).lineNumber, 58);
        assertEquals(errors.get(1).message, OTHER_ERROR_2);
        assertEquals(errors.get(1).lineNumber, 59);
    }

    @Test
    public void wParseTest()
    {
        List<JavacError> errors = JavacError.parse(W_TEST_INPUT);

        assertEquals(errors.size(), 3);
        assertEquals(errors.get(1).message, W_ERROR_2);
    }
}