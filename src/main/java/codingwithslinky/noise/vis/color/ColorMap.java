package codingwithslinky.noise.vis.color;

import static codingwithslinky.noise.vis.color.ColorUtil.toColors;
import java.awt.Color;

/**
 * The {@code ColorMap} class represents a precomputed map of colours used for
 * fast colour lookup based on interpolation weights. This class is designed to
 * efficiently provide interpolated colours by precomputing a set of colours
 * using a specified interpolation strategy and storing them in an array.
 *
 * <p>
 * The {@code ColorMap} is useful in scenarios where colour interpolation needs
 * to be performed frequently and quickly, such as in animations,
 * visualisations, or graphical effects. By precomputing the colour values, the
 * class minimises the computational overhead associated with real-time
 * interpolation.
 *
 * <p>
 * Key Features:
 * <ul>
 * <li><b>Precomputed Colour Map:</b> Colours are precomputed using a specified
 * interpolation strategy, allowing for fast colour lookup during runtime.</li>
 * <li><b>Custom Interpolation:</b> Supports custom interpolation strategies
 * through the {@link ColorInterpolator} functional interface, enabling
 * different methods of blending or transitioning between colours.</li>
 * <li><b>Hexadecimal Colour Support:</b> Allows colours to be specified using
 * hexadecimal values, providing flexibility in defining the colour
 * palette.</li>
 * </ul>
 * <p>
 * Example Usage:
 * <pre>
 * ColorInterpolator interpolator = new LinearRGBInterpolator();
 * ColorMap colorMap = new ColorMap(interpolator, Color.RED, Color.GREEN, Color.BLUE);
 * Color interpolatedColor = colorMap.getColorAt(0.5);
 * </pre>
 *
 * @see ColorInterpolator
 * @since 1.0
 */
public final class ColorMap {

    // ------------------------------ Static -------------------------------- //
    /**
     * The size of the colour map, indicating the number of colours available
     * for interpolation.
     */
    private static final int COLOR_MAP_SIZE = 256; // Number of colours in the map

    /**
     * The last index in of the colour map array. Saves the most minuscule
     * amount of time.
     */
    private static final int LAST_INDEX = COLOR_MAP_SIZE - 1;

    /**
     * Default linear colour interpolator
     */
    private static final ColorInterpolator DEFAULT_INTERPOLATOR = ColorInterpolators.LINEAR_RGB;

    // ------------------------------ Fields -------------------------------- //
    /**
     * An array storing the precomputed colours used for fast colour lookup
     * based on a weight. The colours are initialised using linear RGB
     * interpolation.
     */
    private Color[] colorMap;

    private int resolution;

    private int colorCount;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a ColorMap with the specified interpolator and colors.
     *
     * @param interpolator the strategy used for interpolating between the
     * colors in this map
     * @param colors an array of colors to be interpolated
     */
    public ColorMap(ColorInterpolator interpolator, Color... colors) {
        if (colors == null || colors.length == 0) {
            throw new IllegalArgumentException("At least one color must be provided.");
        }

        colorMap = new Color[COLOR_MAP_SIZE];
        for (int i = 0; i < COLOR_MAP_SIZE; i++) {
            float weight = (float) i / LAST_INDEX;
            colorMap[i] = interpolator.interpolate(weight, colors);
        }

        resolution = COLOR_MAP_SIZE;
        colorCount = colors.length;
    }

    /**
     * Constructs a ColorMap with the specified interpolator and hex color
     * values. Converts the hex values to Color objects before interpolating.
     *
     * @param interpolator the strategy used for interpolating between the
     * colors in this map
     * @param hexValues an array of integer hex color values to be converted to
     * Color objects
     */
    public ColorMap(ColorInterpolator interpolator, int... hexValues) {
        this(interpolator, toColors(hexValues));
    }

    /**
     * Constructs a ColorMap with the specified hex color values and the default
     * interpolator. Converts the hex values to Color objects before
     * interpolating.
     *
     * @param hexValues an array of integer hex color values to be converted to
     * Color objects
     */
    public ColorMap(int... hexValues) {
        this(DEFAULT_INTERPOLATOR, toColors(hexValues));
    }

    /**
     * Constructs a ColorMap with the specified colors and the default
     * interpolator.
     *
     * @param colors an array of Color objects to be interpolated
     */
    public ColorMap(Color... colors) {
        this(DEFAULT_INTERPOLATOR, colors);
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Retrieves the resolution of the colour map.
     *
     * @return the current resolution of the colour map.
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Retrieves the colour from the precomputed colour map based on the given
     * weight. The weight should be between 0 and 1, where 0 corresponds to the
     * first colour in the map and 1 corresponds to the last.
     *
     * @param weight the weight for interpolation (between 0 and 1).
     * @return the interpolated {@code Color}.
     */
    public Color getColorAt(float weight) {
        weight = Math.max(0, Math.min(1, weight));

        // Calculate the index based on the current resolution
        int effectiveLastIndex = resolution - 1;
        int scaledIndex = (int) Math.round(weight * effectiveLastIndex); // rounded to ensure 0 can be reached
        int index = (int) ((float) scaledIndex / effectiveLastIndex * LAST_INDEX);

        return colorMap[index];
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the resolution of the colour map. The resolution determines the
     * number of critical indices that can be accessed in the colour map. This
     * method ensures that the resolution is within the valid range.
     *
     * @param resolution the new resolution value. It must be between 1 and
     * {@code COLOR_MAP_SIZE}.
     */
    public void setResolution(int resolution) {
        this.resolution = Math.max(0, Math.min(COLOR_MAP_SIZE, resolution));
    }

    /**
     * Increases the resolution of the {@code colorMap}. This method increases
     * the resolution by multiplying the current resolution by the number of
     * colours, ensuring it does not exceed the maximum defined size.
     */
    public void increaseResolution() {
        resolution = Math.min(COLOR_MAP_SIZE, resolution * 2);
    }

    /**
     * Decreases the resolution of the colour map dividing it by the number of
     * colours. If the resolution is already at the minimum, it will not be
     * decreased further.
     */
    public void decreaseResolution() {
        resolution = Math.max(colorCount, resolution / 2);
    }

}