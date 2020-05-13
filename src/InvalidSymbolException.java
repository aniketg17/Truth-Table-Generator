/**
 * <h1>Custom Exception Handling Class</h1>
 * This is a custom exception handling class that is used in
 * other classes for invalid expression entries.
 * <p>
 *
 * @author  Aniket Kumar Gupta
 */

public class InvalidSymbolException extends Exception {

    /**
     * This constructor initializes superclass's message field
     * @param message This is the message parameter
     */

    public InvalidSymbolException(String message) {
        super(message);
    }
}
