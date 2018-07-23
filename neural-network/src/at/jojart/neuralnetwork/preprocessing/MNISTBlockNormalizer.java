package at.jojart.neuralnetwork.preprocessing;

import gnu.trove.list.array.TDoubleArrayList;

/**
 *
 * @author Thorsten Jojart
 */
public class MNISTBlockNormalizer {

    public static TDoubleArrayList normalize(byte[] values) {
        TDoubleArrayList normalized = new TDoubleArrayList(values.length / 49);

        //init the list
        for (int i = 0; i < 16; i++) {
            normalized.add(0);
        }

        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 7; k++) {
                    normalized.setQuick(((int) Math.ceil(i / 7) * j + j),
                            normalized.getQuick(((int) Math.ceil(i / 7) * j + j)) + (values[i * 28 + j * 7 + k] & 0xFF) / 49 / 255.0);
                }
            }
        }
        return normalized;
    }
}
