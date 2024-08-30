package codingwithslinky.noise.vis.color;

import java.awt.Color;

/**
 * Utility class for colour conversion and manipulation.
 * <p>
 * This class provides methods for converting colours between different colour
 * models, such as RGB (Red, Green, Blue) and HSL (Hue, Saturation, Lightness).
 * It also includes methods for clamping colour values to ensure they remain
 * within valid ranges.
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Converting hexadecimal colour values to {@code Color} objects.</li>
 * <li>Converting colours from the RGB model to the HSL model.</li>
 * <li>Converting colours from the HSL model to the RGB model.</li>
 * <li>Clamping RGB component values to the [0, 255] range.</li>
 * <li>Clamping hexadecimal colour values to the [0x000000, 0xFFFFFF] range.</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>{@code
 * int[] hexValues = {0xFF5733, 0x33FF57, 0x3357FF};
 * Color[] colors = ColorUtil.toColors(hexValues);
 *
 * Color rgbColor = new Color(255, 87, 51);
 * float[] hsl = ColorUtil.rgbToHSL(rgbColor);
 * Color newRgbColor = ColorUtil.hslToRGB(hsl);
 * }</pre>
 *
 * @version 1.0
 * @author Kheagen Haskins
 */
public class ColorUtil {

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Converts a list of hexadecimal integers into colours
     *
     * @param hex the array of hexadecimal values
     * @return
     */
    public static Color[] toColors(int[] hex) {
        Color[] colors = new Color[hex.length];

        for (int i = 0; i < hex.length; i++) {
            colors[i] = new Color(clampHex(hex[i]));
        }

        return colors;
    }

    /**
     * Converts a colour from the RGB (Red, Green, Blue) colour model to the HSL
     * (Hue, Saturation, Lightness) colour model. This conversion is useful for
     * performing operations like interpolation in the HSL space.
     *
     * @param color the {@code Color} object representing the RGB colour.
     * @return an array containing the hue, saturation, and lightness values.
     */
    public static float[] rgbToHSL(Color color) {
        float[] hsl = new float[3];
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float delta = max - min;

        // Hue calculation
        if (delta == 0) {
            hsl[0] = 0;
        } else if (max == r) {
            hsl[0] = (60 * ((g - b) / delta) + 360) % 360;
        } else if (max == g) {
            hsl[0] = (60 * ((b - r) / delta) + 120) % 360;
        } else {
            hsl[0] = (60 * ((r - g) / delta) + 240) % 360;
        }

        // Lightness calculation
        hsl[2] = (max + min) / 2;

        // Saturation calculation
        if (delta == 0) {
            hsl[1] = 0;
        } else {
            hsl[1] = delta / (1 - Math.abs(2 * hsl[2] - 1));
        }

        return hsl;
    }

    /**
     * Converts a colour from the HSL (Hue, Saturation, Lightness) colour model
     * to the RGB (Red, Green, Blue) colour model. The conversion is useful for
     * interpolating colours in the HSL space and then displaying them in RGB.
     *
     * @param hsl an array containing the hue, saturation, and lightness values.
     * @return a {@code Color} object representing the corresponding RGB colour.
     */
    public static Color hslToRGB(float[] hsl) {
        float c = (1 - Math.abs(2 * hsl[2] - 1)) * hsl[1];
        float x = c * (1 - Math.abs((hsl[0] / 60) % 2 - 1));
        float m = hsl[2] - c / 2;

        float r = 0, g = 0, b = 0;

        if (0 <= hsl[0] && hsl[0] < 60) {
            r = c;
            g = x;
        } else if (60 <= hsl[0] && hsl[0] < 120) {
            r = x;
            g = c;
        } else if (120 <= hsl[0] && hsl[0] < 180) {
            g = c;
            b = x;
        } else if (180 <= hsl[0] && hsl[0] < 240) {
            g = x;
            b = c;
        } else if (240 <= hsl[0] && hsl[0] < 300) {
            r = x;
            b = c;
        } else {
            r = c;
            b = x;
        }

        int red = (int) ((r + m) * 255);
        int green = (int) ((g + m) * 255);
        int blue = (int) ((b + m) * 255);

        return new Color(clamp(red), clamp(green), clamp(blue));
    }

    /**
     * Clamps an integer value to the range [0, 255]. This method ensures that
     * colour component values do not exceed the valid range for RGB colours.
     *
     * @param value the integer value to clamp.
     * @return the clamped value, constrained to the range [0, 255].
     */
    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    /**
     * Clamps an integer value to the range [0, 0xFFFFFF]. This method ensures
     * that colour component values do not exceed the valid range for
     * hexadecimal colours.
     *
     * @param value the integer value to clamp.
     * @return the clamped value, constrained to the range [0, 0xFFFFFF].
     */
    private static int clampHex(int value) {
        return Math.max(0, Math.min(0xFFFFFF, value));
    }

}
