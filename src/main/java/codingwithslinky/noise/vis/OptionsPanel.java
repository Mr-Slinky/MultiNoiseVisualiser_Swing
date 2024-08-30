package codingwithslinky.noise.vis;

import static java.util.Arrays.stream;

import codingwithslinky.noise.vis.color.Palette;
import codingwithslinky.noise.FastNoise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The {@code OptionsPanel} class represents a user interface panel for
 * configuring various noise generation options. It extends {@link JPanel} and
 * includes several combo boxes for selecting noise types and their respective
 * parameters.
 *
 * <p>
 * This class makes extensive use of the {@link FastNoise} library for noise
 * generation and custom UI components for interaction.</p>
 *
 * <p>
 * It includes static initialisation of UI properties for consistent styling and
 * colour scheme using the {@link Palette} class.</p>
 *
 * @see JPanel
 * @see FastNoise
 * @see Palette
 * @see JComboBox
 * @see JLabel
 * @see UIManager
 * @see BorderFactory
 *
 * @since 1.0
 * @version 1.0
 *
 * @author Kheagen Haskins
 */
public class OptionsPanel extends JPanel {

    static {
        // Setting UI properties for consistent styling
        UIManager.put("ComboBox.disabledBackground", Palette.TERTIARY);
        UIManager.put("ComboBox.selectionBackground", Palette.SECONDARY);
        UIManager.put("ComboBox.selectionForeground", Palette.LIGHT);
        UIManager.put("ComboBox.background", Palette.TERTIARY);
        UIManager.put("ComboBox.foreground", Palette.DARK);
    }

    // ------------------------------ Fields -------------------------------- //
    /**
     * Combo box for selecting the noise type.
     */
    private JComboBox<String> noiseTypeOptions = createComboBox(
            stream(FastNoise.NoiseType.values())
                    .map(Enum::name)
                    .toArray(String[]::new)
    );

    /**
     * Combo box for selecting the cellular distance function.
     */
    private JComboBox<String> distanceFunctionOptions = createComboBox(
            stream(FastNoise.CellularDistanceFunction.values())
                    .map(Enum::name)
                    .toArray(String[]::new)
    );

    /**
     * Combo box for selecting the domain warp type.
     */
    private JComboBox<String> warpTypeOptions = createComboBox(
            stream(FastNoise.DomainWarpType.values())
                    .map(Enum::name)
                    .toArray(String[]::new)
    );

    /**
     * Combo box for selecting the fractal type.
     */
    private JComboBox<String> fractalTypeOptions = createComboBox(
            stream(FastNoise.FractalType.values())
                    .map(Enum::name)
                    .toArray(String[]::new)
    );

    /**
     * Combo box for selecting the 3D rotation type.
     */
    private JComboBox<String> rotationTypeOptions = createComboBox(
            stream(FastNoise.RotationType3D.values())
                    .map(Enum::name)
                    .toArray(String[]::new)
    );

    /**
     * Combo box for selecting the cellular return type.
     */
    private JComboBox<String> cellTypeOptions = createComboBox(
            stream(FastNoise.CellularReturnType.values())
                    .map(Enum::name)
                    .toArray(String[]::new)
    );

    /**
     * The {@link FastNoise} object used for generating noise.
     */
    private FastNoise noiseGen;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code OptionsPanel} with the specified
     * {@link FastNoise} object.
     *
     * @param noiseGen the FastNoise object to be controlled by this panel
     */
    public OptionsPanel(FastNoise noiseGen) {
        super(true);
        setPreferredSize(new Dimension(250, 100));
        setBackground(Color.RED); // Set the background colour
        this.noiseGen = noiseGen;
        setLayout(new GridLayout(12, 1));

        initComponents();
        addComponents();
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initializes the components of the panel, including the creation and
     * configuration of combo boxes.
     */
    private void initComponents() {
        // Disable cellular options initially
        triggerCellularTypeSelected(false);

        noiseTypeOptions.addActionListener((ActionEvent ev) -> {
            FastNoise.NoiseType nType = Enum.valueOf(FastNoise.NoiseType.class, (String) noiseTypeOptions.getSelectedItem());
            noiseGen.setNoiseType(nType);

            // Enable or disable cellular options based on the selected noise type
            if (nType == FastNoise.NoiseType.Cellular) {
                triggerCellularTypeSelected(true);
            } else {
                triggerCellularTypeSelected(false);
            }
        });

        distanceFunctionOptions.addActionListener((ActionEvent ev) -> {
            noiseGen.setCellularDistanceFunction(Enum.valueOf(FastNoise.CellularDistanceFunction.class, (String) distanceFunctionOptions.getSelectedItem()));
        });

        warpTypeOptions.addActionListener((ActionEvent ev) -> {
            noiseGen.setDomainWarpType(Enum.valueOf(FastNoise.DomainWarpType.class, (String) warpTypeOptions.getSelectedItem()));
        });

        fractalTypeOptions.addActionListener((ActionEvent ev) -> {
            noiseGen.setFractalType(Enum.valueOf(FastNoise.FractalType.class, (String) fractalTypeOptions.getSelectedItem()));
        });

        rotationTypeOptions.addActionListener((ActionEvent ev) -> {
            noiseGen.setRotationType3D(Enum.valueOf(FastNoise.RotationType3D.class, (String) rotationTypeOptions.getSelectedItem()));
        });

        cellTypeOptions.addActionListener((ActionEvent ev) -> {
            noiseGen.setCellularReturnType(Enum.valueOf(FastNoise.CellularReturnType.class, (String) cellTypeOptions.getSelectedItem()));
        });
    }

    /**
     * Adds the components (labels and combo boxes) to the panel.
     */
    private void addComponents() {
        add(createLabel("Noise Type"));
        add(noiseTypeOptions);

        add(createLabel("Warp Type"));
        add(warpTypeOptions);

        add(createLabel("Fractal Types"));
        add(fractalTypeOptions);

        add(createLabel("Rotation Type"));
        add(rotationTypeOptions);

        add(createLabel("Cell Type"));
        add(cellTypeOptions);

        add(createLabel("Distance Function"));
        add(distanceFunctionOptions);
    }

    /**
     * Creates a combo box with the specified items.
     *
     * @param items the items to be displayed in the combo box
     * @return the created combo box
     */
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> optionsList = new JComboBox<>(items);
        optionsList.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        return optionsList;
    }

    /**
     * Creates a JLabel with the specified text.
     *
     * @param text the text to be displayed by the label
     * @return the created JLabel
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        int fontSize = label.getFont().getSize();

        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setMaximumSize(new Dimension(Short.MAX_VALUE, fontSize + 5));
        label.setMinimumSize(new Dimension(Short.MAX_VALUE, fontSize));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));

        return label;
    }

    /**
     * Enables or disables the cellular type and distance function combo boxes
     * based on the specified flag.
     *
     * @param enabled true to enable the combo boxes, false to disable them
     */
    private void triggerCellularTypeSelected(boolean enabled) {
        cellTypeOptions.setEnabled(enabled);
        distanceFunctionOptions.setEnabled(enabled);
    }

}