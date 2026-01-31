package org.firstinspires.ftc.teamcode.main.movement;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDCoefficients;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDRegulator;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;

@Config
public final class Vehicles implements Initializable {
    private static final Vehicles INSTANCE = new Vehicles();
    private final Motor leftFrontMotor;
    private final Motor rightFrontMotor;
    private final Motor leftBackMotor;
    private final Motor rightBackMotor;
    public double xErr = 0;
    public double yErr = 0;
    public double yawErr = 0;
    private double oldYaw = 0;
    public boolean isBusy = false;
    public double lastTime = 0;
    public static double speedMaxX = 1200;
    public static double speedMaxY = 1200;
    public static double speedMaxYaw = 1200;
    public static double power = 1.2;
    private static final PIDRegulator xSpeedPID = new PIDRegulator(1, 1, 1);
    private static final PIDRegulator ySpeedPID = new PIDRegulator(1, 1, 1);
    private static final PIDRegulator yawSpeedPID = new PIDRegulator(1, 1, 1);
    private static final PIDRegulator xPosPID = new PIDRegulator(1, 1, 1);
    private static final PIDRegulator yPosPID = new PIDRegulator(1, 1, 1);
    private static final PIDRegulator yawPosPID = new PIDRegulator(1, 1, 1);


    private Vehicles() {
        leftFrontMotor= new Motor("left_front_vehicle_motor");
        rightFrontMotor= new Motor("right_front_vehicle_motor"); //
        leftBackMotor = new Motor("left_back_vehicle_motor");
        rightBackMotor = new Motor("right_back_vehicle_motor");
    }

    public static Vehicles getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        leftFrontMotor.initialize(hardwareMap);
        leftBackMotor.initialize(hardwareMap);
        rightFrontMotor.initialize(hardwareMap);
        rightBackMotor.initialize(hardwareMap);
        OdometerPinpoint.getInstance().initialize(hardwareMap);
//        leftBackMotor.invertDirection();
        leftBackMotor.invertDirection();
        leftFrontMotor.invertDirection();
//        rightFrontMotor.invertDirection();

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public boolean isInitialized() {
        return leftFrontMotor.isInitialized()
                && rightFrontMotor.isInitialized()
                && leftBackMotor.isInitialized()
                && rightBackMotor.isInitialized()
                &&  OdometerPinpoint.getInstance().isInitialized();
    }

    public void stopAll() {
        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
    }

    public double[] fieldCentric(double forward, double horizontal, double yawDeg) {
        double yawRad = Math.toRadians(yawDeg);
        double f = forward * Math.cos(yawRad) + horizontal * Math.sin(yawRad);
        double h = -forward * Math.sin(yawRad) + horizontal * Math.cos(yawRad);
        return new double[]{f, h};
    }

