package at.jojart.neuralnetwork.preprocessing;

import gnu.trove.list.array.TDoubleArrayList;

/**
 *
 * @author Thorsten Jojart
 */
public class MNISTPixelNormalizer {
    
    public static TDoubleArrayList normalize(byte[] values) {
        TDoubleArrayList normalized = new TDoubleArrayList(values.length);
        
        for (byte value : values) {
            if (value < 0 || value > 255) {
                normalized.add(Double.NaN);
            }
            normalized.add((value & 0xFF) / 255.0);
        }
        return normalized;
    }
}
