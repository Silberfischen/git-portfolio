package at.jojart.neuralnetwork;

/**
 *
 * @author Silberfischen
 */
public class NeuralNetworkException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>NeuralNetworkException</code> without detail message.
     */
    public NeuralNetworkException() {
    }

    /**
     * Constructs an instance of
     * <code>NeuralNetworkException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NeuralNetworkException(String msg) {
        super(msg);
    }
}
