package at.jojart.neuralnetwork.message;

import at.jojart.neuralnetwork.preprocessing.MNISTBlockNormalizer;
import gnu.trove.list.array.TDoubleArrayList;
import java.io.Serializable;
import java.util.Iterator;

/**
 * This class represents a sample of a handwritten digit which is extracted by
 * the open MNIST database <a href="http://yann.lecun.com/exdb/mnist/">Official
 * MNIST site</a>
 *
 * @author Thorsten Jojart
 */
public class MNISTBlockSample implements Sample, Serializable {

    /**
     * The normalized values of the sample
     */
    private final TDoubleArrayList normalizedFeatures;
    /**
     * The type of this sample
     */
    private final int label;

    public MNISTBlockSample(byte[] samples, int label) {
        //save label
        this.label = label;
        this.normalizedFeatures = MNISTBlockNormalizer.normalize(samples);
    }

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