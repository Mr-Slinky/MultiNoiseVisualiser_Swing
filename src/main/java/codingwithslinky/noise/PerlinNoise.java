package codingwithslinky.noise;

import static codingwithslinky.noise.NoiseUtil.fade;
import static codingwithslinky.noise.NoiseUtil.grad;
import static codingwithslinky.noise.NoiseUtil.lerp;

import java.util.Arrays;
import java.util.Random;

/**
 * An implementation of Perlin Noise for arbitrary dimensions. This class aims
 * to generalise the Perlin Noise calculation, making it dimension-independent,
 * unlike traditional implementations that are specific to 2D, 3D, or 4D noise.
 * It decouples the core logic from a fixed set of dimensions, allowing for more
 * flexible and reusable noise generation.
 *
 * The implementation leverages utility methods from the `NoiseUtility` class,
 * including functions for gradient calculation and interpolation. The
 * permutation table and gradient vectors are initialised based on a given seed
 * and dimension count.
 *
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * PerlinNoise noise = new PerlinNoise(seed, 3);
 * double value = noise.noise(x, y, z);
 * }
 * </pre>
 *
 * <p>
 * Note: This implementation supports 2D to 4D Perlin Noise. The `dimensions`
 * parameter must be within this range.
 * </p>
 *
 * @version 1.0
 * @see codingwithslinky.noise.NoiseUtil
 * @see NoiseXD
 *
 * @throws IllegalArgumentException if dimensions are not between 2D and 4D
 *
 * @implNote This implementation relies on gradient vectors and a permutation
 * table to generate smooth, pseudo-random noise. It uses linear interpolation
 * between gradients at the corners of a unit hypercube for each dimension.
 * 
 * @author Kheagen Haskins
 *
 */
public class PerlinNoise implements NoiseXD {

    /**
     * The size of the permutation table, set to 512 to allow for efficient
     * look-up of gradients and prevent boundary effects by duplicating the
     * initial permutation.
     */
    private static final int PERM_SIZE = 512;

    /**
     * An array representing the permutation table used for gradient lookup. It
     * contains 512 elements to cover all possible values and prevent boundary
     * effects by duplicating the permutation values.
     */
    private final int[] perm = new int[PERM_SIZE];

    /**
     * Constructs a PerlinNoise object with a specified seed and number of
     * dimensions. Initialises the gradient table and permutation table based on
     * the given seed.
     *
     * @param seed the seed value used to initialise the permutation table,
     * allowing for repeatable and pseudo-random noise generation.
     * @param dimensions the number of dimensions for the Perlin Noise; must be
     * between 2 and 4, inclusive.
     * @throws IllegalArgumentException if the dimensions are not within the
     * allowed range (2D-4D).
     */
    public PerlinNoise(int seed, int dimensions) {
        if (dimensions < 2 || dimensions > 4) {
            throw new IllegalArgumentException("Dimensions must be between 2D and 4D");
        }

        generatePermutations(seed);
    }

    /**
     * Generates Perlin noise for the given input values. The method supports
     * multiple dimensions and calculates the noise value based on the position
     * within the noise space.
     *
     * @param values the coordinates in the noise space, with the number of
     * elements corresponding to the number of dimensions.
     * @return the noise value at the given coordinates.
     */
    @Override
    public double noise(double... values) {
        int d = values.length; // dimensions
        int[] V = new int[d];
        double[] v = new double[d];
        double[] fades = new double[d];

        int p = (PERM_SIZE / 2 - 1); // 255;
        for (int i = 0; i < d; i++) {
            V[i] = (int) Math.floor(values[i]) & p;
            v[i] = values[i] - Math.floor(values[i]);
            fades[i] = fade(v[i]);
        }

        // Step 1: Generate all corner points and calculate their gradient values
        int corners = 1 << d; // 2^d corners
        double[] gradients = new double[corners];
        for (int i = 0; i < corners; i++) {
            int index = 0;
            double[] gradPos = new double[d];
            for (int j = 0; j < d; j++) {
                if ((i & (1 << j)) != 0) {
                    index = perm[(index + V[j] + 1) & p];
                    gradPos[j] = v[j] - 1;
                } else {
                    index = perm[(index + V[j]) & p];
                    gradPos[j] = v[j];
                }
            }
            gradients[i] = grad(index, gradPos);
        }

        // Step 2: Interpolate the gradients
        return interpolateGradients(gradients, fades, d);
    }

    /**
     * Recursively interpolates between gradient values based on the fade values
     * for each dimension. This method is used to blend the contributions of the
     * corner gradients smoothly.
     *
     * @param gradients the gradient values at the corners of the unit
     * hypercube.
     * @param fades the fade values for each dimension, used for interpolation.
     * @param dimensions the current dimension being interpolated.
     * @return the interpolated noise value.
     */
    private double interpolateGradients(double[] gradients, double[] fades, int dimensions) {
        if (dimensions == 1) {
            return lerp(fades[0], gradients[0], gradients[1]);
        }

        int half = gradients.length / 2;
        double[] lowerHalf = Arrays.copyOfRange(gradients, 0, half);
        double[] upperHalf = Arrays.copyOfRange(gradients, half, gradients.length);

        double lowerInterp = interpolateGradients(lowerHalf, fades, dimensions - 1);
        double upperInterp = interpolateGradients(upperHalf, fades, dimensions - 1);

        return lerp(fades[dimensions - 1], lowerInterp, upperInterp);
    }

    /**
     * Generates a permutation table using the provided seed. The permutation
     * table is used to randomise the gradient lookup in the noise generation
     * process, ensuring smooth transitions and eliminating boundary effects.
     *
     * @param seed the seed value used to initialise the permutation table.
     */
    private void generatePermutations(int seed) {
        Random rand = new Random(seed);
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        for (int i = 255; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }

        for (int i = 0; i < 256; i++) {
            perm[i] = p[i];
            perm[256 + i] = p[i];
        }
    }

}