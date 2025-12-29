package org.firstinspires.ftc.teamcode.main.opmodes.teleop;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.separatorConfig;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.modules.separator.Separator;
import org.firstinspires.ftc.teamcode.main.modules.transfer.Brush;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;

// coding by Matvey Ivanovv

/*
    EDGE - ПОБЕДА!
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOpBlue", group="Dev")
@Config
public class TeleOp extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    public Motor left = new Motor("gun_motor_left");
    public static double power = 0.8;
    public static double degreeGunTower = 0;
    public static double offset = 10;
    public static double R = 15;
    public static double pXY = 0.02;
    public static double dXY = 0.04;
    public static double pYaw = 0.018;
    public static double iYaw = 0.0;
    public static double dYaw = 0.026;

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

        gunConfig.velocity = -1000;
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
        FieldView.renderRobot(OdometerPinpoint.getInstance().getX() + 21, OdometerPinpoint.getInstance().getY() - 60, OdometerPinpoint.getInstance().getYaw() - 45);
//        FtcDashboard.getInstance().getTelemetry().addData("Last color:", Separator.getInstance().getLastColor());
//        FtcDashboard.getInstance().getTelemetry().addData("Separator pos:", Separator.getInstance().getEncoderPos());
//        FtcDashboard.getInstance().getTelemetry().addData("Velocity Flow:", TransferBall.getInstance().getVelocityFlow());
        FtcDashboard.getInstance().getTelemetry().addData("Velocity Brush:", Brush.getInstance().getVelocityBrush());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw robot:", OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().addData("Velocity Flow", TransferBall.getInstance().getVelocityFlow());
        FtcDashboard.getInstance().getTelemetry().addData("Trigger", gamepad1.right_trigger);
        Vehicles.getInstance().setPosPID(pXY, 0, dXY, pXY, 0, dXY, pYaw, iYaw, dYaw);

        FtcDashboard.getInstance().getTelemetry().update();

        TransferBall.getInstance().startBrush();
        GunControl.getInstance().startShot();
        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw(), offset, R);
        if (gamepad1.right_bumper) {
            power = 0.3;
        }
        if (gamepad1.right_trigger > 0.1) {
            TransferBall.getInstance().startFlow();
        }
        if (gamepad1.right_trigger < 0.1) {
            TransferBall.getInstance().stopFlow();
        }
        if (gamepad1.square) {
            OdometerPinpoint.getInstance().reset();
        }
        Vehicles.getInstance().moveToDirection(gamepad1.left_stick_y * power,
                -gamepad1.left_stick_x * power,
                gamepad1.right_stick_x * power);
        power = 0.8;

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