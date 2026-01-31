package org.firstinspires.ftc.teamcode.main.test.modules.transfer;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.main.modules.gun.DataShots;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;

import android.view.View;

@TeleOp(name="TransferTest", group="Test")
@Config
public class TransferTest extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();
    public static double powerFlow = 0.6;
    public static double timeFlow = 50;
    public int count = 0;
    public boolean openFlag = true;

    @Override
    public void init() {
        GunControl.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().closeDoor();
//        OdometerPinpoint.getInstance().reset();
    }

    @Override
    public void loop() {
        if (count == -1) {
            TransferBall.getInstance().stopBrush();
            TransferBall.getInstance().stopFlow();
        } else {
            TransferBall.getInstance().startBrush();
            TransferBall.getInstance().stopFlow();
        }
        FtcDashboard.getInstance().getTelemetry().addData("FlowBrush", TransferBall.getInstance().getFlowBrush());
        FtcDashboard.getInstance().getTelemetry().update();
        if (TransferBall.getInstance().getFlowBrush() > 1.1 && openFlag) {
            count++;
            runtime.reset();
            while (runtime.milliseconds() < timeFlow) {
                TransferBall.getInstance().vel = powerFlow;
                TransferBall.getInstance().startFlow();
                TransferBall.getInstance().startBrush();
            }
            while (runtime.milliseconds() < timeFlow + 500) {}
        } else {
            TransferBall.getInstance().stopFlow();
            TransferBall.getInstance().stopBrush();
        }
        if (gamepad1.right_trigger > 0.1) {
            openFlag = true;
            runtime.reset();
            TransferBall.getInstance().startFlow();

        } else {
            TransferBall.getInstance().closeDoor();
        }
        if (gamepad1.left_trigger > 0.1) {
            GunControl.getInstance().startShot();
            TransferBall.getInstance().openDoor();
            TransferBall.getInstance().startBrush();
            TransferBall.getInstance().stopFlow();
            openFlag = false;
        }
        FtcDashboard.getInstance().getTelemetry().addData("left_trigger", gamepad1.left_trigger);
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
