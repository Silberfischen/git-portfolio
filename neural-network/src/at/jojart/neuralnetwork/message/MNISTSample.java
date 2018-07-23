package at.jojart.neuralnetwork.message;

import at.jojart.neuralnetwork.preprocessing.MNISTPixelNormalizer;
import gnu.trove.list.array.TDoubleArrayList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a sample of a handwritten digit which is extracted by
 * the open MNIST database <a href="http://yann.lecun.com/exdb/mnist/">Official
 * MNIST site</a>
 *
 * @author Thorsten Jojart
 */
public class MNISTSample implements Sample,Serializable {

    /**
     * The normalized values of the sample
     */
    private final TDoubleArrayList normalizedFeatures;
    /**
     * The type of this sample
     */
    private final int label;

    public MNISTSample(byte[] samples, int label) {
        //save label
        this.label = label;
        this.normalizedFeatures = MNISTPixelNormalizer.normalize(samples);
    }
    
//
//    public static void main(String[] args) throws FileNotFoundException, IOException {
//        /**
//         * Open the sample inputstream
//         */
//        DataInputStream sample = new DataInputStream(new FileInputStream(new File("MNIST/t10k-images.idx3-ubyte")));
//        /**
//         * Open the label inputstream
//         */
//        DataInputStream label = new DataInputStream(new FileInputStream(new File("MNIST/t10k-labels.idx1-ubyte")));
//
//        //variable defination
//        int sampleNr, labelNr;
//        int rows, columns;
//        int[] data;
//        int[][] data2d;
//
//        //test if files contain the "magic number"
//        if (sample.readInt() != 2051 || label.readInt() != 2049) {
//            System.out.println("Files are wrong!");
//            return;
//        }
//        //read how much samples the files include
//        sampleNr = sample.readInt();
//        labelNr = label.readInt();
//
//        //read height and width
//        rows = sample.readInt();
//        columns = sample.readInt();
//
//        //read how big the images are
//        data2d = new int[rows][columns];
//        int greyscale;
//
//        long systime = System.currentTimeMillis();
//
//        for (int b = 0; b < 500; b++) {
//            for (int i = 0; i < rows; i++) {
//                for (int j = 0; j < columns; j++) {
//                    greyscale = sample.readUnsignedByte();
//                    data2d[i][j] = new Color(greyscale, greyscale, greyscale).getRGB();
//                }
//            }
//            ImageIO.write(MNISTExtractor.getImageFromArray(data2d, columns, rows), "PNG", new File("MNISTPics/test" + b + "(" + label.readUnsignedByte() + ").png"));
//            if (b % 1000 == 0 && b != 0) {
//                System.out.println(b + "(" + (System.currentTimeMillis() - systime) / 1000 + " s)");
//            }
//        }
//
//        //close the streams
//        sample.close();
//        label.close();
//    }

    @Override
    public int getFeatureCount() {
        return normalizedFeatures.size();
    }

    @Override
    public Iterator<Double> iterator() {
        return new Iterator<Double>() {
            /**
             * Counter variable for the iterator
             */
            private int counter = 0;

            @Override
            public boolean hasNext() {
                //test if the counter is greater then the count of the features
                if (normalizedFeatures.size() > counter) {
                    return true;
                }
                return false;
            }

            @Override
            public Double next() {
                //retrieve the object
                Double d = normalizedFeatures.get(counter);
                //increment the counter
                counter++;
                return d;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This method won't be supported!");
            }
        };
    }

    @Override
    public int getType() {
        return this.label;
    }

    @Override
    public String toString() {
        return "Sample with Label " + this.label;
    }
}