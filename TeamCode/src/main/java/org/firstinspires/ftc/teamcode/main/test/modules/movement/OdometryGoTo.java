package org.firstinspires.ftc.teamcode.main.test.modules.movement;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDCoefficients;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;

@TeleOp(name="OdometryGoTo", group="Test")
@Config
public class OdometryGoTo extends OpMode
{
    public static double pX = 0.0;
    public static double iX = 0.0;
    public static double dX = 0.0;
    public static double pY = 0.0;
    public static double iY = 0.0;
    public static double dY = 0.0;
    public static double pYaw = 0.0;
    public static double iYaw = 0.0;
    public static double dYaw = 0.0;
    @Override
    public void init() {
        Vehicles.getInstance().initialize(hardwareMap);
        if (Vehicles.getInstance().isInitialized())
        { telemetry.addData("Status", "Initialized"); }
    }

    @Override
    public void init_loop() {
        Odometry.getInstance().setPosition(0, 0);
        Odometry.getInstance().setYaw(0);
    }


    @Override
    public void start() {
        Odometry.getInstance().setPosition(0, 0);
        Odometry.getInstance().setYaw(0);
    }

    @Override
    public void loop() {
        Odometry.getInstance().odometryTick();

        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX(), Vehicles.getInstance().getPositionOdometerY(), OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().addData("X Pos:", OdometerPinpoint.getInstance().getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y Pos:", OdometerPinpoint.getInstance().getY());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw:", OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().update();


        Vehicles.getInstance().setPosPID(pX, iX, dX, pY, iY, dY, pYaw, iYaw, dYaw);
//        Vehicles.getInstance().goTo(vehicles.targetX, vehicles.targetY, vehicles.targetYaw);
        if (gamepad1.dpad_right) {
            FtcDashboard.getInstance().getTelemetry().addData("Satrt PID", true);
            FtcDashboard.getInstance().getTelemetry().update();
            Vehicles.getInstance().goTo(vehicles.targetX, vehicles.targetY, vehicles.targetYaw);
        } else {
            Vehicles.getInstance().moveToDirection(gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    -gamepad1.right_stick_x);
        }

    }

    @Override
    public void stop() {
        Vehicles.getInstance().moveToDirection(0, 0, 0);
    }

}