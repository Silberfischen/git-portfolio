package at.jojart.activation_functions;

/**
 *
 * @author Thorsten Jojart
 */
public class ActivationFunction {

    public static final int SIGMOID = 0;
    public static final int TANH = 1;
    public static final int BIP_SIGMOID = 2;

    public static double sigmoidNormal(double value) {
        //Sigmoid function
        return (1 / (1 + Math.exp(-1 * value)));
    }

    public static double sigmoidDerivate(double value) {
        //1 sigmoid calculation instead of 2 is worth the "overhead" of 1 double
        double help = ActivationFunction.sigmoidNormal(value);

        return help * (1 - help);
    }

    public static double tanhNormal(double value) {
        return StrictMath.tanh(value);
    }

    public static double tanhDerivate(double value) {
        return 1 / ((StrictMath.cosh(value)) * (StrictMath.cosh(value)));
    }

    public static double bipSigmoidNormal(double value) {
        return 2 / (1 + Math.exp(-value)) - 1;
    }

    public static double bipSigmoidDerivate(double value) {
        double help = Math.exp(value);
        return (2 * help) / ((help + 1) * (help + 1));
    }

    public static double calculateNormal(int activationFunction, double value) {
        //test which function is specified in the config
        switch (activationFunction) {
            case ActivationFunction.SIGMOID:
                return ActivationFunction.sigmoidNormal(value);
            case ActivationFunction.TANH:
                return ActivationFunction.tanhNormal(value);
            case ActivationFunction.BIP_SIGMOID:
                return ActivationFunction.bipSigmoidNormal(value);
            default:
                return Double.NaN;
        }
    }

    public static double calculateDerivate(int activationFunction, double value) {
        //test which derivated function is specified in the config
        switch (activationFunction) {
            case ActivationFunction.SIGMOID:
                return ActivationFunction.sigmoidDerivate(value);
            case ActivationFunction.TANH:
                return ActivationFunction.tanhDerivate(value);
            case ActivationFunction.BIP_SIGMOID:
                return ActivationFunction.bipSigmoidDerivate(value);
            default:
                return Double.NaN;
        }
    }
}
