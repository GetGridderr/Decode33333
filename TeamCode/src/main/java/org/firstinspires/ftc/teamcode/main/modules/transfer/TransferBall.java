package org.firstinspires.ftc.teamcode.main.modules.transfer;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.core.device.motor.Motor;

@Config
public class TransferBall implements Initializable {
    private static final TransferBall INSTANCE = new TransferBall();
    private final Motor motorFlow;
    private final Motor motorBrush;
    public static double velocityFlow = 1;
    public static double velocityBrush = 0.8;
    public static double vel = 0.7;
    public static double degreeServo = 0.6;

    public TransferBall() {
        motorFlow = new Motor("motor_flow");
        motorBrush = new Motor("motor_brush");
    }

    public static TransferBall getInstance() { return INSTANCE; }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        motorFlow.initialize(hardwareMap);
        motorBrush.initialize(hardwareMap);
//        motorBrush.setDirection(motorBrush.getDirection().inverted());
    }

    @Override
    public boolean isInitialized() { return motorFlow.isInitialized(); }

    public void setVelocityFlow(double velocity) { velocityFlow = velocity; }

    public double getVelocityFlow() { return velocityFlow; }

    public void setVelocityBrush(double velocity) { velocityBrush = velocity; }

    public double getVelocityBrush() { return velocityBrush; }

//    public void setDegreeServo(double degree) {
//        degreeServo = degree;
//        servoToGun.setServoPosition(degreeServo);
//    }

    public double getDegreeServo() { return degreeServo; }

    public void startBrush() { motorBrush.setPower(velocityBrush); }

    public void stopBrush() { motorBrush.setPower(0); }

    public void startFlow() {
        if (velocityFlow == 1) {
            motorFlow.setPower(vel);
        } else if (velocityFlow == 0) {
            motorFlow.setPower(0.0);
        }
    }

    public void startFlowAuto() {
        motorFlow.setPower(0.5);
    }

    public void stopFlow() { motorFlow.setPower(0); }

}
