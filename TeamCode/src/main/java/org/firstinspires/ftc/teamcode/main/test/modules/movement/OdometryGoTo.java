package org.firstinspires.ftc.teamcode.main.test.modules.movement;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.positionAutoRed;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.device.sensor.SensorVoltage;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
import org.firstinspires.ftc.teamcode.main.opencv.AprilTag;

@Autonomous(name="OdometryGoTo", group="Test")
@Config

public class OdometryGoTo extends LinearOpMode {

    public static double pXY = 0.02;
    public static double dXY = 0.04;
    public static double pYaw = 0.018;
    public static double iYaw = 0.0;
    public static double dYaw = 0.026;
    public static double offset = 10;
    public static double distanceToTarget = 25;

    public static double x = 0;
    public static double y = 0;
    public static double yaw = 0;
    private final ElapsedTime runtime = new ElapsedTime();

    public static int count = 0;

    public void initialize() {
        Vehicles.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().initialize(hardwareMap);
        GunControl.getInstance().initialize(hardwareMap);
        if (Vehicles.getInstance().isInitialized()) {
            telemetry.addData("Status", "Initialized");
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            Odometry.getInstance().odometryTick();
            Vehicles.getInstance().setPosPID(vehicles.pX, 0, vehicles.dX, vehicles.pY, 0, vehicles.dY, vehicles.pYaw, vehicles.iYaw, vehicles.dYaw);
            FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            FtcDashboard.getInstance().getTelemetry().addData("X Pos:", OdometerPinpoint.getInstance().getX());
            FtcDashboard.getInstance().getTelemetry().addData("Y Pos:", OdometerPinpoint.getInstance().getY());
            FtcDashboard.getInstance().getTelemetry().addData("Yaw:", OdometerPinpoint.getInstance().getYaw());
            FtcDashboard.getInstance().getTelemetry().addData("YawErr", Vehicles.getInstance().yawErr);
            FtcDashboard.getInstance().getTelemetry().update();


            Vehicles.getInstance().goTo(x, y, yaw);
        }
    }
}