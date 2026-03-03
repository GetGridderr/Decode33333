package org.firstinspires.ftc.teamcode.main.movement;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDCoefficients;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDRegulator;

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
    public boolean isBusy = false;
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
        leftBackMotor.invertDirection();
        leftFrontMotor.invertDirection();
    }

    @Override
    public boolean isInitialized() {
        return leftFrontMotor.isInitialized()
                && rightFrontMotor.isInitialized()
                && leftBackMotor.isInitialized()
                && rightBackMotor.isInitialized()
                &&  OdometerPinpoint.getInstance().isInitialized();
    }
    public boolean moveToDirection(double forward,
                                   double horizontal,
                                   double turn) {

        double frontLeftPower = forward + horizontal + turn;
        double frontRightPower = forward - horizontal - turn;
        double backLeftPower = forward - horizontal + turn;
        double backRightPower = forward + horizontal - turn;

        double max = Math.max(Math.max(Math.abs(frontLeftPower),
                Math.abs(frontRightPower)),
                Math.max(Math.abs(backLeftPower),
                        Math.abs(backRightPower)));
        if (max > 1.0) {
            frontLeftPower /= max; frontRightPower /= max; backLeftPower /= max; backRightPower /= max;
        }

        leftFrontMotor.setPower(frontLeftPower);
        leftBackMotor.setPower(backLeftPower);
        rightFrontMotor.setPower(frontRightPower);
        rightBackMotor.setPower(backRightPower);
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
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
        if (Math.abs(xErr) < 5 && Math.abs(yErr) < 5 && Math.abs(yawErr) < 2) {
            moveToDirection(0, 0, 0);
            return false;
        }
        FtcDashboard.getInstance().getTelemetry().addData("ErrYaw", yawErr);
        FtcDashboard.getInstance().getTelemetry().addData("X speed", Motor.normalizePower(xSpd));
        FtcDashboard.getInstance().getTelemetry().addData("Y speed", Motor.normalizePower(ySpd));
        FtcDashboard.getInstance().getTelemetry().addData("Yaw speed", Motor.normalizePower(yawSpd));

        FtcDashboard.getInstance().getTelemetry().update();
        return moveToDirection(xSpd, ySpd, yawSpd);
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
    public boolean goTo(Pose2D pos) {
        return goTo(pos.getX(DistanceUnit.CM), pos.getY(DistanceUnit.CM), pos.getHeading(AngleUnit.DEGREES), true, true);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean rotateTo(double yaw) {
        return goTo(0, 0, yaw, false, true);
    }


    @SuppressWarnings("UnusedReturnValue")
    public boolean setSpeed(double targetX, double targetY, double targetYawSpeed, double vectorRotate) {
        double currentXField = OdometerPinpoint.getInstance().getSpeedX();
        double currentYField = OdometerPinpoint.getInstance().getSpeedY();
        double currentYaw = OdometerPinpoint.getInstance().getYaw();
        double currentYawSpeed = OdometerPinpoint.getInstance().getSpeedYaw();
        double forwardPower = xSpeedPID.PIDGet(currentXField, targetX * vehicles.speedMaxX);
        double horizontalPower = ySpeedPID.PIDGet(currentYField, targetY * vehicles.speedMaxY);
        double turnPower = yawSpeedPID.PIDGet(currentYawSpeed, targetYawSpeed * vehicles.speedMaxYaw);
        FtcDashboard.getInstance().getTelemetry().addData("ErrX", xSpeedPID.setpoint - xSpeedPID.input);
        FtcDashboard.getInstance().getTelemetry().addData("ErrY", ySpeedPID.setpoint - ySpeedPID.input);
        FtcDashboard.getInstance().getTelemetry().addData("ErrYaw", yawSpeedPID.setpoint - yawSpeedPID.input);
        FtcDashboard.getInstance().getTelemetry().update();
        double[] robotPowers = fieldToRobotPowers(forwardPower, horizontalPower, vectorRotate + currentYaw);
        FtcDashboard.getInstance().getTelemetry().update();
        return moveToDirection(robotPowers[0], robotPowers[1], turnPower);
    }

    private double[] fieldToRobotPowers(double powerXField, double powerYField, double yawDeg) {
        double yawRad = Math.toRadians(yawDeg);
        double powerXRobot = powerXField * Math.cos(yawRad) - powerYField * Math.sin(yawRad);
        double powerYRobot = powerXField * Math.sin(yawRad) + powerYField * Math.cos(yawRad);
        return new double[] { powerXRobot, powerYRobot };
    }


    public boolean isPosition(double x, double y, double yaw) {
        return Math.abs(x - OdometerPinpoint.getInstance().getX()) > 14 && Math.abs(y - OdometerPinpoint.getInstance().getY()) > 14 && Math.abs(yaw - OdometerPinpoint.getInstance().getYaw()) > 14;
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