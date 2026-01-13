package org.firstinspires.ftc.teamcode.main.movement;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
    private final Motor separatorMotor;
    public double yawErr = 0;
    public boolean isBusy = false;
    public static double power = 1;

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
                &&  OdometerPinpoint.getInstance().isInitialized()
                && separatorMotor.isInitialized();
    }

    public void stopAll() {
        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
    }

    public double[] fieldCentricToRobotCentric(double forward, double horizontal, double turn) {
        double turnRad = Math.toRadians(turn);
        double rotatedFroward = forward * Math.cos(-turnRad) - horizontal * Math.sin(-turnRad);
        double rotatedHorizontal = forward * Math.sin(-turnRad) + horizontal * Math.cos(-turnRad);
        return new double[] {rotatedFroward, rotatedHorizontal};
    }

    public boolean moveToDirection(double forward,
                                   double horizontal,
                                   double turn,
                                   boolean normalize) {

        if(forward == 0 && horizontal == 0 && turn == 0) {
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

        leftFrontMotor.setPower(Motor.normalizePower(frontLeftPower) * power);
        leftBackMotor.setPower(Motor.normalizePower(backLeftPower) * power);
        rightFrontMotor.setPower(Motor.normalizePower(frontRightPower) * power);
        rightBackMotor.setPower(Motor.normalizePower(backRightPower) * power);
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean moveToDirection(double forward, double horizontal, double turn) {
        return moveToDirection(forward, horizontal, turn, false);
    }

    // coding by Timofei
    private boolean goTo(double x, double y, double yaw, boolean posReg, boolean yawReg) {
        double xSpd = 0, ySpd = 0, yawSpd = 0;

        if(posReg) {
            double xErr = OdometerPinpoint.getInstance().getX() - x;
            double yErr = OdometerPinpoint.getInstance().getY() - y;
            if (Math.abs(xErr) < 8 && Math.abs(yErr) < 8) return false;
            double[] errVector = Odometry.rotateVector(xErr, yErr, -OdometerPinpoint.getInstance().getYaw());
            xErr = errVector[0];
            yErr = errVector[1];
            xSpd = xPosPID.PIDGet(-xErr);
            ySpd = yPosPID.PIDGet(yErr);
            isBusy = true;
        }
        yawErr = OdometerPinpoint.getInstance().getShortestPathToAngle(OdometerPinpoint.getInstance().getYaw(), yaw);
        if(yawReg) {
            yawSpd = yawPosPID.PIDGet(yawErr);
        }
        FtcDashboard.getInstance().getTelemetry().addData("ErrYaw", yawErr);
        FtcDashboard.getInstance().getTelemetry().addData("X speed", Motor.normalizePower(xSpd));
        FtcDashboard.getInstance().getTelemetry().addData("Y speed", Motor.normalizePower(ySpd));
        FtcDashboard.getInstance().getTelemetry().addData("Yaw speed", Motor.normalizePower(yawSpd));

        FtcDashboard.getInstance().getTelemetry().update();
        return moveToDirection(ySpd * 0.8, xSpd * 0.8, yawSpd * 0.8, true);
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