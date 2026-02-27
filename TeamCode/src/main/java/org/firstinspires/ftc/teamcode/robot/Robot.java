/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 * @author Semen Scherbina
 */

package org.firstinspires.ftc.teamcode.robot;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

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
import org.firstinspires.ftc.teamcode.core.util.Util;
import org.firstinspires.ftc.teamcode.core.util.pid.PidfController;


public final /* static data */ class Robot {
    public enum TeamColor {
        RED(1),
        BLUE(-1);

        private final int sign;

        TeamColor(int sign) {
            this.sign = sign;
        }

        public int getSign() {
            return sign;
        }
    }

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
    public static final IServo gunDoor = new Servo("servo_door");

    public static GoBildaPinpointDriver pinpoint = null;

    // ----- DATA -----
    public static Pose2D pos = new Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.DEGREES, 0);
    public static double gunX, gunY;
    // X Y HEADING

    public static final double[] RED_TELEOP_END_POS = {81, 98, 90};
    public static final double[] RED_AUTONOMOUS_END_POS = {-103, -39, -133};
    public static final double[] RED_FAR_INIT_POS = {-23, 169, 180};
    public static final double[] RED_GOAL_INIT_POS = {-123, -131, -127};
    public static final double[] RED_DISTANCE_FROM_GATES = {-32, -60, -122};

    public static final double[] RED_GATE_OPEN_POS = {-152, 20, -124};

    public static final double[] RED_BALLS1_PRE_POS = {-72, -24, -93};
    public static final double[] RED_BALLS1_GOT_POS = {-141, -25, -94};
    public static final double[] RED_BALLS2_PRE_POS = {-74, 37, -94};
    public static final double[] RED_BALLS2_GOT_POS = {-161, 33, -92};
    public static final double[] RED_BALLS3_PRE_POS = {-75, 94, -92};
    public static final double[] RED_BALLS3_GOT_POS = {-162, 94, -92};

    public static final double[] RED_BALLS2_AFTER_POS = {-157, 53, -92};
    public static final double[] RED_BALLS2_BACK_POS = {-83, 53, -92};

    public static boolean initialized = false;

    public static TeamColor teamColor = TeamColor.RED;

    public static final PidfController xPosPID = new PidfController(WebConfig.xP, WebConfig.xI, WebConfig.xD);
    public static final PidfController yPosPID = new PidfController(WebConfig.yP, WebConfig.yI, WebConfig.yD);
    public static final PidfController yawPosPID = new PidfController(WebConfig.yawP, WebConfig.yawI, WebConfig.yawD);

    public static final PidfController xVelPID = new PidfController(WebConfig.vxP, WebConfig.vxI, WebConfig.vxD,1);
    public static final PidfController yVelPID = new PidfController(WebConfig.vyP, WebConfig.vyI, WebConfig.vyD, 1);
    public static final PidfController yawVelPID = new PidfController(WebConfig.vyawP, WebConfig.vyawI, WebConfig.vyawD, 1);

    public static VoltageSensor voltageSensor;
    public static boolean flowOn = false;
    public static boolean doorOpen = false;

    public static void initialize(HardwareMap hardwareMap) {
        if (isInitialized()) {
            reset();
            return;
        }

        rightFrontVehicleMotor.initialize(hardwareMap);
        rightBackVehicleMotor.initialize(hardwareMap);
        leftFrontVehicleMotor.initialize(hardwareMap);
        leftBackVehicleMotor.initialize(hardwareMap);

        flowMotor.initialize(hardwareMap);
        brushMotor.initialize(hardwareMap);

        gunMotor.initialize(hardwareMap);
        gunMotor.invertDirection(); // Gun motors spin reversed by default

        gunAngleServo.initialize(hardwareMap);
        towerTurnServo.initialize(hardwareMap);
        gunDoor.initialize(hardwareMap);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpoint.setOffsets(RobotConfig.yOdometerOffset,
                RobotConfig.xOdometerOffset,
                DistanceUnit.MM);
        pinpoint.recalibrateIMU();
        pinpoint.resetPosAndIMU();
        pinpoint.setPosition(pos);
        updateOdometry();

        // configure movement motors directions
        rightFrontVehicleMotor.invertDirection();
        rightBackVehicleMotor.invertDirection();

        initialized = true;
    }

    public static double getVoltage() {
        FtcDashboard.getInstance().getTelemetry().addData("Voltage", voltageSensor.getVoltage());
        return voltageSensor.getVoltage();
    }

    public static double getVoltageCorrection() {
        return RobotConfig.desiredVoltage / getVoltage();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static double[] convertPosColor(double[] pos) {
        return new double[] {
                teamColor.getSign() * pos[0],
                pos[1],
                teamColor.getSign() * pos[2],
        };
    }

    public static void setInitPos() {
        Robot.setPos(Robot.convertPosColor(Robot.RED_GOAL_INIT_POS));
    }

    public static void setPos(double[] pos) {
        setPos(pos[0], pos[1], pos[2]);
    }

    public static void setPos(double x, double y, double yaw) {
        pos = new Pose2D(DistanceUnit.CM, y,  // swap x/y
                -x,
                AngleUnit.DEGREES,
                -yaw);
        pinpoint.setPosition(pos);
        updateOdometry();
    }

    public static void reset() {
        setPos(0, 0, 0);
    }

    // ----- PINPOINT -----

    public static double[] getGunPos(double x, double y, double yaw) {
        double gX = 0;
        double gY = RobotConfig.gunOffset;
        double[] gunPos = Util.rotateVector(gX, gY, getYaw());
        gX = gunPos[0] + getX();
        gY = gunPos[1] + getY();
        return new double[]{gX, gY};
    }

    public static void updateOdometry() {
        pinpoint.update();
        pos = pinpoint.getPosition();
        double[] gunPos = getGunPos(getX(), getY(), getYaw());
        gunX = gunPos[0];
        gunY = gunPos[1];
    }

    public static double getSpeedX() {  // swap x/y
        return -pinpoint.getVelY(DistanceUnit.CM);
    }

    public static double getSpeedY() {  // swap x/y
        return pinpoint.getVelX(DistanceUnit.CM);
    }

    public static double getSpeedYaw() {  // clockwise
        return -pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES);
    }

    public static double getX() {
        return -pos.getY(DistanceUnit.CM);
    }

    public static double getY() {
        return pos.getX(DistanceUnit.CM);
    }

    public static double getYaw() {
        return -pos.getHeading(AngleUnit.DEGREES);
    }

    // ----- VEHICLES -----

    public static void setPIDsFromConfig() {
        xPosPID.coefficients.kP = WebConfig.xP;
        xPosPID.coefficients.kI = WebConfig.xI;
        xPosPID.coefficients.kD = WebConfig.xD;

        yPosPID.coefficients.kP = WebConfig.yP;
        yPosPID.coefficients.kI = WebConfig.yI;
        yPosPID.coefficients.kD = WebConfig.yD;

        yawPosPID.coefficients.kP = WebConfig.yawP;
        yawPosPID.coefficients.kI = WebConfig.yawI;
        yawPosPID.coefficients.kD = WebConfig.yawD;

        xVelPID.coefficients.kP = WebConfig.vxP;
        xVelPID.coefficients.kI = WebConfig.vxI;
        xVelPID.coefficients.kD = WebConfig.vxD;

        yVelPID.coefficients.kP = WebConfig.vyP;
        yVelPID.coefficients.kI = WebConfig.vyI;
        yVelPID.coefficients.kD = WebConfig.vyD;

        yawVelPID.coefficients.kP = WebConfig.vyawP;
        yawVelPID.coefficients.kI = WebConfig.vyawI;
        yawVelPID.coefficients.kD = WebConfig.vyawD;

        gunMotor.setPidfCoefficients(
                WebConfig.gP,
                WebConfig.gI,
                WebConfig.gD,
                RobotConfig.gF
        );
    }

    // true: motors are spinning; false: arrived
    public static boolean setPowers(double xPower,
                            double yPower,
                            double yawPower,
                            boolean normalize) {
        double frontLeftPower = yPower + xPower + yawPower;
        double frontRightPower = yPower - xPower - yawPower;
        double backLeftPower = yPower - xPower + yawPower;
        double backRightPower = yPower + xPower - yawPower;

        if (Math.abs(frontLeftPower) < WebConfig.powerLim) frontLeftPower = 0;
        if (Math.abs(frontRightPower) < WebConfig.powerLim) frontRightPower = 0;
        if (Math.abs(backLeftPower) < WebConfig.powerLim) backLeftPower = 0;
        if (Math.abs(backRightPower) < WebConfig.powerLim) backRightPower = 0;

        if(frontLeftPower == 0 &&
                frontRightPower == 0 &&
                backLeftPower == 0 &&
                backRightPower == 0) {
            leftFrontVehicleMotor.setPower(0);
            leftBackVehicleMotor.setPower(0);
            rightFrontVehicleMotor.setPower(0);
            rightBackVehicleMotor.setPower(0);
            return false;
        }

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

    public static boolean setSpeed(double xSpd,
                                   double ySpd,
                                   double yawSpd) {
        updateOdometry();
        setPIDsFromConfig();
        return setPowers(
                xVelPID.update(getSpeedX(), xSpd),
                yVelPID.update(getSpeedY(), ySpd),
                yawVelPID.update(getSpeedYaw(), yawSpd),
                WebConfig.normTeleop
        );
    }

    public static boolean goTo(double[] pos) {
        return goTo(pos[0], pos[1], pos[2]);
    }

    public static boolean goTo(double xPos,
                               double yPos,
                               double yaw) {
        updateOdometry();
        setPIDsFromConfig();
        double xErr = xPos - getX();
        double yErr = yPos - getY();
        double[] errVector = Util.rotateVector(xErr, yErr, -getYaw());
        xErr = errVector[0];
        yErr = errVector[1];
        FtcDashboard.getInstance().getTelemetry().addData("XErr", xErr);
        FtcDashboard.getInstance().getTelemetry().addData("YErr", yErr);
        return setPowers(
                xPosPID.update(-xErr, 0),
                yPosPID.update(-yErr, 0),
                yawPosPID.update(-Util.getShortestPathToAngle(getYaw(), yaw), 0),
                WebConfig.normAuto
        );
    }

    public static void stopMoving() {
        setPowers(0, 0, 0, false);
    }

    public static void startBrush() {
        brushMotor.setPower(1);
    }

    public static void stopBrush() {
        brushMotor.setPower(0);
    }

    public static void startFlow() {
        flowOn = true;
        if(doorOpen) flowMotor.setPower(1);
        else flowMotor.setPower(WebConfig.flowSpeedWithClosedDoor);
    }

    public static void stopFlow() {
        flowOn = false;
        flowMotor.setPower(0);
    }

    public static void closeGunDoor() {
        doorOpen = false;
        gunDoor.setPosition(WebConfig.doorClosed);  // найдено ценой 3 синяков
        FtcDashboard.getInstance().getTelemetry().addData("Door open", false);
        if(flowOn) flowMotor.setPower(WebConfig.flowSpeedWithClosedDoor);
    }

    public static void openGunDoor() {
        doorOpen = true;
        gunDoor.setPosition(WebConfig.doorOpen);
        FtcDashboard.getInstance().getTelemetry().addData("Door open", true);
        if(flowOn) flowMotor.setPower(1);
    }

    public static void setGunVelocity(double vel) {
        if(gunMotor.getVelocity() < vel - 25) {
            gunMotor.setPower(1);
        } else {
            gunMotor.setVelocity(vel);
        }
    }

    public static void _turnTowerRaw(double angle) {
        angle = 0.5 - angle / 540.0;
        angle = Util.clamp(angle, 0, 1);
        towerTurnServo.setPosition(angle);
    }

    public static boolean turnTower(double angle) {
        angle = Util.normalizeAngle(angle+180);

        double best = 0, dist, a;
        double bestDist = Double.POSITIVE_INFINITY;

        for(double k = -1; k <= 1; k+=1) {
            a = angle + 360.0*k;

            // This angle is supported
            if(a >= RobotConfig.towerMin && a <= RobotConfig.towerMax) {
                _turnTowerRaw(a);
                return true;
            }

            if(a < RobotConfig.towerMin) {  // too low
                dist = RobotConfig.towerMin - a;
                a = RobotConfig.towerMin;
            } else {  // too high
                dist = a - RobotConfig.towerMax;
                a = RobotConfig.towerMax;
            }

            if(bestDist > dist) {
                bestDist = dist;
                best = a;
            }
        }

        _turnTowerRaw(best);
        return false;
    }

    public static double angleToGoal(double x, double y, double yaw) {
        double dx = WebConfig.GoalX * teamColor.sign - x;
        double dy = WebConfig.GoalY - y;
        double angle = Math.atan2(dx, dy) * Util.DEG_PER_RAD - yaw;
        return Util.normalizeAngle(angle);
    }

    public static double angleToGoal() {
        return angleToGoal(getX(), getY(), getYaw());
    }

    public static void setShootingAngle(double x) {
        x = Util.clamp(x, 0, 1);
        x = x * (RobotConfig.shootMax - RobotConfig.shootMin) + RobotConfig.shootMin;
        gunAngleServo.setPosition(x);
    }

    public static double normalizeStickPower(double input) {
        return IMotor.normalizePower(
                Math.pow(Math.abs(input), WebConfig.stickNormPowExponent)
                * Math.signum(input));
    }
}
