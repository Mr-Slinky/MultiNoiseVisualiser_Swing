package codingwithslinky.noise.vis;

import codingwithslinky.noise.vis.color.Palette;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * ButtonPanel is a custom JPanel that contains buttons for increasing and
 * decreasing the colour resolution of a NoiseVisualizer. It uses a GridLayout
 * to organise the buttons.
 *
 * @author Kheagen Haskins
 */
public class ButtonPanel extends JPanel {

    // Static block to set the UI properties for buttons
    static {
        UIManager.put("Button.background", Palette.TERTIARY);
        UIManager.put("Button.foreground", Palette.DARK);
        UIManager.put("Button.select", Palette.PRIMARY);
    }

    // ------------------------------ Fields -------------------------------- //
    /**
     * The NoiseVisualizer instance whose colour resolution is controlled by
     * this panel.
     */
    private NoiseVisualizer vis;

    /**
     * Button to increase colour resolution.
     */
    private JButton btnColorResUp;

    /**
     * Button to decrease colour resolution.
     */
    private JButton btnColorResDown;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a ButtonPanel with the specified NoiseVisualizer.
     *
     * @param noiseVisualiser the NoiseVisualizer instance to control.
     */
    public ButtonPanel(NoiseVisualizer noiseVisualiser) {
        super(new GridLayout(1, 3), true);
        this.vis = noiseVisualiser;

        initComponents();
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initialises the components of this panel. Creates the buttons and binds
     * their actions.
     */
    private void initComponents() {
        btnColorResUp = createButton("Increase Colour Resolution");
        btnColorResDown = createButton("Decrease Colour Resolution");

        add(wrapButtonPair(btnColorResUp, btnColorResDown));

        bindComponents();
    }

    /**
     * Creates a JButton with the specified text.
     *
     * @param text the text to display on the button.
     * @return the created JButton.
     */
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Wraps two components into a JPanel with a GridLayout.
     *
     * @param btn1 the first component.
     * @param btn2 the second component.
     * @return the JPanel containing the two components.
     */
    private JPanel wrapButtonPair(JComponent btn1, JComponent btn2) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 0), true);

        panel.add(btn1);
        panel.add(btn2);

        return panel;
    }

    /**
     * Binds the action listeners to the buttons. The buttons adjust the colour
     * resolution of the NoiseVisualizer.
     */
    private void bindComponents() {
        btnColorResUp.addActionListener(ev -> adjustColorResolution(true));
        btnColorResDown.addActionListener(ev -> adjustColorResolution(false));
    }

    /**
     * Creates a JLabel with the specified text.
     *
     * @param text the text to display on the label.
     * @return the created JLabel.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);

        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return label;
    }

    /**
     * Adjusts the colour resolution of the NoiseVisualizer.
     *
     * @param increase true to increase the colour resolution, false to decrease
     * it.
     */
    private void adjustColorResolution(boolean increase) {
        if (increase) {
            vis.increaseColorResolution();
        } else {
            vis.decreaseColorResolution();
        }
    }
}