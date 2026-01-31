package org.firstinspires.ftc.teamcode.main.modules.transfer;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.core.device.motor.Motor;

@Config
public class TransferBall implements Initializable {
    private static final TransferBall INSTANCE = new TransferBall();
    private final Motor motorFlow;
    private final Motor motorBrush;
    private final Servomotor servoDoor;
    public static double velocityFlow = 1;
    public static double velocityBrush = 1.0;
    public static double vel = 1.0;
    public double powerBrush = 0;
    public static double degreeServo = 0.6;

    public TransferBall() {
        motorFlow = new Motor("motor_flow");
        motorBrush = new Motor("motor_brush");
        servoDoor = new Servomotor("servo_door");
    }

    public static TransferBall getInstance() { return INSTANCE; }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        motorFlow.initialize(hardwareMap);
        motorBrush.initialize(hardwareMap);
        servoDoor.initialize(hardwareMap);
        motorBrush.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        motorBrush.setDirection(motorBrush.getDirection().inverted());
    }

    @Override
    public boolean isInitialized() { return motorFlow.isInitialized(); }

    public void setVelocityFlow(double velocity) { velocityFlow = velocity; }

    public double getVelocityFlow() { return velocityFlow; }

    public void setVelocityBrush(double velocity) { velocityBrush = velocity; }

    public double getVelocityBrush() { return velocityBrush; }

    public double getFlowBrush() {
        return motorBrush.getCurrent(CurrentUnit.AMPS);
    }

    public double getDegreeServo() { return degreeServo; }

    public void startBrush() {
        powerBrush = motorBrush.getPower();
        FtcDashboard.getInstance().getTelemetry().addData("powerBrush", motorBrush);
        motorBrush.setPower(velocityBrush);
    }

    public void stopBrush() { motorBrush.setPower(0); }

    public void startFlow() {
        if (velocityFlow == 1) {
            motorFlow.setPower(vel);
        } else if (velocityFlow == 0) {
            motorFlow.setPower(0.0);
        }
    }

    public void openDoor() {
        servoDoor.setServoPosition(0.8);
    }
    public void closeDoor() {
        servoDoor.setServoPosition(1);
    }

    public void startFlowAuto() {
        motorFlow.setPower(0.85);
    }

    public void setVelFlow(double velo) {
        vel = velo;
    }

    public void stopFlow() { motorFlow.setPower(0); }

}
