package codingwithslinky.noise.vis.color;

import java.awt.Color;

/**
 * Defines a palette of static color constants used throughout the application.
 * This class serves as a central repository for managing color themes by
 * defining primary, secondary, tertiary, dark, and light colors. The colors are
 * predefined with specific hex values representing visual elements consistently
 * across the application.
 *
 * <p>
 * Each color in the palette is represented by a static final {@link Color}
 * object, which can be accessed directly from anywhere in the application.</p>
 *
 * <p>
 * Usage example:</p>
 * <pre>
 *   Color primaryColor = Palette.PRIMARY;
 *   Color backgroundColor = Palette.DARK;
 * </pre>
 *
 * @author Kheagen Haskins
 */
public class Palette {

    /**
     * Primary theme color represented as a yellow tone.
     */
    public static final Color PRIMARY = new Color(0xE6C600);

    /**
     * Secondary theme color represented as a magenta tone.
     */
    public static final Color SECONDARY = new Color(0xE6008B);

    /**
     * Tertiary theme color represented as a cyan tone.
     */
    public static final Color TERTIARY = new Color(0x00D9F5);

    /**
     * Dark theme color used for backgrounds or text, represented as a jet black
     * tone.
     */
    public static final Color DARK = new Color(0x282B28);

    /**
     * Light theme color typically used for text or contrasting elements,
     * represented as white.
     */
    public static final Color LIGHT = new Color(0xFFFFFF);

}
