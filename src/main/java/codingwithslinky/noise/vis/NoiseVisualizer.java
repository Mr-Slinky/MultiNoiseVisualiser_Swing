package codingwithslinky.noise.vis;

import codingwithslinky.noise.vis.color.ColorMap;
import codingwithslinky.noise.FastNoise;
import static codingwithslinky.noise.vis.color.ColorUtil.toColors;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

/**
 * The {@code NoiseVisualizer} class represents a custom Swing component that
 * displays a dynamic, animated background inspired by the flowing motion of a
 * traditional lava lamp. It extends {@code JPanel} and implements
 * {@code AnimationPanel}, enabling smooth integration into Java Swing
 * applications. The animation is achieved using 3D Perlin noise, provided by
 * the {@link codingwithslinky.noise.FastNoise} class, which is used to generate
 * evolving noise patterns over time.
 *
 * <p>
 * The class offers a variety of features:
 * <ul>
 * <li><b>Configurable Animation Speed:</b> Adjust the speed at which the noise
 * evolves over time, influencing the perceived motion of the animated
 * background.</li>
 * <li><b>Detail Level:</b> Control the granularity of the noise patterns,
 * allowing for fine-tuned visual detail. Higher detail levels produce more
 * intricate patterns.</li>
 * <li><b>Colour Customisation:</b> Support for user-defined colour palettes,
 * enabling the creation of unique and visually appealing effects. The class
 * provides a default set of colours, but users can specify custom colours
 * either via {@code Color} objects or hexadecimal values.</li>
 * </ul>
 *
 * <p>
 * The {@code NoiseVisualizer} can be easily incorporated into any Swing-based
 * UI, offering a visually engaging backdrop for user interfaces, decorative
 * panels, or interactive visualisations. The component is designed to be
 * responsive and adaptable, with easy-to-use methods for starting and stopping
 * the animation, adjusting the animation parameters, and setting the
 * component's preferred size.
 * <p>
 * Example usage:
 * <pre>
 * AnimationPanel noiseVis = new NoiseVisualizer(
 *      500, 500,
 *      CYAN, MAGENTA, YELLOW // Colors to use
 * );
 *
 * noiseVis.setDetail(5);
 * noiseVis.setAnimationSpeed(10);
 * noiseVis.startAnimation();
 *
 * GUILauncher.display(noiseVis);
 * </pre>
 *
 * @see codingwithslinky.noise.FastNoise
 * @see codingwithslinky.noise.vis.color.ColorMap
 * @since 1.0
 *
 * @author Kheagen Haskins
 */
public class NoiseVisualizer extends JPanel implements AnimationPanel {

    // ------------------------------ Static -------------------------------- //
    /**
     * Default colours to use if a constructor is not provided with its own
     * default colours
     */
    private static final Color[] DEFAULT_COLORS = {Color.BLACK, Color.WHITE};

    // ------------------------------ Fields -------------------------------- //
    /**
     * The z-slice of the noise cube
     */
    private float z = 0; // used below

    /**
     * The FPS
     */
    private final int fps = 50;

    /**
     * The speed at which the z-axis value is incremented, affecting the speed
     * of the animation.
     */
    private float speed = 1f;

    /**
     * The level of detail in the generated noise. Higher values result in more
     * detailed images.
     */
    private float detail = 1f;

    /**
     * The timer responsible for triggering the repaint of the component at
     * regular intervals.
     */
    private Timer animationLoop;

    /**
     * A cached color map to improve performance and reduce the constant need to
     * interpolate between colors. Also allows dynamic resolution adjustment.
     */
    private ColorMap colorMap;

