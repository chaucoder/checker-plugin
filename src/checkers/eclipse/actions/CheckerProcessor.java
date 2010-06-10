package checkers.eclipse.actions;

/**
 * This represents an individual checker processor and its corresponding name
 * (displayed in the UI) and the corresponding class to be run.
 * 
 * @author asumu
 * 
 */
public class CheckerProcessor
{
    private final String label;
    private final Class<?> processor;

    /**
     * Sets the name and processor accordingly
     * 
     * @param label
     * @param processor
     */
    CheckerProcessor(String label, Class<?> processor)
    {
        this.label = label;
        this.processor = processor;
    }

    /**
     * Gets the canonical class name for running the processor
     * 
     * @return the class name
     */
    String getClassName()
    {
        return this.processor.getCanonicalName();
    }

    /**
     * Get the label for this processor
     * 
     * @return the label name
     */
    String getLabel()
    {
        return this.label;
    }
}
