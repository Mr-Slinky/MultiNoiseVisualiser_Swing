package codingwithslinky.noise.vis.color;

import java.awt.Color;

/**
 * A functional interface for interpolating between multiple colours.
 * Implementations of this interface provide different algorithms for blending
 * or transitioning between an arbitrary number of colours.
 *
 * The primary method, {@link #interpolate(double, Color...)}, uses a weight
 * parameter to determine the blend ratio between the provided colours, allowing
 * for smooth transitions within a defined gradient. This is useful in various
 * applications such as graphics, visualisations, and UI design where colour
 * gradients are required.
 *
 * @apiNote Implementations of this interface should ensure that the
 * {@code weight} is properly clamped to the range [0, 1] and handle cases where
 * fewer colours are provided gracefully, either by throwing an exception or
 * returning a sensible default.
 *
 * @implSpec The {@code interpolate} method should handle at least one colour
 * and throw an {@code IllegalArgumentException} if no colours are provided.
 *
 * @since 1.0
 * @author Kheagen Haskins
 */
@FunctionalInterface
public interface ColorInterpolator {

    /**
     * Interpolates between an arbitrary number of colours using a single
     * weight. The weight determines the position within the gradient defined by
     * the provided colours.
     *
     * @param weight the weight for interpolation, typically between 0 and 1. A
     * weight of 0 corresponds to the first colour, while a weight of 1
     * corresponds to the last colour.
     * @param colors the colours to interpolate between. At least one colour
     * must be provided.
     * @return the interpolated {@code Color}.
     * @throws IllegalArgumentException if no colours are provided.
     */
    Color interpolate(float weight, Color... colors);

}