    /**
     * The random noise function; highly customisable
     */
    protected FastNoise noiseGen;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code NoiseVisualizer} with specified width, height,
     * and custom colours. This constructor allows for full customisation of the
     * component's size and colour palette. The animation will use the provided
     * colours to create gradients.
     *
     * @param width the width of the {@code NoiseVisualizer} in pixels.
     * @param height the height of the {@code NoiseVisualizer} in pixels.
     * @param noiseGen the FastNoise object used to generate the noise.
     * @param colors an array of {@code Color} objects to be used in the
     * gradient. The colours are interpolated to create the animated effect.
     * @throws IllegalArgumentException if the width or height are non-positive,
     * or if no colours are provided.
     */
    public NoiseVisualizer(int width, int height, FastNoise noiseGen, Color... colors) {
        init(width, height, noiseGen, colors);
    }

    /**
     * Constructs a new {@code NoiseVisualizer} with specified width, height,
     * and custom colours using hexadecimal colour values. This constructor
     * allows the use of hexadecimal values for colour specification, offering
     * flexibility in defining the component's colour palette.
     *
     * @param width the width of the {@code NoiseVisualizer} in pixels.
     * @param height the height of the {@code NoiseVisualizer} in pixels.
     * @param hexs an array of integer values representing colours in
     * hexadecimal format. Each integer should be a valid hex colour code, such
     * as {@code 0xFF5733}.
     * @throws IllegalArgumentException if the width or height are non-positive,
     * or if no hexadecimal colours are provided.
     */
    public NoiseVisualizer(int width, int height, int... hexs) {
        init(width, height, FastNoise.getGlobalNoiseObject(), toColors(hexs));
    }

    /**
     * Constructs a new {@code NoiseVisualizer} with a default size of 250x250
     * pixels and custom colours. This constructor simplifies the creation of
     * the component by automatically setting the size to a standard value while
     * allowing custom colour input.
     *
     * @param colors an array of {@code Color} objects to be used in the
     * gradient. The colours are interpolated to create the animated effect.
     * @throws IllegalArgumentException if no colours are provided.
     */
    public NoiseVisualizer(Color... colors) {
        init(250, 250, FastNoise.getGlobalNoiseObject(), colors);
    }

    /**
     * Constructs a new {@code NoiseVisualizer} with a default size of 250x250
     * pixels and custom colours using hexadecimal colour values. This
     * constructor allows for easy specification of the component's colour
     * palette using hex values.
     *
     * @param hexs an array of integer values representing colours in
     * hexadecimal format. Each integer should be a valid hex colour code, such
     * as {@code 0xFF5733}.
     * @throws IllegalArgumentException if no hexadecimal colours are provided.
     */
    public NoiseVisualizer(int... hexs) {
        init(250, 250, FastNoise.getGlobalNoiseObject(), toColors(hexs));
    }

    /**
     * Constructs a new {@code NoiseVisualizer} with specified width and height,
     * using the default colour palette. This constructor is useful for creating
     * a standard-sized component with a predefined set of colours.
     *
     * @param width the width of the {@code NoiseVisualizer} in pixels.
     * @param height the height of the {@code NoiseVisualizer} in pixels.
     * @throws IllegalArgumentException if the width or height are non-positive.
     */
    public NoiseVisualizer(int width, int height) {
        init(width, height, FastNoise.getGlobalNoiseObject(), DEFAULT_COLORS);
    }

    /**
     * Constructs a new {@code NoiseVisualizer} with a default size of 250x250
     * pixels and the default colour palette. This constructor provides the
     * simplest way to create a {@code NoiseVisualizer} component, using
     * standard dimensions and colours.
     */
    public NoiseVisualizer() {
        init(250, 250, FastNoise.getGlobalNoiseObject(), DEFAULT_COLORS);
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Gets the current speed of the animation.
     *
     * @return the animation speed
     */
    @Override
    public float getAnimationSpeed() {
        return speed;
    }

    /**
     * Gets the current level of detail for the animation.
     *
     * @return the detail level
     */
    @Override
    public float getDetail() {
        return detail;
    }

    /**
     * Returns the frames per second.
     *
     * @return the frames per second
     */
    @Override
    public int getFPS() {
        return fps;
    }

    /**
     * Determines if the animation is currently playing.
     *
     * @return true if the animation is currently playing.
     */
    @Override
    public boolean isRunning() {
        return animationLoop.isRunning();
    }

    /**
     * Exposes the color map
     *
     * @return The color map
     */
    public ColorMap getColorMap() {
        return colorMap;
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * sets the speed of the animation.
     *
     * @param zSpeed the new speed for the animation
     */
    @Override
    public void setAnimationSpeed(float zSpeed) {
        this.speed = zSpeed;
    }

    /**
     * sets the level of detail for the animation.
     *
     * @param detail the new level of detail for the animation
     */
    @Override
    public void setDetail(float detail) {
        this.detail = detail;
    }

    /**
     * Sets the color map to be used for painting pixels. The color map
     * determines the colors associated with different noise values.
     *
     * @param colorMap the {@code ColorMap} object to set
     */
    public void setColorMap(ColorMap colorMap) {
        this.colorMap = colorMap;
    }

    /**
     * Sets the resolution of the internal colour map. The resolution determines
     * the number of critical indices that can be accessed in the colour map.
     * This method delegates the resolution setting to the internal
     * {@code ColorMap} instance.
     *
     * @param resolution the new resolution value. It must be between 1 and the
     * maximum size of the colour map (256).
     */
    public void setColorMapResolution(int resolution) {
        colorMap.setResolution(resolution);
    }

    /**
     * Sets the noise generator to be used for generating noise values. The
     * noise generator determines the type and characteristics of the noise.
     *
     * @param noiseGenerator the {@code FastNoise} object to set
     */
    public void setNoiseGenerator(FastNoise noiseGenerator) {
        this.noiseGen = noiseGenerator;
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Paints the component by drawing a gradient background image based on the
     * current z-axis value and the noise function. This method is called by the
     * Swing framework whenever the component needs to be rendered.
     *
     * @param g the Graphics object used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintPixels(g, getWidth(), getHeight());
    }

    /**
     * Starts the animation by starting the timer, which triggers the component
     * to repaint at regular intervals.
     */
    @Override
    public void startAnimation() {
        animationLoop.start();
    }

    /**
     * Stops the animation by stopping the timer, which pauses the repainting of
     * the component.
     */
    @Override
    public void stopAnimation() {
        animationLoop.stop();
    }

    /**
     * Increases the colour resolution of the {@code colorMap}. This method
     * delegates the action to the {@code increaseResolution} method of the
     * {@code colorMap} object.
     */
    public void increaseColorResolution() {
        colorMap.increaseResolution();
    }

    /**
     * Decreases the colour resolution of the {@code colorMap}. This method
     * delegates the action to the {@code decreaseResolution} method of the
     * {@code colorMap} object.
     */
    public void decreaseColorResolution() {
        colorMap.decreaseResolution();
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initialises {@code NoiseVisualizer} with specified width, height, and
     * custom colours.
     *
     * @param width the width of the {@code NoiseVisualizer} in pixels.
     * @param height the height of the {@code NoiseVisualizer} in pixels.
     * @param noiseGen the FastNoise object used to generate the noise.
     * @param colors an array of {@code Color} objects to be used in the
     * gradient. The colours are interpolated to create the animated effect.
     * @throws IllegalArgumentException if the width or height are non-positive,
     * or if no colours are provided.
     */
    private void init(int width, int height, FastNoise noiseGen, Color... colors) {
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        configureMouseWheelListener();

        this.noiseGen = noiseGen;
        this.colorMap = new ColorMap((colors == null || colors.length == 0) ? DEFAULT_COLORS : colors);
        this.animationLoop = new Timer(1000 / fps, ev -> repaint());
    }

    /**
     * Paints the pixels on the given {@code Graphics} object using noise
     * values. The noise values are generated by the noise generator and are
     * used to determine the color of each pixel based on the current noise
     * settings and the color map.
     *
     * @param g the {@code Graphics} object to draw on
     * @param width the width of the area to paint
     * @param height the height of the area to paint
     */
    private void paintPixels(Graphics g, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                g.setColor(colorMap.getColorAt(getNoise(x, y)));
                g.drawRect(x, y, 1, 1);
            }
        }

        z += speed;
    }

    /**
     * Generates a noise value for the given coordinates, normalising or
     * adjusting the value based on the type of noise function used.
     *
     * @param x The x-coordinate for noise generation.
     * @param y The y-coordinate for noise generation.
     * @return A noise value between 0 and 1. If the noise type is not Cellular,
     * the value is normalised to be between 0 and 1. If the noise type is
     * Cellular, the absolute value of the noise is returned.
     */
    private float getNoise(float x, float y) {
        float noise = noiseGen.getNoise(x * detail, y * detail, z);
        if (noiseGen.getNoiseType() != FastNoise.NoiseType.Cellular) {
            noise = (noise + 1) / 2; // Normalises the value to be between 0 and 1
        } else {
            // Cellular noise function often give negative values (not sure why).
            // so we just flip them to positive here
            noise = Math.abs(noise);
        }
        return noise;
    }

    /**
     * Creates a gradient background image based on the current z-axis value and
     * noise function. The image is generated pixel by pixel, with each pixel's
     * colour determined by the noise value at that position.
     *
     * @param sliceIndex the current z-axis value for the cached noise
     * @return a BufferedImage representing the gradient background
     */
    private BufferedImage getNextBackgroundSlice() {
        int height = getHeight();
        int width = getWidth();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = colorMap.getColorAt(getNoise(x, y)).getRGB();
                image.setRGB(x, y, color);
            }
        }

        z += speed;

        return image;
    } // Kept for future use in a cached versions of this class

    // -------------------- Mouse Wheel Configuration ----------------------- //
    /**
     * The current sensitivity of the mouse wheel.
     */
    private float sensitivity = 16;

    /**
     * The maximum sensitivity that can be set for the mouse wheel.
     */
    private float maxSensitivity = 18;

    /**
     * The minimum sensitivity that can be set for the mouse wheel.
     */
    private float minSensitivity = 2;

    /**
     * The minimum level of detail adjustment.
     */
    private float minDetail = 0.01f;

    /**
     * The maximum level of detail adjustment.
     */
    private float maxDetail = 10f;

    /**
     * Configures the mouse wheel listener to adjust sensitivity based on wheel
     * rotation. Requests focus for the window to ensure it receives mouse wheel
     * events.
     */
    private void configureMouseWheelListener() {
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent ev) {
                int rotation = ev.getWheelRotation();
                if (rotation < 0) {
                    increaseSensitivity();
                } else if (rotation > 0) {
                    decreaseSensitivity();
                }
            }
        });
    }

    /**
     * Increases the sensitivity of the mouse wheel, ensuring it does not exceed
     * the maximum sensitivity. Adjusts the detail level downwards
     * proportionally.
     */
    private void increaseSensitivity() {
        detail = Math.max(minDetail, detail - (detail / sensitivity));
        sensitivity = Math.min(maxSensitivity, sensitivity + 0.1f);
    }

    /**
     * Decreases the sensitivity of the mouse wheel, ensuring it does not go
     * below the minimum sensitivity. Adjusts the detail level upwards
     * proportionally.
     */
    private void decreaseSensitivity() {
        detail = Math.min(maxDetail, detail + ((detail / sensitivity)));
        sensitivity = Math.max(minSensitivity, sensitivity - 0.1f);
    }

}
