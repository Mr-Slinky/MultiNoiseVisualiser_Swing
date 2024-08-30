package codingwithslinky.noise.vis;

import codingwithslinky.noise.vis.color.Palette;
import codingwithslinky.noise.FastNoise;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * The {@code MainInterface} class represents the main user interface panel for
 * controlling and visualising noise generation. It extends {@link JPanel} and
 * includes components for adjusting noise parameters, visualising the noise,
 * and other options.
 *
 * <p>
 * This class makes extensive use of the {@link FastNoise} library for noise
 * generation and custom UI components such as
 * {@link SpinnerPanel}, {@link NoiseVisualizer}, and {@link OptionsPanel} for
 * interaction and display.</p>
 *
 * <p>
 * It includes static initialisation of UI properties for consistent styling and
 * colour scheme using the {@link Palette} class.</p>
 *
 * @see JPanel
 * @see FastNoise
 * @see SpinnerPanel
 * @see NoiseVisualizer
 * @see OptionsPanel
 * @see Palette
 * @see UIManager
 * @see BoxLayout
 * @see GridLayout
 * @see BorderFactory
 *
 * @since 1.0
 * @version 1.0
 *
 * @author Kheagen Haskins
 */
public class MainInterface extends JPanel {

    // ------------------------------ Static -------------------------------- //
    /**
     * The font used for various UI components within this panel.
     */
    private static final Font FONT = new Font("Courier New", Font.PLAIN, 15);

    /**
     * The margin used for the borders of various components within this panel.
     */
    private static final int MARGIN = 10;

    static {
        // Set the locale to US so that is uses periods for decimal points
        Locale.setDefault(Locale.US);

        // Fonts
        UIManager.put("Spinner.font", FONT);
        UIManager.put("Button.font", FONT);
        UIManager.put("Label.font", FONT);
        UIManager.put("ComboBox.font", FONT);

        // Colours
        UIManager.put("Panel.foreground", Palette.LIGHT);
        UIManager.put("Label.foreground", Palette.LIGHT);
        UIManager.put("Panel.background", Palette.DARK);
        UIManager.put("Label.background", Palette.DARK);
    }

    // ------------------------------ Fields -------------------------------- // 
    /**
     * The noise generator object used for producing noise values.
     */
    private FastNoise noiseGen;

    /**
     * The panel containing spinners for adjusting noise parameters.
     */
    private SpinnerPanel spinners;

    /**
     * The panel for visualising the generated noise.
     */
    private NoiseVisualizer vis;

    /**
     * The panel containing additional options for the noise generation.
     */
    private OptionsPanel options;
    
    /**
     * 
     */
    private ButtonPanel buttons;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code MainInterface} with the specified
     * {@link FastNoise} object.
     *
     * @param noise the FastNoise object to be controlled by this interface
     */
    public MainInterface(FastNoise noise) {
        super(true);
        setLayout(new BorderLayout());
        this.noiseGen = noise;
        initComponents();

        vis.startAnimation();
    }

    /**
     * Constructs a new {@code MainInterface} with a global {@link FastNoise}
     * object.
     */
    public MainInterface() {
        this(FastNoise.getGlobalNoiseObject());
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initialises the components of the interface, including the creation and
     * configuration of the spinner panel, noise visualiser, and options panel.
     */
    private void initComponents() {
        Dimension panelSize = new Dimension(250, 300);
        vis = new NoiseVisualizer(
                panelSize.width, panelSize.height,
                noiseGen,
                Palette.PRIMARY, Palette.SECONDARY, Palette.TERTIARY
        );
        
        spinners = new SpinnerPanel(noiseGen);
        options = new OptionsPanel(noiseGen);
        buttons = new ButtonPanel(vis);
        
        spinners.setBorder(panelBorder());
        options.setBorder(panelBorder());
        buttons.setBorder(panelBorder());

        spinners.setBackground(Palette.DARK);
        options.setBackground(Palette.DARK);
        buttons.setBackground(Palette.DARK);

        // Sizes the internal components
        spinners.setPreferredSize(panelSize);
        options.setPreferredSize(panelSize);
        buttons.setPreferredSize(new Dimension(
                panelSize.width * 3,
                50
        ));

        add(spinners, BorderLayout.WEST);
        add(borderWrap(vis), BorderLayout.CENTER);
        add(options, BorderLayout.EAST);
        add(buttons, BorderLayout.SOUTH);

        setMaximumSize(new Dimension(750, 300));
    }

    /**
     * Creates an empty border with a specified margin.
     *
     * @return a {@link Border} with the specified margin
     */
    private Border panelBorder() {
        return BorderFactory.createEmptyBorder(MARGIN/2, MARGIN, MARGIN/2, MARGIN);
    }

    /**
     * Wraps a given component with a bordered panel.
     *
     * @param comp the component to be wrapped
     * @return a {@link JPanel} containing the wrapped component
     */
    private JPanel borderWrap(JComponent comp) {
        JPanel wrapper = new JPanel(new GridLayout());
        wrapper.setBorder(BorderFactory.createLineBorder(Palette.TERTIARY, 2));
        wrapper.add(comp);
        return wrapper;
    }

    /**
     * Toggles the animation state of the noise visualiser. Starts the animation
     * if it is not running, and stops it if it is running.
     */
    private void toggleAnimation() {
        if (vis.isRunning()) {
            vis.stopAnimation();
        } else {
            vis.startAnimation();
        }
    }

}