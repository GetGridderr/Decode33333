package org.firstinspires.ftc.teamcode.main.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.separatorConfig;

import org.firstinspires.ftc.teamcode.main.modules.separator.Separator;
import org.firstinspires.ftc.teamcode.main.modules.transfer.Brush;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;

// coding by Matvey Ivanovv

/*
    EDGE - ПОБЕДА!
 */

@TeleOp(name="TeleOpTest", group="Test")
@Config
public class TeleOpTest extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    public Motor left = new Motor("gun_motor_left");
    public static double power;
    public static double degreeGunTower = 0;
    public static double offset = 10;
    public static double R = 20;

    @Override
    public void init() {
        Vehicles.getInstance().initialize(hardwareMap);
        Brush.getInstance().initialize(hardwareMap);
        OdometerPinpoint.getInstance().initialize(hardwareMap);
        GunControl.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().initialize(hardwareMap);
        if (Vehicles.getInstance().isInitialized() &&
                GunControl.getInstance().isInitialized() &&
                TransferBall.getInstance().isInitialized()) {
            FtcDashboard.getInstance().getTelemetry().addData("Status", "Initialized");
        }
//        Vision.getInstance().startStreaming();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
        Odometry.getInstance().setPosition(0, 0);
        Odometry.getInstance().setYaw(0);
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        Odometry.getInstance().setPosition(0, 0);
        Odometry.getInstance().setYaw(0);
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        Odometry.getInstance().odometryTick();
        FtcDashboard.getInstance().getTelemetry().addData("Velocity Gun:", GunControl.getInstance().getVelocity());
//        FtcDashboard.getInstance().getTelemetry().addData("Last color:", Separator.getInstance().getLastColor());
//        FtcDashboard.getInstance().getTelemetry().addData("Separator pos:", Separator.getInstance().getEncoderPos());
//        FtcDashboard.getInstance().getTelemetry().addData("Velocity Flow:", TransferBall.getInstance().getVelocityFlow());
        FtcDashboard.getInstance().getTelemetry().addData("Velocity Brush:", Brush.getInstance().getVelocityBrush());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw robot:", OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().addData("Velocity Flow", TransferBall.getInstance().getVelocityFlow());

        FtcDashboard.getInstance().getTelemetry().update();

        TransferBall.getInstance().startBrush();
        GunControl.getInstance().startShot();
        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw(), offset, R);
        Vehicles.getInstance().moveToDirection(-gamepad1.left_stick_y * 0.8,
                -gamepad1.left_stick_x * 0.8,
                gamepad1.right_stick_x * 0.8);
        if (gamepad1.dpad_right) {
            TransferBall.getInstance().startFlow();
        } else if (!gamepad1.dpad_right) {
            TransferBall.getInstance().stopFlow();
        }

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