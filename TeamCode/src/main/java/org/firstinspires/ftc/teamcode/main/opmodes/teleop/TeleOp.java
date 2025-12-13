package org.firstinspires.ftc.teamcode.main.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.separatorConfig;

import org.firstinspires.ftc.teamcode.main.modules.separator.Separator;
import org.firstinspires.ftc.teamcode.main.modules.transfer.Brush;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;

// coding by Matvey Ivanovv

/*
    EDGE - ПОБЕДА!
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="Dev")
public class TeleOp extends OpMode {
    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        Vehicles.getInstance().initialize(hardwareMap);
        Brush.getInstance().initialize(hardwareMap);
        GunControl.getInstance().initialize(hardwareMap);
        Separator.getInstance().initialize(hardwareMap);
//        Gyro.getInstance().initialize(hardwareMap);
//        Vision.getInstance().initialize(hardwareMap);
//        AprilTag.getInstance().initialize(hardwareMap);
        if (Vehicles.getInstance().isInitialized() &&
                GunControl.getInstance().isInitialized() &&
                Separator.getInstance().isInitialized()) {
            FtcDashboard.getInstance().getTelemetry().addData("Status", "Initialized");
        }
//        Vision.getInstance().startStreaming();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {

        FtcDashboard.getInstance().getTelemetry().addData("Velocity Gun:", GunControl.getInstance().getVelocity());
//        FtcDashboard.getInstance().getTelemetry().addData("Last color:", Separator.getInstance().getLastColor());
//        FtcDashboard.getInstance().getTelemetry().addData("Separator pos:", Separator.getInstance().getEncoderPos());
//        FtcDashboard.getInstance().getTelemetry().addData("Velocity Flow:", TransferBall.getInstance().getVelocityFlow());
        FtcDashboard.getInstance().getTelemetry().addData("Velocity Brush:", Brush.getInstance().getVelocityBrush());
        FtcDashboard.getInstance().getTelemetry().addData("Degree tower:", GunControl.getInstance().getTowerDegree());
        FtcDashboard.getInstance().getTelemetry().addData("Separator position", Separator.getInstance().getEncoderPos());
//        FtcDashboard.getInstance().getTelemetry().addData("OdometerX:", Vehicles.getInstance().getPositionOdometerX());
//        FtcDashboard.getInstance().getTelemetry().addData("OdometerY:", Vehicles.getInstance().getPositionOdometerY());
//        FtcDashboard.getInstance().getTelemetry().addData("Yaw:", Vehicles.getInstance.getYaw());
//        AprilTag.getInstance().telemetryAprilTag();
//        Vision.getInstance().stopStreaming();

        FtcDashboard.getInstance().getTelemetry().update();

        Brush.getInstance().startBrush();
        Separator.getInstance().turnToPosition(separatorConfig.POSITION_1);
        Separator.getInstance().kickToGun();
        GunControl.getInstance().startShot();
//        Separator.getInstance().startSeparator();
        Vehicles.getInstance().moveToDirection(gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        Vehicles.getInstance().moveToDirection(0, 0, 0);
        GunControl.getInstance().stopShot();
        Brush.getInstance().stopBrush();
    }
}