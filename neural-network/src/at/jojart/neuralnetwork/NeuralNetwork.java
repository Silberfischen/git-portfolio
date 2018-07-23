package at.jojart.neuralnetwork;

import at.jojart.activation_functions.ActivationFunction;
import at.jojart.neuralnetwork.message.NeuralNetworkConfig;
import at.jojart.neuralnetwork.message.Sample;
import gnu.trove.list.array.TDoubleArrayList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ThorstenJojart
 */
public final class NeuralNetwork {

    /**
     * The learning factor which controlls the learning <br />NOTE: this factor
     * should be negative and for simplicity the minus is added here!
     */
    private double LEARNING_FACTOR = 0.3;
    private final double INIT_WEIGHTS_FACTOR = Math.sqrt(Math.abs(LEARNING_FACTOR) / 49);
    private final double MOMENTUM = 0;
    /**
     * Create default neural network
     */
    protected NeuralNetworkConfig config;
    protected int counter = 1;
    //New "style"
    private TDoubleArrayList[] neuronValues;
    private TDoubleArrayList[] neuronSums;
    //translated to an array this would look like this if il = 10 hl1 = 20 hl2 = 30 ol = 40
    //il: neuronWeights[0][10][20]
    //hl1: neuronWeights[1][20][30]
    //hl2: neuronWeights[2][30][40]
    //ol : doesn't have weights cause these are stored in hl1
    private List<List<TDoubleArrayList>> neuronWeights;
    private TDoubleArrayList[] deltaValues;
    private int error = 1;
    private int right = 1;

    public NeuralNetwork(NeuralNetworkConfig nnc) {
        if (nnc != null) {
            //Apply config
            this.processConfig(nnc);
        } else {
            this.processConfig(this.config);
        }
    }

    /**
     *
     * @param s
     * @param production
     */
    public void forwardSample(Sample sample, boolean production) throws ArrayIndexOutOfBoundsException {
        this.forwardPropagate(sample);

        //<editor-fold defaultstate="collapsed" desc="misc">
        boolean print = counter % 100 == 0;

        double biggest = 0;
        int biggestLabel = 0;
        StringBuffer text = null;
        StringBuffer help = null;

        if (print) {
            text = new StringBuffer();
            help = new StringBuffer();
        }

        if (print) {
            text.append("Sample Nr.").append(this.counter).append(" - ").append(sample.getType());
        }

        for (int neuron = 0; neuron < neuronValues[neuronValues.length - 1].size(); neuron++) {
            if (biggest < neuronValues[neuronValues.length - 1].getQuick(neuron)) {
                biggest = neuronValues[neuronValues.length - 1].getQuick(neuron);
                biggestLabel = neuron;
            }

            if (print) {
                help.append(neuron).append(":(").append(String.format("%.5f", neuronValues[neuronValues.length - 1].getQuick(neuron))).append(") ");
            }
        }
        if (print) {
            text.append(" Pre: ").append(biggestLabel).append(" ");
        }

        if (counter > 0) {
            if (biggestLabel == sample.getType()) {
                this.right++;
            } else {
                this.error++;
            }
        }
        if (print) {
            double percentage = ((100.0 * error) / (error + right));
            right = 0;
            error = 0;
            text.append(String.format("%4.1f", (percentage)));
            text.append("% [ ");
            text.append(help);
            text.append(" ]");

            System.out.println(text);
        }
        this.counter++;
        //</editor-fold>

        this.backPropagate(sample);
    }

