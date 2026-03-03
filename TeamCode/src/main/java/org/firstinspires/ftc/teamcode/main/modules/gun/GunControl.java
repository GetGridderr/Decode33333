package org.firstinspires.ftc.teamcode.main.modules.gun;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.core.device.motor.FFEncoderMotor;
import org.firstinspires.ftc.teamcode.core.device.sensor.SensorVoltage;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;

// coding by Matvey Ivanovv
@Config
public final class GunControl implements Initializable {
    private static final GunControl INSTANCE = new GunControl();
    private final FFEncoderMotor motorGun;
    private final Servomotor servo;
    private final Servomotor servoAngle;

    public static DataShots[] dataShots = {
            new DataShots(60, 980, 0.17, 0.00004),
            new DataShots(100, 1050, 0.25, 0.00004),
            new DataShots(130, 1050, 0.25, 0.00003),
            new DataShots(180, 1200, 0.75, 0.00010),
            new DataShots(270, 1325, 0.87, 0.00003),
            new DataShots(240, 1300, 0.9, 0.00030),
            new DataShots(300, 1500, 1.0, 0.00032),
    };

    public GunControl() {
        motorGun = new FFEncoderMotor("gun_motor_left");
        servo = new Servomotor("servo_turn_tower");
        servoAngle = new Servomotor("servo_angle_gun");
    }

    public static GunControl getInstance() { return INSTANCE; }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        motorGun.initialize(hardwareMap);
        SensorVoltage.getInstance().initialize(hardwareMap);
        servo.initialize(hardwareMap);
        servoAngle.initialize(hardwareMap);
        motorGun.invertDirection();
        SensorVoltage.getInstance().initialize(hardwareMap);
    }

    @Override
    public boolean isInitialized() { return motorGun.isInitialized() && servo.isInitialized() && SensorVoltage.getInstance().isInitialized(); }

    public double getVelocity() {
        return gunConfig.velocity;
    }

    public void setVelocity(double vel) {
        gunConfig.velocity = vel;
    }

    public void startShot() {
        FtcDashboard.getInstance().getTelemetry().update();
        motorGun.setPIDCoefficients(gunConfig.kP, gunConfig.kI, gunConfig.kD);
        motorGun.setAlpha(gunConfig.alpha);
        motorGun.setSpeedMul(gunConfig.spdMul);
        motorGun.setSpeed(gunConfig.velocity);
        motorGun.speedTick();
        FtcDashboard.getInstance().getTelemetry().addData("Speed gun", GunControl.getInstance().getSpeedGun());
        FtcDashboard.getInstance().getTelemetry().update();
    }

    public DataShots shotToDistance(double distance) {
        if (distance <= dataShots[0].distance) {
            return dataShots[0];
        }
        if (distance >= dataShots[dataShots.length - 1].distance) {
            return dataShots[dataShots.length - 1];
        }
        for (int i = 0; i < dataShots.length - 1; i++) {

            DataShots left  = dataShots[i];
            DataShots right = dataShots[i + 1];

            if (distance >= left.distance && distance <= right.distance) {

                double coefficient = (distance - left.distance) / (right.distance - left.distance);
                double speed = left.speed + (coefficient * (right.speed - left.speed));
                double angle = left.angle + (coefficient * (right.angle - left.angle));
                double kp = left.k + (coefficient * (right.k - left.k));
                return new DataShots(distance, speed, angle, kp);
            }
        }
        return dataShots[dataShots.length - 1];
    }

    public void stopShot() { motorGun.setPower(0); }

    public void setTowerDegree(double degree, double offsetGun, double R) {
        double theta = Math.toRadians(degree);

        double correction = Math.atan(offsetGun * Math.sin(-theta) / R);
        double correctionDegrees = Math.toDegrees(correction);
        setAngleDegree((-degree - correctionDegrees) / 360 + gunConfig.offsetTurn);
    }

    public void aimToTarget(Pose2D posTarget, Pose2D robotPos) {
        double errX = posTarget.getX(DistanceUnit.CM) - robotPos.getX(DistanceUnit.CM);
        double errY = posTarget.getY(DistanceUnit.CM) - robotPos.getY(DistanceUnit.CM);
        double targetAngle = Math.toDegrees(Math.atan2(errY, errX));
        double servoPosition = targetAngle / 360.0 * gunConfig.kTower;
        servoPosition += gunConfig.offsetTurn;
        servoPosition = Math.max(gunConfig.minTurnPos, Math.min(gunConfig.maxTurnPos, servoPosition));
        setAngleDegree(servoPosition);
    }

    public double getFlowGun() {
        return motorGun.getCurrent(CurrentUnit.AMPS);
    }

    public void setBananServo() {
        servoAngle.setServoPosition(gunConfig.bananPosition);
    }

    public void setAngleDegree(double position) {
        gunConfig.degreeAngleGun = position;
        servo.setServoPosition(position);
    }

    public double getSpeedGun() {
        return motorGun.getEncoderSpeed();
    }

    public double getTowerDegree() {
        return gunConfig.degreeTower;
    }


}