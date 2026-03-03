package org.firstinspires.ftc.teamcode.main.modules.transfer;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.transferConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;

@Config
public class TransferBall implements Initializable {
    private static final TransferBall INSTANCE = new TransferBall();
    private final Motor motorFlow;
    private final Motor motorBrush;
    private final Servomotor servoDoor;
    public double powerBrush = 0;
    public ElapsedTime timeForFlow = new ElapsedTime();

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
        motorBrush.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public boolean isInitialized() { return motorFlow.isInitialized(); }

    public double getVelocityFlow() { return transferConfig.velocityFlow; }

    public void setVelocityBrush(double velocity) { transferConfig.velocityBrush = velocity; }

    public double getVelocityBrush() { return transferConfig.velocityBrush; }

    public double getFlowBrush() {
        return motorBrush.getCurrent(CurrentUnit.AMPS);
    }

    public void startBrush() {
        powerBrush = motorBrush.getPower();
        FtcDashboard.getInstance().getTelemetry().addData("powerBrush", motorBrush);
        motorBrush.setPower(transferConfig.velocityBrush);
    }

    public void startPulseFlow() {
        transferConfig.velocityFlow = 0.6;
        if (timeForFlow.milliseconds() < transferConfig.timeFlowUp) {
            motorFlow.setPower(transferConfig.velocityFlow);
        } else if (timeForFlow.milliseconds() > transferConfig.timeFlowUp && timeForFlow.milliseconds() < transferConfig.timeFlowReverse) {
            motorFlow.setPower(transferConfig.getVelocityReverseFlow);
        } else if (timeForFlow.milliseconds() > transferConfig.timeFlowReverse) timeForFlow.reset();
    }

    public void reverse() {
        setVelFlow(-transferConfig.velocityFlow);
        startFlow();
    }

    public void stopBrush() { motorBrush.setPower(0); }

    public void startFlow() {
        transferConfig.velocityFlow = 1.0;
        motorFlow.setPower(transferConfig.velocityFlow);
    }

    public void openDoor() {
        servoDoor.setServoPosition(transferConfig.openDoor);
    }
    public void closeDoor() {
        servoDoor.setServoPosition(transferConfig.closeDoor);
    }

    public void startFlowAuto() {
        motorFlow.setPower(0.85);
    }

    public void setVelFlow(double vel) {
        transferConfig.velocityFlow = vel;
    }

    public void stopFlow() { motorFlow.setPower(0); }

}
