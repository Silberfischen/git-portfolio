package logic.document;

/**
 *
 * @author Thorsten Jojart
 */
public class DocumentWriterException extends Exception {

    /**
     * Creates a new instance of
     * <code>DocumentWriterException</code> without detail message.
     */
    public DocumentWriterException() {
    }

    /**
     * Constructs an instance of
     * <code>DocumentWriterException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DocumentWriterException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>DocumentWriterException</code> with the specified Exception.
     *
     * @param t the exception you want to throw further.
     */
    public DocumentWriterException(Throwable t) {
        super(t.getMessage());
        t.printStackTrace();

    }
}
