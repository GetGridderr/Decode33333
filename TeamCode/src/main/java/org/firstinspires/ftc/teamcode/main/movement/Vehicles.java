package org.firstinspires.ftc.teamcode.main.movement;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDRegulator;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;


public final class Vehicles implements Initializable {
    private static final Vehicles INSTANCE = new Vehicles();

    private OdometerPinpoint pinpoint;

    private final Motor leftFrontMotor;
    private final Motor rightFrontMotor;
    private final Motor leftBackMotor;
    private final Motor rightBackMotor;
    private final Motor separatorMotor;

    private final PIDRegulator xPosPID = new PIDRegulator(1, 1, 1);
    private final PIDRegulator yPosPID = new PIDRegulator(1, 1, 1);
    private final PIDRegulator yawPosPID = new PIDRegulator(1, 1, 1);


    private Vehicles() {
        leftFrontMotor= new Motor("left_front_vehicle_motor");
        rightFrontMotor= new Motor("right_front_vehicle_motor"); //
        leftBackMotor = new Motor("left_back_vehicle_motor");
        rightBackMotor = new Motor("right_back_vehicle_motor");
        separatorMotor = new Motor("motor_separator");
        pinpoint = new OdometerPinpoint("pinpoint");
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
        separatorMotor.initialize(hardwareMap);
        pinpoint.initialize(hardwareMap);

        /*
         * Motor by default rotates clockwise
         * so right motors relative to left
         * motors will rotate backward.
         * We should change their direction.
         */
//        leftBackMotor.invertDirection();
//        leftFrontMotor.invertDirection();
        rightFrontMotor.invertDirection();
//        odometerX.resetEncoder();
//        odometerY.resetEncoder();
    }

    @Override
    public boolean isInitialized() {
        return leftFrontMotor.isInitialized()
                && rightFrontMotor.isInitialized()
                && leftBackMotor.isInitialized()
                && rightBackMotor.isInitialized()
                && pinpoint.isInitialized()
                && separatorMotor.isInitialized();
    }

    public void stopAll() {
        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
    }

    public boolean moveToDirection(double forward,
                                   double horizontal,
                                   double turn,
                                   boolean normalize) {
        double deadZone = 0.2;
        if (Math.abs(forward) < deadZone) forward = 0;
        if (Math.abs(horizontal) < deadZone) horizontal = 0;
        if (Math.abs(turn) < deadZone) turn = 0;

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

    // coding by Timofei
    private boolean goTo(double x, double y, double yaw, boolean posReg, boolean yawReg) {
        double xSpd = 0, ySpd = 0, yawSpd = 0;

        if(posReg) {
            double xErr = x - pinpoint.getX();
            double yErr = y - pinpoint.getY();
            double[] errVector = Odometry.rotateVector(xErr, yErr, -OdometerPinpoint.getInstance().getYaw());
            xErr = errVector[0];
            yErr = errVector[1];
            xSpd = xPosPID.PIDGet(-xErr);
            ySpd = yPosPID.PIDGet(-yErr);
        }

        if(yawReg) {
            double yawErr = OdometerPinpoint.getInstance().getShortestPathToAngle(Odometry.getInstance().getYaw(), yaw);
            yawSpd = yawPosPID.PIDGet(-yawErr);
        }
        return moveToDirection(ySpd * 0.5, xSpd * 0.5, yawSpd * 0.5, true);
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

    public double getPositionOdometerX() {
        return OdometerPinpoint.getInstance().getX();
    }

    public double getPositionOdometerY() {
        return OdometerPinpoint.getInstance().getY();
    }

    public double getGyroYaw() { return OdometerPinpoint.getInstance().getYaw(); }
}