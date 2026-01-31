package org.firstinspires.ftc.teamcode.main.modules.gun;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;

import android.provider.ContactsContract;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.core.device.motor.FFEncoderMotor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;

import org.firstinspires.ftc.teamcode.core.device.sensor.SensorVoltage;
import org.firstinspires.ftc.teamcode.main.config.ConfigValues;

// coding by Matvey Ivanovv
@Config
public final class GunControl implements Initializable {
    private static final GunControl INSTANCE = new GunControl();
    private final FFEncoderMotor motorLeft;
    private final Servomotor servo;
    private final Servomotor servoAngle;

    public static DataShots[] dataShots = {
            new DataShots(60, -980, 0.17, 0.00004),
            new DataShots(100, -1050, 0.25, 0.00004),
            new DataShots(130, -1050, 0.25, 0.00003),
            new DataShots(180, -1200, 0.75, 0.00010),
            new DataShots(270, -1325, 0.87, 0.00003),
            new DataShots(240, -1300, 0.9, 0.00030),
            new DataShots(300, -1500, 1.0, 0.00032),
    };

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
        motorLeft.setSpeed(gunConfig.velocity);
        motorLeft.speedTick();
        FtcDashboard.getInstance().getTelemetry().addData("Speed gun", GunControl.getInstance().getSpeedGun());
        FtcDashboard.getInstance().getTelemetry().update();
    }

    public DataShots shotToDistance(double distance) {
        if (distance <= dataShots[0].distance) {
            return dataShots[0];
        }
        int length = dataShots.length - 1;
        if (distance >= dataShots[length].distance) {
            return dataShots[length];
        }
        for (int i = 0; i < length; i++) {

            DataShots left  = dataShots[i];
            DataShots right = dataShots[i + 1];

            if (distance >= left.distance && distance <= right.distance) {

                double coefficient = (distance - left.distance) / (right.distance - left.distance);
                double speed = left.speed + (coefficient * (right.speed - left.speed));
                double angle = left.angle + (coefficient * (right.angle - left.angle));
                double k = left.k + (coefficient * (right.k - left.k));
                return new DataShots(distance, speed, angle, k);
            }
        }
        return dataShots[length];
    }

    public void stopShot() { motorLeft.setPower(0); }

    public void setTowerDegree(double degree, double offsetGun, double R) {
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

    public void setTowerDegree(double xGoal, double yGoal,
                               double xRobot, double yRobot,
                               double yawRobot) {
        double errX = xGoal - xRobot;
        double errY = yGoal - yRobot;
        double targetAngle = Math.toDegrees(Math.atan2(-errY, -errX));
        while (targetAngle < 0) targetAngle += 360;
        while (targetAngle >= 360) targetAngle -= 360;
        double servoAngle = targetAngle - yawRobot;
        while (servoAngle > 180) servoAngle -= 360;
        while (servoAngle < -180) servoAngle += 360;
        double servoPosition = servoAngle / 360.0 * gunConfig.kTower;
        servoPosition += gunConfig.offsetTurn;
        servoPosition = Math.max(0.05, Math.min(0.95, servoPosition));

        FtcDashboard.getInstance().getTelemetry().addData("dx", errX);
        FtcDashboard.getInstance().getTelemetry().addData("dy", errY);
        FtcDashboard.getInstance().getTelemetry().addData("Target angle", targetAngle);
        FtcDashboard.getInstance().getTelemetry().addData("Servo angle", servoAngle);
        FtcDashboard.getInstance().getTelemetry().addData("Servo position", servoPosition);
        setAngleDegree(servoPosition);
    }

    public double getFlowGun() {
        return motorLeft.getCurrent(CurrentUnit.AMPS);
    }

    public void setBananServo() {
        servoAngle.setServoPosition(gunConfig.bananPosition);
    }

    public void setAngleDegree(double position) {
        gunConfig.degreeAngleGun = position;
        FtcDashboard.getInstance().getTelemetry().addData("Servo position set", position);
        servo.setServoPosition(position);
    }

    public double getSpeedGun() {
        return motorLeft.getEncoderSpeed();
    }

    public double getTowerDegree() {
        return gunConfig.degreeTower;
    }


}