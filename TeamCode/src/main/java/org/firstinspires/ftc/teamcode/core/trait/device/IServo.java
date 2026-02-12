/**
 * @author Arsen Berezin
 */

package org.firstinspires.ftc.teamcode.core.trait.device;


/**
 * Interface describing servomotors.
 *
 * @see IDevice
 *
 * @see org.firstinspires.ftc.teamcode.core.implementation
 */
public interface IServo extends IDevice {
    double getPosition();
    void setPosition(double position);

    /**
     * Normalize the position value if it is outside the possible range.
     */
    static double normalizePosition(double position) {
        if (position > 1) return 1;
        if (position < 0) return 0;
        return position;
    }
}
