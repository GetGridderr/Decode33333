/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 * @author Semen Scherbina
 */

package org.firstinspires.ftc.teamcode.robot;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.core.implementation.EncoderMotor;
import org.firstinspires.ftc.teamcode.core.implementation.Motor;
import org.firstinspires.ftc.teamcode.core.implementation.Servo;
import org.firstinspires.ftc.teamcode.core.trait.device.IEncoderMotor;
import org.firstinspires.ftc.teamcode.core.trait.device.IMotor;
import org.firstinspires.ftc.teamcode.core.trait.device.IServo;
import org.firstinspires.ftc.teamcode.core.util.pid.PidfController;


public final /* static data */ class Robot {

    // ----- DEVICES -----
    public static final IMotor rightFrontVehicleMotor = new Motor("right_front_vehicle_motor");
    public static final IMotor rightBackVehicleMotor = new Motor("right_back_vehicle_motor");
    public static final IMotor leftFrontVehicleMotor = new Motor("left_front_vehicle_motor");
    public static final IMotor leftBackVehicleMotor = new Motor("left_back_vehicle_motor");

    public static final IMotor flowMotor = new Motor("motor_flow");
    public static final IMotor brushMotor = new Motor("motor_brush");

    public static final IEncoderMotor gunMotor = new EncoderMotor("gun_motor_left");

    public static final IServo gunAngleServo = new Servo("servo_angle_gun");
    public static final IServo towerTurnServo = new Servo("servo_turn_tower");
    public static final IServo gunStopper = new Servo("servo_door");

    public static GoBildaPinpointDriver pinpoint = null;

    // ----- DATA -----
    public static Pose2D pos;

    private static boolean initialized = false;

    private final PidfController xPosPID = new PidfController(WebConfig.xP, WebConfig.xI, WebConfig.xD);
    private final PidfController yPosPID = new PidfController(WebConfig.yP, WebConfig.yI, WebConfig.yD);
    private final PidfController yawPosPID = new PidfController(WebConfig.yawP, WebConfig.yawI, WebConfig.yawD);

    private final PidfController xVelPID = new PidfController(WebConfig.vxP, WebConfig.vxI, WebConfig.vxD);
    private final PidfController yVelPID = new PidfController(WebConfig.vyP, WebConfig.vyI, WebConfig.vyD);
    private final PidfController yawVelPID = new PidfController(WebConfig.vyawP, WebConfig.vyawI, WebConfig.vyawD);

    public static void initialize(HardwareMap hardwareMap) {
        if (isInitialized()) return;

        rightFrontVehicleMotor.initialize(hardwareMap);
        rightBackVehicleMotor.initialize(hardwareMap);
        leftFrontVehicleMotor.initialize(hardwareMap);
        leftBackVehicleMotor.initialize(hardwareMap);

        flowMotor.initialize(hardwareMap);
        brushMotor.initialize(hardwareMap);

        gunMotor.initialize(hardwareMap);

        gunAngleServo.initialize(hardwareMap);
        towerTurnServo.initialize(hardwareMap);
        gunStopper.initialize(hardwareMap);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpoint.setOffsets(110, 60, DistanceUnit.MM);
        pinpoint.recalibrateIMU();
        pinpoint.resetPosAndIMU();

        initialized = true;
    }

    // ----- UTILS -----

    public double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    public double getShortestPathToAngle(double currentAngle, double targetAngle) {
        double difference = targetAngle - currentAngle;
        return normalizeAngle(difference);
    }

    // ----- PINPOINT -----

    public void updateOdometry() {
        pinpoint.update();
        pos = pinpoint.getPosition();
    }

    public double getSpeedX() {
        return pinpoint.getVelX(DistanceUnit.CM);
    }

    public double getSpeedY() {
        return pinpoint.getVelY(DistanceUnit.CM);
    }

    public double getSpeedYaw() {
        return pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES);
    }

    public double getX() {
        return pos.getX(DistanceUnit.CM);
    }

    public double getY() {
        return pos.getY(DistanceUnit.CM);
    }

    public double getYaw() {
        return pos.getHeading(AngleUnit.DEGREES);
    }

    // ----- VEHICLES -----

    public void setPIDsFromConfig() {
        xPosPID.kP = WebConfig.xP;
        xPosPID.kI = WebConfig.xI;
        xPosPID.kD = WebConfig.xD;
        yPosPID.kP = WebConfig.yP;
        yPosPID.kI = WebConfig.yI;
        yPosPID.kD = WebConfig.yD;
        yawPosPID.kP = WebConfig.yawP;
        yawPosPID.kI = WebConfig.yawI;
        yawPosPID.kD = WebConfig.yawD;
        xVelPID.kP = WebConfig.vxP;
        xVelPID.kI = WebConfig.vxI;
        xVelPID.kD = WebConfig.vxD;
        yVelPID.kP = WebConfig.vyP;
        yVelPID.kI = WebConfig.vyI;
        yVelPID.kD = WebConfig.vyD;
        yawVelPID.kP = WebConfig.vyawP;
        yawVelPID.kI = WebConfig.vyawI;
        yawVelPID.kD = WebConfig.vyawD;
    }

    // true: motors are spinning; false: arrived
    public boolean setPowers(double xPower,
                            double yPower,
                            double yawPower,
                            boolean normalize) {
        if (Math.abs(yPower) < WebConfig.powerLim) yPower = 0;
        if (Math.abs(xPower) < WebConfig.powerLim) xPower = 0;
        if (Math.abs(yawPower) < WebConfig.powerLim) yawPower = 0;

        if(yPower == 0 && xPower == 0 && yawPower == 0) {
            return false;
        }

        double frontLeftPower = yPower + xPower + yawPower;
        double frontRightPower = yPower - xPower - yawPower;
        double backLeftPower = yPower - xPower + yawPower;
        double backRightPower = yPower + xPower - yawPower;

        double maxSpd = Math.max(Math.max(
                Math.abs(frontLeftPower), Math.abs(frontRightPower)
        ), Math.max(
                Math.abs(backLeftPower), Math.abs(backRightPower)
        ));

        if(maxSpd > 1) {
            FtcDashboard.getInstance().getTelemetry().addData("Max motor speed", 1);
            if(normalize) {
                frontLeftPower /= maxSpd;
                frontRightPower /= maxSpd;
                backLeftPower /= maxSpd;
                backRightPower /= maxSpd;
            }
        } else {
            FtcDashboard.getInstance().getTelemetry().addData("Max motor speed", maxSpd);
        }

        leftFrontVehicleMotor.setPower(frontLeftPower);
        leftBackVehicleMotor.setPower(backLeftPower);
        rightFrontVehicleMotor.setPower(frontRightPower);
        rightBackVehicleMotor.setPower(backRightPower);

        return true;
    }

    public boolean setSpeed(double xSpd,
                        double ySpd,
                        double yawSpd) {
        return setPowers(
            xVelPID.update(getSpeedX(), xSpd),
            yVelPID.update(getSpeedY(), ySpd),
            yawVelPID.update(getSpeedYaw(), yawSpd),
            WebConfig.normAuto
        );
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
