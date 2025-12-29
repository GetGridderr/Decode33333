package org.firstinspires.ftc.teamcode.main.modules.gun;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;

import org.firstinspires.ftc.teamcode.core.device.motor.FFEncoderMotor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;

import org.firstinspires.ftc.teamcode.core.device.sensor.SensorVoltage;
import org.firstinspires.ftc.teamcode.main.opencv.AprilTag;

// coding by Matvey Ivanovv

public final class GunControl implements Initializable {
    private static final GunControl INSTANCE = new GunControl();
    private final FFEncoderMotor motorLeft;
    private final Servomotor servo;
    private final Servomotor servoAngle;

    public GunControl() {
        motorLeft = new FFEncoderMotor("gun_motor_left");
        servo = new Servomotor("servo_turn_tower");
        servoAngle = new Servomotor("servo_angle_gun");
    }

    public static GunControl getInstance() { return INSTANCE; }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        motorLeft.initialize(hardwareMap);
        SensorVoltage.getInstance().initialize(hardwareMap);
        servo.initialize(hardwareMap);
        servoAngle.initialize(hardwareMap);
        SensorVoltage.getInstance().initialize(hardwareMap);
        motorLeft.invertDirection();
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
        motorLeft.setPIDCoefficients(gunConfig.kP, gunConfig.kI, gunConfig.kD);
        motorLeft.setAlpha(gunConfig.alpha);
        motorLeft.setSpeedMul(gunConfig.spdMul);
        motorLeft.setSpeed(gunConfig.velocity);  // sensorVoltage.calculateCoefficientVoltage(velocity)
        motorLeft.speedTick();
        FtcDashboard.getInstance().getTelemetry().addData("Speed gun", GunControl.getInstance().getSpeedGun());
        FtcDashboard.getInstance().getTelemetry().update();
    }

    public void stopShot() { motorLeft.setPower(0); }

    public void setTowerDegree(double degree, double offsetGun, double R) {
        if (degree > 60) degree = 60;
        double theta = Math.toRadians(degree);

        double correction = Math.atan(offsetGun * Math.sin(-theta) / R);
        double correctionDegrees = Math.toDegrees(correction);
        FtcDashboard.getInstance().getTelemetry().addData("correction", correction);
        FtcDashboard.getInstance().getTelemetry().addData("correctionDegreees", correctionDegrees);
        FtcDashboard.getInstance().getTelemetry().addData("Degree servo", (-degree - correctionDegrees));
        FtcDashboard.getInstance().getTelemetry().addData("Pos servo", (-degree - correctionDegrees) / 360);
        FtcDashboard.getInstance().getTelemetry().update();
        setAngleDegree((-degree - correctionDegrees) / 360 + gunConfig.offsetTurn);
    }

    public void testPower() {
        motorLeft.setPower(gunConfig.velocity);
    }

    public void setAngleDegree(double degree) {
        if (degree < 0) {
            degree = (degree * -1);
        }
        if (degree > 0.23) degree = 0.22;
        if (degree < 0.0) degree = 0.0;
        gunConfig.degreeAngleGun = degree;
        FtcDashboard.getInstance().getTelemetry().addData("Degree", degree);

        servo.setServoPosition(degree);
    }


    public double getSpeedGun() {
        return motorLeft.getEncoderSpeed();
    }

    public double getTowerDegree() {
        return gunConfig.degreeTower;
    }


}