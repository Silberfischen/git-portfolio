package at.jojart.neuralnetwork.data.extractor;

import at.jojart.neuralnetwork.NeuralNetwork;
import at.jojart.neuralnetwork.message.MNISTBlockSample;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An extractor designed for extracting data from .idx3-Files(sample data) and
 * .idx1-Files(label data) which occur in the MNIST database of handwritten
 * digits
 *
 * @author Thorsten Jojart
 */
public class MNISTExtractor implements Extractor {

    /**
     * The count of all samples the mnist-file contains
     */
    private int sampleSize;
    /**
     * The width of the picture in pixels
     */
    private int width;
    /**
     * The height of the picture in pixels
     */
    private int height;
    /**
     * The inputstream of the mnist samples
     */
    private DataInputStream sampleData;
    /**
     * The inputstream of the mnist labels
     */
    private DataInputStream labelData;
    private byte sampleStore[];

    /**
     * Constructor of the MNISTExtractor It reads all the pre-sample data and
     * initializes everything needed for further processing
     *
     * @param sample
     * @param label
     */
    public MNISTExtractor(File sample, File label) throws FileNotFoundException, IOException {
        this.sampleData = new DataInputStream(new FileInputStream(sample));
        this.labelData = new DataInputStream(new FileInputStream(label));

        //test if files contain the "magic number"
        if (this.sampleData.readInt() != 2051 || this.labelData.readInt() != 2049) {
            System.out.println("Files are wrong!");
            //throw new NeuralNetworkException("Files are wrong!");
            return;
        }

        //read how much samples are provided
        this.sampleSize = this.sampleData.readInt();
        if (this.sampleSize != this.labelData.readInt()) {
            System.out.println("Sample and label files don't match!");
            //throw new NeuralNetworkException("Sample and label files don't match!");
            return;
        }

        //Read dimensions
        this.height = this.sampleData.readInt();
        this.width = this.sampleData.readInt();

        this.sampleStore = new byte[this.height * this.width];
    }

    @Override
    protected void finalize() throws Throwable {
        //Call finalizer to close resources
        super.finalize();

        this.labelData.close();
        this.sampleData.close();
    }

    public static BufferedImage getImageFromArray(int[][] pixels, int width, int height) {
        //Create new buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //iterate through all samples
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //set rgb value of the pixel
                image.setRGB(j, i, pixels[i][j]);
            }
        }
        return image;
    }

    @Override
    public void provideData(NeuralNetwork nn) {
        try {
            //run till the end of the file
            //while (this.sampleData.available() > 783 && this.labelData.available() > 0) {
            for (int i = 0; i < 60000; i++) {
                //read 784 byte per read to improve performance
                this.sampleData.read(this.sampleStore);
                //push the new sample to the neural-network
                //nn.forwardSample(new MNISTSample(this.sampleStore, this.labelData.readByte()), false);
                nn.forwardSample(new MNISTBlockSample(this.sampleStore, this.labelData.readByte()), false);
            }
            //EntryPoint.getNeuralNetworkCreator().shutdown();
            //}
        } catch (IOException ex) {
            Logger.getLogger(MNISTExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}