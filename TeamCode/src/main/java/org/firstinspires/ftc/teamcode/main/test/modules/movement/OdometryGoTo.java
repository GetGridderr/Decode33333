package org.firstinspires.ftc.teamcode.main.test.modules.movement;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.device.single.Gyro;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;

// coding by Timofei

@TeleOp(name="OdometryGoTo", group="Test")
@Config
public class OdometryGoTo extends OpMode
{

    public static double pX = 0.07, iX = 0, dX = 0.1;
    public static double pY = 0.07, iY = 0, dY = 0.1;
    public static double pYaw = 0.03, iYaw = 0, dYaw = 0.12;

    public static double targetX = 0, targetY = 20, targetYaw = 0;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        Vehicles.getInstance().initialize(hardwareMap);
        OdometerPinpoint.getInstance().initialize(hardwareMap);
        if (Vehicles.getInstance().isInitialized() && OdometerPinpoint.getInstance().isInitialized())
        { telemetry.addData("Status", "Initialized"); }
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
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        Odometry.getInstance().odometryTick();

        FtcDashboard.getInstance().getTelemetry().addData("X Pos:", OdometerPinpoint.getInstance().getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y Pos:", OdometerPinpoint.getInstance().getY());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw:", OdometerPinpoint.getInstance().getYaw());
        FieldView.renderRobot(Odometry.getInstance().getX(), Odometry.getInstance().getY(), Odometry.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().update();

        Vehicles.getInstance().setPosPID(
                pX, iX, dX,
                pY, iY, dY,
                pYaw, iYaw, dYaw
        );
//        if (gamepad1.square) {
//            Vehicles.getInstance().goTo(0, 0);
//        } else if (gamepad1.circle) {
//            Vehicles.getInstance().rotateTo(0);
//        } else if (gamepad1.triangle) {
//            Vehicles.getInstance().goTo(0, 0, 0);
//        } else {
//            Vehicles.getInstance().moveToDirection(-gamepad1.left_stick_y,
//                    gamepad1.left_stick_x,
//                    gamepad1.right_stick_x, true);
//        }
        if(((long) (System.nanoTime() / 1_000_000_000L)) % 6 < 3) {
            Vehicles.getInstance().goTo(0, 0, 0);
        } else {
            Vehicles.getInstance().goTo(targetX, targetY, targetYaw);
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        Vehicles.getInstance().moveToDirection(0, 0, 0);
    }

}