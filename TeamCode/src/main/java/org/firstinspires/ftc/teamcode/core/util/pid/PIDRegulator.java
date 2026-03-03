package org.firstinspires.ftc.teamcode.core.util.pid;

public class PIDRegulator {
    private double KP = 0;
    private double KD = 0;
    private double KI = 0;
    public double setpoint = 0;
    public double input = 0;
    private double integral_err = 0;
    private double old_err = 0;

    public PIDRegulator(PIDCoefficients pidCoefficients) {
        this.KP = pidCoefficients.getKP();
        this.KI = pidCoefficients.getKI();
        this.KD = pidCoefficients.getKD();
    }

    public PIDRegulator(double KP, double KI, double KD) {
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
    }
    public PIDRegulator(double KP, double KI, double KD, double setpoint) {
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
        this.setpoint = setpoint;
    }

    public void setCoefficients(double KP, double KI, double KD) {
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
    }

    public void setSetpoint(double setpoint) { this.setpoint = setpoint; }

    public void setCoefficients(PIDCoefficients pidCoefficients) {
        this.KP = pidCoefficients.getKP();
        this.KI = pidCoefficients.getKI();
        this.KD = pidCoefficients.getKD();
    }

    public double PIDGet(double input, double setpoint) {
        double err = setpoint - input;
        this.input = input;
        double d = err - this.old_err;
        double i = this.integral_err;
        double integralLimit = 1000;
        this.integral_err += err;
        this.integral_err = Math.max(-integralLimit, Math.min(integralLimit, this.integral_err));
        this.old_err = err;
        this.integral_err += err;
        return err * this.KP + d * this.KD + i * this.KI;
    }
    public double PIDGet(double input) {
        double err = this.setpoint - input;
        this.input = input;
        double d = err - this.old_err;
        double i = this.integral_err;
        double integralLimit = 1000;
        this.integral_err += err;
        this.integral_err = Math.max(-integralLimit, Math.min(integralLimit, this.integral_err));
        this.old_err = err;
        this.integral_err += err;
        return err * this.KP + d * this.KD + i * this.KI;
    }

    public double getOldErr() {
        return old_err;
    }
}