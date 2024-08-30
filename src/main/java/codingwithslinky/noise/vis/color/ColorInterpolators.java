package codingwithslinky.noise.vis.color;

import static codingwithslinky.noise.vis.color.ColorUtil.clamp;
import static codingwithslinky.noise.vis.color.ColorUtil.hslToRGB;
import static codingwithslinky.noise.vis.color.ColorUtil.rgbToHSL;
import java.awt.Color;

/**
 *
 * @author Kheagen Haskins
 */
public class ColorInterpolators {

    // ------------------------------ Fields -------------------------------- //
    public static final ColorInterpolator LINEAR_RGB = new LinearInterpolator();
    public static final ColorInterpolator COSINE = new CosineInterpolator();
    public static final ColorInterpolator LINEAR_HSL = new HueInterpolator();

    /**
     * Interpolates between an arbitrary number of colours using a single
     * weight. The weight determines the position within the gradient defined by
     * the provided colours.
     */
    public static class LinearInterpolator implements ColorInterpolator {

        // ---------------------------- API Methods ----------------------------- //
        /**
         * Interpolates between an arbitrary number of colours using a single
         * weight. The weight determines the position within the gradient
         * defined by the provided colours.
         *
         * @param weight the weight for interpolation (typically between 0 and
         * 1).
         * @param colors the colours to interpolate between. At least one colour
         * must be provided.
         * @return the interpolated {@code Color}.
         * @throws IllegalArgumentException if no colours are provided.
         */
        @Override
        public Color interpolate(float weight, Color... colors) {
            if (colors.length == 0) {
                throw new IllegalArgumentException("At least one color must be provided.");
            }

            // If only one colour is provided, return it directly
            if (colors.length == 1) {
                return colors[0];
            }

            // Ensure weight is within the bounds [0, 1]
            weight = Math.max(0, Math.min(1, weight));

            // Calculate the length of each segment between two consecutive colours
            float segmentLength = 1.0f / (colors.length - 1);

            // Determine which segment the weight falls into
            int segmentIndex = (int) (weight / segmentLength);

            // Calculate the local weight within the current segment
            float localWeight = (weight - (segmentIndex * segmentLength)) / segmentLength;

            // Ensure the segmentIndex is within the valid range
            if (segmentIndex >= colors.length - 1) {
                segmentIndex = colors.length - 2;
                localWeight = 1f;
            }

            // Retrieve the two colours to interpolate between for the current segment
            Color c1 = colors[segmentIndex];
            Color c2 = colors[segmentIndex + 1];

            // Interpolate the RGB components between c1 and c2 using localWeight
            int red = (int) (c1.getRed() * (1 - localWeight) + c2.getRed() * localWeight);
            int green = (int) (c1.getGreen() * (1 - localWeight) + c2.getGreen() * localWeight);
            int blue = (int) (c1.getBlue() * (1 - localWeight) + c2.getBlue() * localWeight);

            // Clamp the RGB values to ensure they are within the valid range [0, 255]
            red = clamp(red);
            green = clamp(green);
            blue = clamp(blue);

            // Return the final interpolated colour
            return new Color(red, green, blue);
        }

    }

    /**
     * Interpolates between multiple colours using cosine interpolation.
     */
    public static class CosineInterpolator implements ColorInterpolator {

        // ---------------------------- API Methods ----------------------------- //
        /**
         * Interpolates between multiple colours using cosine interpolation.
         *
         * @param weight the interpolation weight, typically in the range [0,
         * 1].
         * @param colors the array of colours to interpolate between.
         * @return the interpolated colour.
         * @throws IllegalArgumentException if no colours are provided.
         */
        @Override
        public Color interpolate(float weight, Color... colors) {
            if (colors.length == 0) {
                throw new IllegalArgumentException("At least one colour must be provided.");
            }

            // If only one colour is provided, return it directly
            if (colors.length == 1) {
                return colors[0];
            }

            // Ensure weight is within the bounds [0, 1]
            weight = Math.max(0, Math.min(1, weight));

            // Calculate the length of each segment between two consecutive colours
            float segmentLength = 1.0f / (colors.length - 1);

            // Determine which segment the weight falls into
            int segmentIndex = (int) (weight / segmentLength);

            // Calculate the local weight within the current segment
            float localWeight = (weight - (segmentIndex * segmentLength)) / segmentLength;

            // Adjust the weight using the cosine interpolation formula
            float cosineWeight = (float) (1 - Math.cos(localWeight * Math.PI)) / 2f;

            // Ensure the segmentIndex is within the valid range
            if (segmentIndex >= colors.length - 1) {
                segmentIndex = colors.length - 2;
                cosineWeight = 1f;
            }

            // Retrieve the two colours to interpolate between for the current segment
            Color c1 = colors[segmentIndex];
            Color c2 = colors[segmentIndex + 1];

            // Interpolate the RGB components between c1 and c2 using cosineWeight
            int red = (int) (c1.getRed() * (1 - cosineWeight) + c2.getRed() * cosineWeight);
            int green = (int) (c1.getGreen() * (1 - cosineWeight) + c2.getGreen() * cosineWeight);
            int blue = (int) (c1.getBlue() * (1 - cosineWeight) + c2.getBlue() * cosineWeight);

            // Clamp the RGB values to ensure they are within the valid range [0, 255]
            red = clamp(red);
            green = clamp(green);
            blue = clamp(blue);

            // Return the final interpolated colour
            return new Color(red, green, blue);
        }

    }

    /**
     * Interpolates between multiple colours in the HSL (Hue, Saturation,
     * Lightness) colour space using linear interpolation.
     */
    public static class HueInterpolator implements ColorInterpolator {

        /**
         * Interpolates between multiple colours in the HSL (Hue, Saturation,
         * Lightness) colour space using linear interpolation.
         *
         * @param weight the interpolation weight, typically in the range [0,
         * 1].
         * @param colors the array of colours to interpolate between.
         * @return the interpolated colour.
         * @throws IllegalArgumentException if no colours are provided.
         */
        @Override
        public Color interpolate(float weight, Color... colors) {
            if (colors.length == 0) {
                throw new IllegalArgumentException("At least one colour must be provided.");
            }

            // If only one colour is provided, return it directly
            if (colors.length == 1) {
                return colors[0];
            }

            // Ensure weight is within the bounds [0, 1]
            weight = Math.max(0, Math.min(1, weight));

            // Calculate the length of each segment between two consecutive colours
            float segmentLength = 1.0f / (colors.length - 1);

            // Determine which segment the weight falls into
            int segmentIndex = (int) (weight / segmentLength);

            // Calculate the local weight within the current segment
            float localWeight = (weight - (segmentIndex * segmentLength)) / segmentLength;

            // Ensure the segmentIndex is within the valid range
            if (segmentIndex >= colors.length - 1) {
                segmentIndex = colors.length - 2;
                localWeight = 1.0f;
            }

            // Retrieve the two colours to interpolate between for the current segment
            Color c1 = colors[segmentIndex];
            Color c2 = colors[segmentIndex + 1];

            // Convert the colours to HSL
            float[] hsl1 = rgbToHSL(c1);
            float[] hsl2 = rgbToHSL(c2);

            // Interpolate the HSL components
            float h = (float) (hsl1[0] + (hsl2[0] - hsl1[0]) * localWeight);
            float s = (float) (hsl1[1] + (hsl2[1] - hsl1[1]) * localWeight);
            float l = (float) (hsl1[2] + (hsl2[2] - hsl1[2]) * localWeight);

            // Return the final interpolated colour in RGB
            return hslToRGB(new float[]{h, s, l});
        }

    }

}