    private void backPropagate(Sample sample) {
        double help;
        int neuronWeightsSize = neuronWeights.size();
        double deltaWeightSum = 0;

        //<editor-fold defaultstate="collapsed" desc="Calculate output layer delta">
        //change the output layer weights according to their given values
        for (int olNeuron = 0; olNeuron < config.getOutputLayerCount(); olNeuron++) {
            help = neuronValues[neuronValues.length - 1].getQuick(olNeuron);
            if (sample.getType() == olNeuron) {
                //the output should be 1
                deltaValues[deltaValues.length - 1].setQuick(olNeuron,
                        this.calculateActivationFunctionDerivate(help) * (help - 1) * this.LEARNING_FACTOR);
            } else {
                //the output should be 0 or 1 (help) or (help + 1)
                deltaValues[deltaValues.length - 1].setQuick(olNeuron,
                        this.calculateActivationFunctionDerivate(help) * (help) * this.LEARNING_FACTOR);
            }

        //<editor-fold defaultstate="collapsed" desc="Calculate the delta of the other Layers">
        for (int layer = neuronWeightsSize - 2; layer >= 0; layer--) {
            for (int neuron = 0; neuron < neuronWeights.get(layer + 1).size(); neuron++) {
                //<editor-fold defaultstate="collapsed" desc="comment">
                /*
                 * if (layer == neuronWeightsSize - 1) { //calculate the sum of
                 * all deltas with respect to the weight for (int hlToOlWeight =
                 * 0; hlToOlWeight < neuronWeights.get(neuronWeightsSize -
                 * 1).get(hlNeuron).size(); hlToOlWeight++) { deltaWeightSum +=
                 * deltaOutputNeurons[hlToOlWeight] *
                 * neuronWeights.get(layer).get(hlNeuron).getQuick(hlToOlWeight);
                 * } //normal weight changing of the hiddel layers } else {
                 * //same calculation with the only difference that we now use
                 * the deltaHiddenNeurons for (int weight = 0; weight <
                 * neuronWeights.size(); weight++) { deltaWeightSum +=
                 * deltaHiddenNeurons[weight] *
                 * neuronWeights.get(layer).get(hlNeuron).getQuick(weight); } }
                 */
                //</editor-fold>
                //calculate delta weight of the layer after this one
                for (int delta = 0; delta < deltaValues[layer + 1].size(); delta++) {
                    deltaWeightSum += deltaValues[layer + 1].getQuick(delta) * neuronWeights.get(layer + 1).get(neuron).getQuick(delta);
                }

                deltaValues[layer].setQuick(neuron,
                        deltaWeightSum
                        * this.calculateActivationFunctionDerivate(neuronValues[layer + 1].getQuick(neuron))
                        * this.LEARNING_FACTOR);

                deltaWeightSum = 0;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Adjust output layer weights">
        for (int olNeuron = 0; olNeuron < config.getOutputLayerCount(); olNeuron++) {
            for (int hlNeuron = 0; hlNeuron < config.getHiddenNeuronCounts()[config.getHiddenNeuronCounts().length - 1]; hlNeuron++) {
                neuronWeights.get(neuronWeights.size() - 1).get(hlNeuron).setQuick(olNeuron,
                        neuronWeights.get(neuronWeights.size() - 1).get(hlNeuron).getQuick(olNeuron)
                        + ((neuronValues[neuronValues.length - 1].getQuick(olNeuron))
                        * deltaValues[deltaValues.length - 1].getQuick(olNeuron))
                        + MOMENTUM * neuronWeights.get(neuronWeights.size() - 1).get(hlNeuron).getQuick(olNeuron));
            }
        }

        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Adjust all the other weights">
        for (int layer = neuronWeightsSize - 2; layer >= 0; layer--) {
            for (int neuron = 0; neuron < neuronWeights.get(layer + 1).size(); neuron++) {
                for (int weights = 0; weights < neuronWeights.get(layer).size(); weights++) {
                    //calculate weight
                    neuronWeights.get(layer).get(weights).setQuick(neuron,
                            neuronWeights.get(layer).get(weights).getQuick(neuron)
                            + ((neuronValues[layer].getQuick(weights)) * deltaValues[layer].getQuick(neuron))
                            + (MOMENTUM * (neuronWeights.get(layer).get(weights).getQuick(neuron))));

                }
            }
        }
        //</editor-fold>
    }

    private void forwardPropagate(Sample sample) {
        int help = 0;
        double helpSum = 0;

        //calculate sigmoids of samples and store them in the input layer (index 0)
        for (Double sampleValue : sample) {
            neuronValues[0].setQuick(help, this.calculateActivationFunction(sampleValue));
            ++help;
        }

        for (int layer = 1; layer < neuronValues.length; layer++) {
            for (int neuron = 0; neuron < neuronValues[layer].size(); neuron++) {
                //calculate the value with respect to every weight that is connected to this neuron
                for (int weights = 0; weights < neuronWeights.get(layer - 1).size(); weights++) {
                    helpSum += (neuronValues[layer - 1].getQuick(weights) * neuronWeights.get(layer).get(weights).getQuick(neuron));
                }
                helpSum = this.calculateActivationFunction(helpSum /*+ 1 /*BIAS*/);

                //neuron value set (with the activation function)
                neuronValues[layer].setQuick(neuron, helpSum);
                //reset helpSum
                helpSum = 0;
            }
        }
    }

    /**
     * Inits all weights between the neurons with random values between the two
     * params
     */
    private void initWeightsRandom() {
        //create new rnd obj
        Random r = new Random(new Date().getTime());

        //simply initialize every weight point with a random value
        for (List<TDoubleArrayList> layer : neuronWeights) {
            for (TDoubleArrayList neuron : layer) {
                for (int weight = 0; weight < neuron.size(); weight++) {
                    if (r.nextBoolean()) {
                        //bounds checking is not needed here
                        neuron.setQuick(weight, r.nextDouble()
                                * (config.getActivationFunction() == ActivationFunction.SIGMOID
                                ? INIT_WEIGHTS_FACTOR : -INIT_WEIGHTS_FACTOR));
                    } else {
                        neuron.setQuick(weight, r.nextDouble() * INIT_WEIGHTS_FACTOR);
                    }
                }
            }
        }

    }

    /**
     * Returns the current config of the neural network
     *
     * @return the current config
     */
    public NeuralNetworkConfig getConfig() 
    {
        return this.config;
    }

    /**
     * Function to process the config operations when a config object arrives
     */
    private void processConfig(NeuralNetworkConfig config) {

        //<editor-fold defaultstate="collapsed" desc="Deallocate the old variables">
        //if it is null skip processing
        if (config == null) {
            return;
        }
        //save variable
        if (config != null) {
            this.config = config;
        }

        //set the neuron values to null
        if (this.neuronValues != null) {
            this.neuronValues = null;
        }

        if (this.neuronSums != null) {
            //set the neuron sums to null
            this.neuronSums = null;
        }
        //tell the neuronWeights that we don't need it anymore
        if (this.neuronWeights != null) {
            this.neuronWeights = null;
        }
        //</editor-fold>

        /*
         * Refactor Learning Rate if SIGMOID is the activation function
         */
        if (config.getActivationFunction() == ActivationFunction.SIGMOID) {
            this.LEARNING_FACTOR *= -1;
        }
        /*
         * populate all variables
         */

        //<editor-fold defaultstate="collapsed" desc="Init neuron value lists">
        //start with the values of the neurons and sums
        this.neuronValues = new TDoubleArrayList[(config.getHiddenNeuronCounts().length + 2)];
        //input & output layer initialization
        this.neuronValues[0] = new TDoubleArrayList(new double[config.getInputFeatureCount()]);
        this.neuronValues[this.neuronValues.length - 1] = new TDoubleArrayList(new double[config.getOutputLayerCount()]);

        //init hidden layers
        for (int i = 1; i < this.neuronValues.length - 1; i++) {
            this.neuronValues[i] = new TDoubleArrayList(new double[config.getHiddenNeuronCounts()[i - 1]]);
        }

        //and finally the weights
        this.neuronWeights = new ArrayList<>(config.getHiddenNeuronCounts().length + 1);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Init delta values of the neurons">
        //next ones are the lists of the delta values
        this.deltaValues = new TDoubleArrayList[config.getHiddenNeuronCounts().length + 1];
        //init the output layer list
        this.deltaValues[this.deltaValues.length - 1] = new TDoubleArrayList(new double[config.getOutputLayerCount()]);
        //init the others
        for (int i = 0; i < this.deltaValues.length - 1; i++) {
            this.deltaValues[i] = new TDoubleArrayList(new double[config.getHiddenNeuronCounts()[i]]);
        }

        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Init neuron weight lists">
        this.neuronWeights = new ArrayList<>(config.getHiddenNeuronCounts().length + 1);

        for (int layer = 0; layer < config.getHiddenNeuronCounts().length + 1; layer++) {
            //connection from input to first hidden layer
            if (layer == 0) {
                this.neuronWeights.add(new ArrayList<TDoubleArrayList>(config.getInputFeatureCount()));

                //for each input neuron
                for (int neuron = 0; neuron < config.getInputFeatureCount(); neuron++) {
                    //create an array list that stores all the weights to the first hidden layer
                    this.neuronWeights.get(layer).add(new TDoubleArrayList(new double[config.getHiddenNeuronCounts()[0]]));
                }
            } else //connection from hidden to output layer
            if (layer == config.getHiddenNeuronCounts().length) {
                this.neuronWeights.add(new ArrayList<TDoubleArrayList>(config.getHiddenNeuronCounts()[config.getHiddenNeuronCounts().length - 1]));

                //for each neuron of the last hidden layer
                for (int neuron = 0; neuron < config.getHiddenNeuronCounts()[config.getHiddenNeuronCounts().length - 1]; neuron++) {
                    //create an array list that stores all the weights to the output layer
                    this.neuronWeights.get(layer).add(new TDoubleArrayList(new double[config.getOutputLayerCount()]));
                }
                //connection from previous hidden to next hidden layer
            } else {
                this.neuronWeights.add(new ArrayList<TDoubleArrayList>(config.getHiddenNeuronCounts()[layer - 1]));

                //for each hidden neuron
                for (int neuron = 0; neuron < config.getHiddenNeuronCounts()[layer - 1]; neuron++) {
                    //create an array list that stores all the weights to the hidden layer
                    this.neuronWeights.get(layer).add(new TDoubleArrayList(new double[config.getHiddenNeuronCounts()[layer - 1]]));
                }
            }
        }
        //</editor-fold>

        this.initWeightsRandom();
    }

    private double changeWeight(int layer, int changeNeuron, int weightNeuron, double delta, double deltaWeightSum) {
        double old = neuronWeights.get(layer).get(changeNeuron).getQuick(weightNeuron);

        //the old weight + the value which will be added to the weight
        double newD = old + (delta * this.LEARNING_FACTOR * neuronValues[layer - 1].getQuick(weightNeuron) * deltaWeightSum);

        //actually change the neuron
        neuronWeights.get(layer).get(changeNeuron).setQuick(weightNeuron, newD);

        //return difference
        return newD - old;
    }

    private double changeWeightHidden(int layer, int changeNeuron, int weightNeuron, double delta, double deltaWeightSum) {
        double old = neuronWeights.get(layer - 1).get(changeNeuron).getQuick(weightNeuron);

        //the old weight + the value which will be added to the weight
        double newD = old + (delta * this.LEARNING_FACTOR * neuronValues[layer].getQuick(weightNeuron) * deltaWeightSum);

        //actually change the neuron
        neuronWeights.get(layer - 1).get(changeNeuron).setQuick(weightNeuron, newD);

        //return difference
        return newD - old;
    }

    public void resetCounter() {
        this.counter = 1;
    }

    private double calculateActivationFunction(Double value) {
        return ActivationFunction.calculateNormal(this.config.getActivationFunction(), value);
    }

    private double calculateActivationFunctionDerivate(Double value) {
        return ActivationFunction.calculateDerivate(this.config.getActivationFunction(), value);
    }
}