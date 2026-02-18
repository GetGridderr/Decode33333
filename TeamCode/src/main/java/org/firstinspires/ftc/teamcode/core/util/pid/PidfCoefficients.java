package org.firstinspires.ftc.teamcode.core.util.pid;


public class PidfCoefficients {
    public double kP;
    public double kI;
    public double kD;
    public double kF;


    public PidfCoefficients(double kP, double kI, double kD, double kF) {
        setAll(kP, kI, kD, kF);
    }

    public void setAll(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public void setPid(double kP, double kI, double kD) {
        setAll(kP, kI, kD, kF);
    }
}
