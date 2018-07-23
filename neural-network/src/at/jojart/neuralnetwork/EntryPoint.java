package at.jojart.neuralnetwork;

import at.jojart.activation_functions.ActivationFunction;
import at.jojart.neuralnetwork.data.extractor.MNISTExtractor;
import at.jojart.neuralnetwork.message.NeuralNetworkConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Thorsten Jojart
 */
public class EntryPoint {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
        long l = System.currentTimeMillis();
        NeuralNetwork nn = new NeuralNetwork(
                new NeuralNetworkConfig(490, 10, NeuralNetworkConfig.CONNECT_DEFAULT, ActivationFunction.SIGMOID,49,490,49));
        
        //create the data extractor
        MNISTExtractor m = new MNISTExtractor(new File("MNIST/train-images.idx3-ubyte"), new File("MNIST/train-labels.idx1-ubyte"));
        //extract the data
        m.provideData(nn);

        long l2 = System.currentTimeMillis();

        m = new MNISTExtractor(new File("MNIST/t10k-images.idx3-ubyte"), new File("MNIST/t10k-labels.idx1-ubyte"));

        nn.resetCounter();
        m.provideData(nn);
        System.out.println("Learning First took: " + (System.currentTimeMillis() - l) / 1000 + " seconds");
        System.out.println("Recognizing Second took: " + (System.currentTimeMillis() - l2) / 1000 + " seconds");

    }
}