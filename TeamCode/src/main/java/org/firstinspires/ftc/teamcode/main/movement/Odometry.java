package org.firstinspires.ftc.teamcode.main.movement;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.core.device.single.Gyro;


// coding by Timofei

@Config
public final class Odometry {
    private static final Odometry instance = new Odometry();
    public double ticksX, ticksY, yaw;
    private double oldXOd, oldYOd;
    public static double ticksPerCm = 1;
    private boolean started = false;
    private double yawOffset = 0;
    private double dX = 0;
    private double dY = 0;

    // Сколько лишних тиков набегает при полном обороте робота на месте
//    public static double ticksPerRotX = 0;
//    public static double ticksPerRotY = 0;


    public static Odometry getInstance() {
        return instance;
    }

    private Odometry() {
    }

    // rotates vector clockwise in right-sided coordinate system
    @NonNull
    public static double[] rotateVector(double x, double y, double rot) {
        double cos = Math.cos(rot*Gyro.DEG_TO_RAD), sin = Math.sin(rot*Gyro.DEG_TO_RAD);
        return new double[]{cos * x - sin * y, cos * y + sin * x};
    }

    public void setPosition(double x, double y) {
        ticksX = x;
        ticksY = y;
        started = false;
    }

    public void setPositionCm(double x, double y) {
        setPosition(x, y);
    }

    public void setYaw(double yaw) {
        yawOffset = yaw - this.yaw;
        started = false;
    }

    public void odometryTick() {
        if(!started) {
            yaw = Vehicles.getInstance().getGyroYaw();
            oldXOd = Vehicles.getInstance().getPositionOdometerX();
            oldYOd = Vehicles.getInstance().getPositionOdometerY();
            started = true;
            return;
        }
        double newPosX = Vehicles.getInstance().getPositionOdometerX();
        double newPosY = Vehicles.getInstance().getPositionOdometerY();
        double newYaw = Vehicles.getInstance().getGyroYaw();

        dX = newPosX - oldXOd;
        dY = newPosY - oldYOd;
        double dYaw = newYaw - yaw;
        dX -= dYaw / 360;
        dY -= dYaw / 360;
        double[] deltaPos = rotateVector(dX, dY, newYaw + yawOffset);

        ticksX += deltaPos[0];
        ticksY += deltaPos[1];
        yaw = newYaw;

        oldXOd = newPosX;
        oldYOd = newPosY;
    }

    public double getX() {
        return ticksX;
    }

    public double getY() {
        return ticksY;
    }

    public double getXSpeed() {
        return dX;
    }

    public double getYSpeed() {
        return dY;
    }

    public double getXSpeedCm() {
        return dX;
    }

    public double getYSpeedCm() {
        return dY;
    }

    public double getYaw() {
        return yaw + yawOffset;
    }

}