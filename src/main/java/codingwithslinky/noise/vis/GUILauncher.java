package codingwithslinky.noise.vis;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * The {@code GUILauncher} class provides utility methods for displaying Swing
 * components in a JFrame window. It leverages the Event Dispatch Thread to
 * ensure that Swing components are created and updated in a thread-safe manner.
 * <p>
 * This class supports enabling OpenGL rendering to improve performance and
 * visual quality by setting the system property {@code sun.java2d.opengl}.
 * </p>
 * <p>
 * Example usage:
 * <pre>{@code
 JPanel panel = new JPanel();
 panel.add(new JLabel("Hello, World!"));
 GUILauncher.display(panel);
 }</pre>
 * </p>
 * <p>
 * The class contains two main methods:
 * <ul>
 * <li>{@link #display(JComponent)} - A public method to display a given
 * {@code JComponent} in a JFrame window.</li>
 * <li>{@link #createAppWindow(JComponent)} - A private helper method that
 * creates and configures the JFrame window.</li>
 * </ul>
 * </p>
 * <p>
 * Note: This class cannot be instantiated as it only contains static methods.
 * </p>
 *
 * @see javax.swing.JComponent
 * @see javax.swing.JFrame
 * @see java.awt.EventQueue
 * @see java.awt.HeadlessException
 *
 * @author Kheagen Haskins
 */
public final class GUILauncher {
    
    private static final String TITLE = "2D Multi-Noise Visualiser (Procedural Texture Generator)";
    
    private GUILauncher(){} // Prevent instantiation
    
    /**
     * Displays the specified {@code JComponent} in a JFrame window.
     * <p>
     * This method sets the system property {@code sun.java2d.opengl} to enable
     * OpenGL rendering, and then schedules the creation and display of the
     * JFrame window on the Event Dispatch Thread.
     * </p>
     *
     * @param contentPane the {@code JComponent} to display in the window
     * @throws NullPointerException if {@code contentPane} is null
     */
    public static void display(JComponent contentPane) {
        if (contentPane == null) {
            throw new NullPointerException("contentPane cannot be null");
        }

        System.setProperty("sun.java2d.opengl", "true");
        // Launches the app on the Event Dispatch Thread
        EventQueue.invokeLater(() -> createAppWindow(contentPane).setVisible(true));
    }

    /**
     * Creates and configures a JFrame window with the specified content pane.
     * <p>
     * The created JFrame is set to exit the application when closed, is not
     * resizable, and is centered on the screen. The content pane is packed to
     * fit the preferred size of its components.
     * </p>
     *
     * @param contentPane the {@code JComponent} to set as the content pane of
     * the window
     * @return the configured {@code JFrame} instance
     * @throws HeadlessException if GraphicsEnvironment.isHeadless() returns
     * true
     */
    private static JFrame createAppWindow(JComponent contentPane) throws HeadlessException {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.pack(); // Sizes the JFrame according to the given content pane
        frame.setLocationRelativeTo(null); // Centres the JFrame in the middle of the monitor
        frame.setResizable(false); // Disables the ability to resize the application

        return frame;
    }
    
}