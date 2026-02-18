/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 */

package org.firstinspires.ftc.teamcode.core.implementation;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.trait.device.IEncoderMotor;
import org.firstinspires.ftc.teamcode.core.util.exception.UnimplementedException;
import org.firstinspires.ftc.teamcode.core.util.pid.PidfCoefficients;
import org.firstinspires.ftc.teamcode.core.util.pid.PidfController;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.WebConfig;


/**
 * Just motor with an encoder.
 *
 * @see IEncoderMotor
 *
 * @see Encoder
 * @see Motor
 */
public class EncoderMotor extends Motor implements IEncoderMotor {
    /**
     * Encoder offset relative to its standard zero position
     * that may be non-zero after device initialization.
     *
     * @implNote
     *  Should only be set once, not counting the constructor,
     *  and only inside {@link EncoderMotor#initialize(HardwareMap)} method
     */
    protected int encoderOffset;
    protected PidfController velocityPidf;


    public EncoderMotor(String name) {
        this(name, 0, 0, 0, 0);
    }

    public EncoderMotor(String name, double kP, double kI, double kD, double kF) {
        super(name);
        encoderOffset = 0;
        velocityPidf = new PidfController(kP, kI, kD, kF);
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        assert hardwareMap != null;

        super.initialize(hardwareMap);

        /*
         * We shouldn't multiply current position
         * by direction sign, because in getEncoderPosition
         * we will firstly subtract offset from raw position/velocity
         * and then multiply by direction
         */
        encoderOffset = device.getCurrentPosition();
        resetEncoder();
    }

    @Override
    public void setVelocity(double target) {
        assert isInitialized();

        double pwr = velocityPidf.update(getEncoderVelocity(), target);
        if(target != 0) pwr += RobotConfig.gB;
        if (Math.signum(pwr) != Math.signum(target)) pwr = 0;
        setPower(pwr * Robot.getVoltageCorrection());
    }

    public void setPidfCoefficients(double kP, double kI, double kD, double kF) {
        velocityPidf.coefficients.setAll(kP, kI, kD, kF);
    }

    public void setPidfCoefficients(PidfCoefficients coefficients) {
        velocityPidf.coefficients = coefficients;
    }

    public PidfCoefficients getPidfCoefficients() {
        return velocityPidf.coefficients;
    }

    @Override
    public int getEncoderPosition() {
        assert isInitialized();
        return (device.getCurrentPosition() - encoderOffset) * direction.getSign();
    }

    @Override
    public double getEncoderVelocity() {
        assert isInitialized();
        return device.getVelocity();
    }

    @Override
    public void resetEncoder() {
        assert isInitialized();

        device.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        device.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
