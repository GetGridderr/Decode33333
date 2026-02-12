/**
 * @author Arsen Berezin
 */

package org.firstinspires.ftc.teamcode.core.trait.device;



/**
 * Interface describing motors.
 *
 * @see IDirectional
 * @see IDevice
 *
 * @see org.firstinspires.ftc.teamcode.core.implementation
 */
public interface IMotor extends IDevice, IDirectional {
    double getPower();
    void setPower(double power);

    void enable();
    void disable();
    boolean isEnabled();


    /**
     * Normalize the power value if it is outside the possible range.
     */
    static double normalizePower(double power) {
        if (power > 1) return 1;
        if (power < -1) return -1;
        return power;
    }
}
