package codingwithslinky.noise.vis;

/**
 * The AnimationPanel interface defines the methods required for any panel that
 * supports animated rendering. Implementing classes should provide mechanisms
 * to control the animation speed, detail level, and manage the start and stop
 * of the animation.
 *
 * @author Kheagen Haskins
 */
public interface AnimationPanel {

    // ------------------------------ Getters ------------------------------- //
    /**
     * Gets the current speed of the animation.
     *
     * @return the animation speed
     */
    float getAnimationSpeed();

    /**
     * Gets the current level of detail for the animation.
     *
     * @return the detail level
     */
    float getDetail();

    /**
     * Returns the frames per second, which is the number of times the animation
     * updates per second.
     *
     * @return the frames per second
     */
    int getFPS();

    /**
     * Determines if the animation is currently playing.
     *
     * @return true if the animation is currently playing.
     */
    boolean isRunning();

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the speed of the animation.
     *
     * @param speed the new speed for the animation
     */
    void setAnimationSpeed(float speed);

    /**
     * Sets the level of detail for the animation.
     *
     * @param detail the new level of detail for the animation
     */
    void setDetail(float detail);

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Starts the animation.
     */
    void startAnimation();

    /**
     * Stops the animation.
     */
    void stopAnimation();

}
