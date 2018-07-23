package at.jojart.neuralnetwork.message;

import java.io.Serializable;

/**
 *
 * @author Thorsten Jojart
 */
public class NeuralNetworkConfig implements Serializable {

    public static final int CONNECT_DEFAULT = 0;
    //message variable definitions
    private final int inputFeatureCount;
    private final int[] hiddenNeuronCounts;
    private final int outputLayerCount;
    private final int connectionType;
    private final int activationFunction;

    public NeuralNetworkConfig(int inputFeatureCount, int outputLayerCount, int connectionType, int activationFunction, int... hiddenNeuronCounts) {
        this.inputFeatureCount = inputFeatureCount;
        this.outputLayerCount = outputLayerCount;
        this.connectionType = connectionType;
        this.hiddenNeuronCounts = hiddenNeuronCounts;
        this.activationFunction = activationFunction;
    }

    public int getConnectionType() {
        return connectionType;
    }

    public int getActivationFunction() {
        return activationFunction;
    }

    public int getOutputLayerCount() {
        return outputLayerCount;
    }

    public int getInputFeatureCount() {
        return inputFeatureCount;
    }

    public int[] getHiddenNeuronCounts() {
        return hiddenNeuronCounts;
    }
}
