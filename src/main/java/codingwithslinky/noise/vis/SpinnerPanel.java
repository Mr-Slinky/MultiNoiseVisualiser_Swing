package codingwithslinky.noise.vis;

import codingwithslinky.noise.vis.color.Palette;
import codingwithslinky.noise.FastNoise;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

/**
 * A panel designed to manage a set of vertical spinners that control the
 * properties of a {@link FastNoise} object. The panel allows for real-time
 * adjustment of various noise parameters, enhancing the noise generation
 * process.
 *
 * <p>
 * The spinners are customised to reflect the dark theme using the
 * {@link Palette} class for color settings.</p>
 *
 * <p>
 * This class extends {@link JPanel} and uses a {@link GridLayout} for
 * organising the spinners in a vertical arrangement.</p>
 *
 * <p>
 * The spinners included in this panel control the following properties of the
 * {@link FastNoise} object:</p>
 * <ul>
 * <li>Fractal Octaves</li>
 * <li>Frequency</li>
 * <li>Fractal Gain</li>
 * <li>Fractal Lacunarity</li>
 * <li>Fractal Ping Pong Strength</li>
 * <li>Fractal Weighted Strength</li>
 * <li>Cellular Jitter</li>
 * </ul>
 *
 * <p>
 * The class includes methods for initialising the components, creating spinners
 * and labels, and binding the spinners to the noise object's properties.</p>
 *
 * @see FastNoise
 * @see Palette
 * @see JSpinner
 * @see JPanel
 * @see GridLayout
 * @see SpinnerNumberModel
 * @see ChangeEvent
 * @see JLabel
 * @see JButton
 * @see JComponent
 * @see UIManager
 * @see BorderFactory
 * @see Component
 *
 * @since 1.0
 * @version 1.0
 *
 * @author Kheagen Haskins
 */
public class SpinnerPanel extends JPanel {

    static {
        UIManager.put("Spinner.foreground", Palette.LIGHT);
        UIManager.put("Spinner.background", Palette.DARK);
    }

    // ------------------------------ Fields -------------------------------- //
    private JSpinner spnOctaves;
    private JSpinner spnFrequency;
    private JSpinner spnFractalGain;
    private JSpinner spnLac;
    private JSpinner spnPingPong;
    private JSpinner spnWeightStrength;
    private JSpinner spnJitter;

    private FastNoise noise;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new SpinnerPanel with the specified FastNoise object.
     *
     * @param noiseGen the FastNoise object to be controlled by this panel
     */
    public SpinnerPanel(FastNoise noiseGen) {
        super(new GridLayout(14, 1), true);
        this.noise = noiseGen;

        initComponents();
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initialises the components of the panel, including the creation of
     * spinners and labels, and binds the spinners to the properties of the
     * FastNoise object.
     */
    private void initComponents() {
        spnOctaves = createSpinner(noise.getFractalOctaves(), 1f, 10f, 1f);
        spnFrequency = createSpinner(noise.getFrequency(), 0.001f, 0.5f, 0.01f);
        spnFractalGain = createSpinner(noise.getFractalGain(), 0f, 1f, 0.01f);
        spnLac = createSpinner(noise.getFractalLacunarity(), 0f, 5f, 0.1f);
        spnPingPong = createSpinner(noise.getFractalPingPongStrength(), 0f, 5f, 0.01f);
        spnWeightStrength = createSpinner(noise.getFractalWeightedStrength(), 0f, 10f, 0.1f);
        spnJitter = createSpinner(noise.getCellularJitter(), 0f, 1f, 0.01f);

        bindSpinnersToNoiseObject();

        add(createLabel("Octaves"));
        add(spnOctaves);
        add(createLabel("Frequency"));
        add(spnFrequency);
        add(createLabel("Fractal Gain"));
        add(spnFractalGain);
        add(createLabel("Lacunarity"));
        add(spnLac);
        add(createLabel("Ping Pong Strength"));
        add(spnPingPong);
        add(createLabel("Fractal Weight Strength"));
        add(spnWeightStrength);
        add(createLabel("Cellular Jitter"));
        add(spnJitter);
    }

    /**
     * Creates a JLabel with the specified text.
     *
     * @param text the text to be displayed by the label
     * @return the created JLabel
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);

        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return label;
    }

    /**
     * Creates a JSpinner with the specified parameters.
     *
     * @param startingValue the initial value of the spinner
     * @param min the minimum value of the spinner
     * @param max the maximum value of the spinner
     * @param stepSize the step size for the spinner
     * @return the created JSpinner
     */
    private JSpinner createSpinner(double startingValue, double min, double max, double stepSize) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(startingValue, min, max, stepSize));

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor) editor;
            defaultEditor.getTextField().setBackground(Palette.DARK);
            defaultEditor.getTextField().setForeground(Palette.LIGHT); 
        }

        // Change the background colour of the spinner buttons
        Component[] components = spinner.getComponents();
        for (Component component : components) {
            if (component instanceof JButton btn) {
                btn.setBackground(Palette.TERTIARY);
                btn.setBorder(BorderFactory.createLineBorder(Palette.TERTIARY));
                btn.setFocusPainted(false);
            }
        }

        spinner.setBorder(BorderFactory.createLineBorder(Palette.TERTIARY, 1));
        return spinner;
    }

    /**
     * Binds the spinners to the properties of the FastNoise object, so that
     * changes in the spinners directly update the corresponding properties in
     * the FastNoise object.
     */
    private void bindSpinnersToNoiseObject() {
        spnOctaves.addChangeListener((ChangeEvent ev) -> {
            noise.setFractalOctaves(((SpinnerNumberModel) spnOctaves.getModel()).getNumber().intValue());
        });

        spnFrequency.addChangeListener((ChangeEvent ev) -> {
            noise.setFrequency(((SpinnerNumberModel) spnFrequency.getModel()).getNumber().floatValue());
        });

        spnFractalGain.addChangeListener((ChangeEvent ev) -> {
            noise.setFractalGain(((SpinnerNumberModel) spnFractalGain.getModel()).getNumber().floatValue());
        });

        spnLac.addChangeListener((ChangeEvent ev) -> {
            noise.setFractalLacunarity(((SpinnerNumberModel) spnLac.getModel()).getNumber().floatValue());
        });

        spnPingPong.addChangeListener((ChangeEvent ev) -> {
            noise.setFractalPingPongStrength(((SpinnerNumberModel) spnPingPong.getModel()).getNumber().floatValue());
        });

        spnWeightStrength.addChangeListener((ChangeEvent ev) -> {
            noise.setFractalWeightedStrength(((SpinnerNumberModel) spnWeightStrength.getModel()).getNumber().floatValue());
        });

        spnJitter.addChangeListener((ChangeEvent ev) -> {
            noise.setCellularJitter(((SpinnerNumberModel) spnJitter.getModel()).getNumber().floatValue());
        });
    }

}