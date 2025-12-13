package org.firstinspires.ftc.teamcode.main.modules.gun;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;

import org.firstinspires.ftc.teamcode.core.device.motor.FFEncoderMotor;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;

import org.firstinspires.ftc.teamcode.core.device.sensor.SensorVoltage;
import org.firstinspires.ftc.teamcode.main.opencv.AprilTag;

// coding by Matvey Ivanovv

public final class GunControl implements Initializable {
    private static final GunControl INSTANCE = new GunControl();
    private final FFEncoderMotor motorLeft;
    private final Servomotor servo;

    public GunControl() {
        motorLeft = new FFEncoderMotor("gun_motor_left");
        servo = new Servomotor("servo_turn_tower");
    }

    public static GunControl getInstance() { return INSTANCE; }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        motorLeft.initialize(hardwareMap);
        SensorVoltage.getInstance().initialize(hardwareMap);
        servo.initialize(hardwareMap);
        SensorVoltage.getInstance().initialize(hardwareMap);
    }

    @Override
    public boolean isInitialized() { return motorLeft.isInitialized() && servo.isInitialized() && SensorVoltage.getInstance().isInitialized(); }

    public double getVelocity() {
        return gunConfig.velocity;
    }

    public void setVelocity(double vel) {
        gunConfig.velocity = vel;
    }

    public void startShot() {
        FtcDashboard.getInstance().getTelemetry().update();
        motorLeft.setPIDCoefficients(gunConfig.gunPid);
        motorLeft.setAlpha(gunConfig.alpha);
        motorLeft.setSpeedMul(gunConfig.spdMul);
        motorLeft.setSpeed(gunConfig.velocity);  // sensorVoltage.calculateCoefficientVoltage(velocity)
        motorLeft.speedTick();
        FtcDashboard.getInstance().getTelemetry().addData("Speed gun", GunControl.getInstance().getSpeedGun());
        FtcDashboard.getInstance().getTelemetry().update();
    }

    public void stopShot() { motorLeft.setPower(0); }

    public void setTowerDegree(double degree) {
        gunConfig.degreeTower = degree;
        servo.setServoPosition(degree);
    }


    public double getSpeedGun() {
        return motorLeft.getEncoderSpeed();
    }

    public double getTowerDegree() {
        return gunConfig.degreeTower;
    }

    public void aimToAprilTag() {
        double bearing = AprilTag.getInstance().getBearing();
        double servoPos = servo.getCurrentDegree();
        

        double kP = 0.005;

        double correction = bearing * kP;

        double newPosition = servoPos + correction;
        newPosition = Math.max(0.0, Math.min(1.0, newPosition));


        servo.setServoPosition(newPosition);
    }
}