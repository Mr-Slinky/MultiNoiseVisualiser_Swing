package codingwithslinky.noise;

import java.util.List;
import static java.lang.Math.pow;
import java.util.ArrayList;

/**
 * The {@code NoiseUtil} class provides utility functions and constants for
 * generating and working with Perlin noise. It includes methods for generating
 * gradient vectors, performing interpolation, and calculating gradient values
 * for noise generation. The class also provides precomputed gradient vectors
 * for 2D, 3D, and 4D spaces, used in noise calculations.
 *
 * <p>
 * This utility class is essential for implementing noise-based algorithms, such
 * as Perlin noise, by providing the necessary mathematical tools and constants.
 * </p>
 *
 * @see java.lang.Math
 * @see java.util.List
 *
 * @author Kheagen Haskins
 */
public class NoiseUtil {

    public static final double[][] GRAD2 = generateGradients(2);
    public static final double[][] GRAD3 = generateGradients(3);
    public static final double[][] GRAD4 = generateGradients(4);

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Computes the fade function used for smoothing the transition between
     * gradients. This function is often used in Perlin noise generation.
     *
     * @param t The input value, typically in the range [0, 1].
     * @return The faded value, calculated using the polynomial 6t^5 - 15t^4 +
     * 10t^3.
     */
    public static double fade(double t) {
        return pow(t, 3) * (t * (t * 6 - 15) + 10);
    }

    /**
     * Performs a linear interpolation between two values.
     *
     * @param t The interpolation factor, typically in the range [0, 1]. When
     * t=0, the result is a; when t=1, the result is b.
     * @param a The start value.
     * @param b The end value.
     * @return The interpolated value, calculated as a + t * (b - a).
     */
    public static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    /**
     * Generates gradient vectors for a given number of dimensions.
     *
     * @param dimensions the number of dimensions
     * @return a 2D array where each sub-array represents a gradient vector
     */
    public static double[][] generateGradients(int dimensions) {
        List<double[]> gradients = new ArrayList<>();

        // Generate vectors with all possible combinations of -1, 0, and 1
        generateVectors(dimensions, new double[dimensions], gradients);

        // Convert the list of gradients to a 2D array
        double[][] gradArray = new double[gradients.size()][];
        gradients.toArray(gradArray);

        return gradArray;
    }

    /**
     * Computes the gradient value for a given corner of the hypercube. The
     * gradient vector is chosen based on the hash value, and the dot product
     * with the coordinates is calculated.
     *
     * @param hash A value derived from the permutation table, used to select
     * the gradient vector.
     * @param coordinates The coordinates within the unit hypercube.
     * @return The dot product of the gradient vector and the coordinates.
     */
    public static double grad(int hash, double... coordinates) {
        int d = coordinates.length;
        double result = 0.0;

        // Create a unit vector for the gradient based on the hash
        for (int i = 0; i < d; i++) {
            // For each dimension, decide if the component is positive or negative
            double gradientComponent = ((hash & (1 << i)) != 0) ? coordinates[i] : -coordinates[i];
            result += gradientComponent;
        }

        return result;
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Recursively generates gradient vectors.
     *
     * @param dimensions the total number of dimensions
     * @param current the current vector being constructed
     * @param gradients the list to store generated gradient vectors
     */
    private static void generateVectors(int dimensions, double[] current, List<double[]> gradients) {
        if (dimensions == 0) {
            // Add the vector if it's non-zero (at least one non-zero component)
            if (!isZeroVector(current)) {
                gradients.add(current.clone());
            }
            return;
        }

        // Recursively set each dimension to -1, 0, or 1
        for (int i = -1; i <= 1; i++) {
            current[current.length - dimensions] = i;
            generateVectors(dimensions - 1, current, gradients);
        }
    }

    /**
     * Checks if a vector is a zero vector.
     *
     * @param vector the vector to check
     * @return true if the vector is zero, false otherwise
     */
    private static boolean isZeroVector(double[] vector) {
        for (double v : vector) {
            if (v != 0) {
                return false;
            }
        }
        return true;
    }

}