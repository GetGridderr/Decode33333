/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 * @author Semen Scherbina
 */


package org.firstinspires.ftc.teamcode.core.util.pid;


import org.firstinspires.ftc.teamcode.core.util.Constants;


/**
 * Class for PID Controller.
 */
public class PidfController {
    protected double target = 0;
    protected double lastInput = 0;
    protected boolean first = true;

    protected double integral = 0;
    protected double integralLimit = Double.POSITIVE_INFINITY;

    protected double lastNanoTimeStamp = System.nanoTime();

    public double kP = 0;
    public double kD = 0;
    public double kI = 0;
    public double kF = 0;


    public PidfController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public PidfController(double kP, double kI, double kD, double kF) {
        this(kP, kI, kD);
        this.kF = kF;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getIntegralLimit() {
        return integralLimit;
    }

    public void setIntegralLimit(double integral) {
        integralLimit = Math.abs(integral);
    }

    public double update(double input, double target) {
        final double error = target - input;

        final double currentNanoTimeStamp = System.nanoTime();
        final double dT =
                (currentNanoTimeStamp - lastNanoTimeStamp) * Constants.SECONDS_PER_NANOSECOND;
        lastNanoTimeStamp = currentNanoTimeStamp;

        final double derivative;
        if (!first) {
            integral += error * kI * dT;
            if (Math.abs(integral) > integralLimit) {
                integral = integralLimit * Math.signum(integral);
            }
            derivative = (lastInput - input) / dT;
        } else {
            derivative = 0;
            first = false;
        }

        lastInput = input;

        return kP * (error + integral + (kD * derivative)) + (target * kF);
    }

    public double update(double input) {
        return update(input, target);
    }
}
