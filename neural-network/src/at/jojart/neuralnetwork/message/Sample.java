package at.jojart.neuralnetwork.message;

/**
 * This class represents a sample which contains data that is relevant for
 * learning.
 * 
 *
 * @author Thorsten Jojart
 */
public interface Sample extends Iterable<Double> {

    /**
     * This function returns the count of the features which are contained in
     * the sample
     *
     * @return the count of all features
     */
    public int getFeatureCount();
   
   /**
    * Returns the type of the sample as an int
    * @return type of the sample as an int
    */ 
    public int getType();
}