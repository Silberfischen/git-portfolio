package at.jojart.neuralnetwork.data.extractor;

import at.jojart.neuralnetwork.NeuralNetwork;

/**
 * An extractor which extracts data and pushes it into the queue with the
 * provideData
 *
 * @author Thorsten Jojart
 */
public interface Extractor {

    /**
     * Provides the data to the given queue This method will run till there is
     * no data available, so it is suggested to run it in a seperate thread
     *
     * @param nn the NeuralNetwork to which will be offered
     */
    public abstract void provideData(NeuralNetwork nn);
}