    public boolean moveToDirection(double forward,
                                   double horizontal,
                                   double turn,
                                   boolean normalize) {

        if(Math.abs(forward) < 0.1 && Math.abs(horizontal) < 0.1 && Math.abs(turn) < 0.1) {
            leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            stopAll();
            return false;
        }

        double frontLeftPower = forward + horizontal + turn;
        double frontRightPower = forward - horizontal - turn;
        double backLeftPower = forward - horizontal + turn;
        double backRightPower = forward + horizontal - turn;

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

        leftFrontMotor.setPower(Motor.normalizePower(frontLeftPower));
        leftBackMotor.setPower(Motor.normalizePower(backLeftPower));
        rightFrontMotor.setPower(Motor.normalizePower(frontRightPower));
        rightBackMotor.setPower(Motor.normalizePower(backRightPower));
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean moveToDirection(double forward, double horizontal, double turn) {
        return moveToDirection(forward, horizontal, turn, false);
    }
    private boolean goTo(double x, double y, double yaw, boolean posReg, boolean yawReg) {
        double xSpd = 0, ySpd = 0, yawSpd = 0;
        if(posReg) {
            xErr = OdometerPinpoint.getInstance().getX() - x;
            yErr = OdometerPinpoint.getInstance().getY() - y;
            double[] errVector = Odometry.rotateVector(xErr, yErr, -OdometerPinpoint.getInstance().getYaw());
            xErr = errVector[0];
            yErr = errVector[1];
            xSpd = xPosPID.PIDGet(xErr);
            ySpd = yPosPID.PIDGet(-yErr);
            isBusy = true;
        }
        if(yawReg) {
            yawErr = OdometerPinpoint.getInstance().getShortestPathToAngle(OdometerPinpoint.getInstance().getYaw(), yaw);
            yawSpd = yawPosPID.PIDGet(yawErr);
        }
        if (Math.abs(xErr) < 8 && Math.abs(yErr) < 8 && Math.abs(yawErr) < 2) {
            moveToDirection(0, 0, 0);
            return false;
        }
        FtcDashboard.getInstance().getTelemetry().addData("ErrYaw", yawErr);
        FtcDashboard.getInstance().getTelemetry().addData("X speed", Motor.normalizePower(xSpd));
        FtcDashboard.getInstance().getTelemetry().addData("Y speed", Motor.normalizePower(ySpd));
        FtcDashboard.getInstance().getTelemetry().addData("Yaw speed", Motor.normalizePower(yawSpd));

        FtcDashboard.getInstance().getTelemetry().update();
        return moveToDirection(xSpd * 0.8, ySpd * 0.8, yawSpd * 0.8, true);
    }

    @SuppressWarnings("UnusedReturnValue")



    private double lastYawAngle = 0;
    private ElapsedTime yawTimer = new ElapsedTime();

    @SuppressWarnings("UnusedReturnValue")
    public boolean setSpeed(double targetX, double targetY, double targetYawSpeed) {
        double currentXField = OdometerPinpoint.getInstance().getSpeedX();
        double currentYField = OdometerPinpoint.getInstance().getSpeedY();
        double currentYawAngle = OdometerPinpoint.getInstance().getYaw();
        double deltaTime = yawTimer.seconds();
        double currentYawSpeed = 0;
        if (deltaTime > 0.001) {
            double deltaAngle = currentYawAngle - lastYawAngle;
            if (deltaAngle > 180) {
                deltaAngle -= 360;
            } else if (deltaAngle < -180) {
                deltaAngle += 360;
            }

            currentYawSpeed = deltaAngle / deltaTime;
        }
        lastYawAngle = currentYawAngle;
        yawTimer.reset();
        FtcDashboard.getInstance().getTelemetry().addData("currentX", currentXField);
        FtcDashboard.getInstance().getTelemetry().addData("targetX", targetX);
        double forwardPower = xSpeedPID.PIDGet(currentXField, targetX * speedMaxX);
        double horizontalPower = ySpeedPID.PIDGet(currentYField, targetY * speedMaxY);
        double turnPower = yawSpeedPID.PIDGet(currentYawSpeed, targetYawSpeed * speedMaxYaw);
        double[] robotPowers = fieldToRobotPowers(forwardPower, horizontalPower, currentYawAngle);
        FtcDashboard.getInstance().getTelemetry().addData("Xpower", robotPowers[0]);
        FtcDashboard.getInstance().getTelemetry().update();
        return moveToDirection(robotPowers[0], robotPowers[1], turnPower, true);
    }

    private double[] fieldToRobotPowers(double powerXField, double powerYField, double yawDeg) {
        double yawRad = Math.toRadians(yawDeg);
        double powerXRobot = powerXField * Math.cos(yawRad) - powerYField * Math.sin(yawRad);
        double powerYRobot = powerXField * Math.sin(yawRad) + powerYField * Math.cos(yawRad);
        return new double[]{ powerXRobot, powerYRobot };
    }


    public boolean isPosition(double x, double y, double yaw) {
        return Math.abs(x - OdometerPinpoint.getInstance().getX()) > 14 && Math.abs(y - OdometerPinpoint.getInstance().getY()) > 14 && Math.abs(yaw - OdometerPinpoint.getInstance().getYaw()) > 14;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean goTo(double x, double y) {
        return goTo(x, y, 0, true, false);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean goTo(double x, double y, double yaw) {
        return goTo(x, y, yaw, true, true);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean rotateTo(double yaw) {
        return goTo(0, 0, yaw, false, true);
    }

    public void setPosPID(double pX, double iX, double dX,
                          double pY, double iY, double dY,
                          double pYaw, double iYaw, double dYaw) {
        xPosPID.setCoefficients(pX, iX, dX);
        yPosPID.setCoefficients(pY, iY, dY);
        yawPosPID.setCoefficients(pYaw, iYaw, dYaw);
    }

    public void setSpeedPID(double pX, double iX, double dX,
                            double pY, double iY, double dY,
                            double pYaw, double iYaw, double dYaw) {
        xSpeedPID.setCoefficients(pX, iX, dX);
        ySpeedPID.setCoefficients(pY, iY, dY);
        yawSpeedPID.setCoefficients(pYaw, iYaw, dYaw);
    }

    public void setPosPID(PIDCoefficients pidX, PIDCoefficients pidY, PIDCoefficients pidYaw) {
        xPosPID.setCoefficients(pidX);
        yPosPID.setCoefficients(pidY);
        yawPosPID.setCoefficients(pidYaw);
    }

    public double getPositionOdometerX() {
        return OdometerPinpoint.getInstance().getX();
    }

    public double getPositionOdometerY() {
        return OdometerPinpoint.getInstance().getY();
    }

    public double getGyroYaw() { return OdometerPinpoint.getInstance().getYaw(); }
}