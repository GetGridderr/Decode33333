/**
 * @author Arsen Berezin
 */

package org.firstinspires.ftc.teamcode.core.trait.device;


import org.firstinspires.ftc.teamcode.core.util.pid.PidfCoefficients;

/**
 * Interface describing a motors with encoder.
 *
 * @see IEncoder
 * @see IMotor
 * @see IDevice
 *
 * @see org.firstinspires.ftc.teamcode.core.implementation
 */
public interface IEncoderMotor extends IMotor, IEncoder {
    default double getVelocity() {
        return getEncoderVelocity();
    }

    void setVelocity(double velocity);

    PidfCoefficients getPidfCoefficients();
    void setPidfCoefficients(PidfCoefficients coefficients);
    void setPidfCoefficients(double kP, double kI, double kD, double kF);
}
