package org.firstinspires.ftc.teamcode.main.test;//package org.firstinspires.ftc.teamcode.main.test;
//
//import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;
//import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
//import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
//import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
//import org.firstinspires.ftc.teamcode.main.modules.gun.DataShots;
//import org.firstinspires.ftc.teamcode.main.modules.transfer.Brush;
//import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
//import org.firstinspires.ftc.teamcode.main.movement.Odometry;
//import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
//import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
//
//// coding by Matvey Ivanovv
//
///*
//    EDGE - 33333!
//    AIR лучше
// */
//
//@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOpTest", group="Dev")
//@Config
//public class TeleOpTest extends OpMode {
//    private final ElapsedTime runtime = new ElapsedTime();
//    public static double power = 0.7;
//    public static double degreeOffset = 135;
//    public static double xGoal = 125;
//    public static double yGoal = 137;
//    public static double pYaw = 0.018;
//    public static double iYaw = 0.0;
//    public static double dYaw = 0.026;
//    public static double powerFlow = 0.6;
//    public static double timeFlow = 50;
//    public boolean lockGun = true;
//    public boolean openFlag = true;
//    public double count = 0;
//    public double countBall = 0;
//
//    @Override
//    public void init() {
//        Vehicles.getInstance().initialize(hardwareMap);
//        Brush.getInstance().initialize(hardwareMap);
//        OdometerPinpoint.getInstance().initialize(hardwareMap);
//        GunControl.getInstance().initialize(hardwareMap);
//        TransferBall.getInstance().initialize(hardwareMap);
//        if (Vehicles.getInstance().isInitialized() &&
//                GunControl.getInstance().isInitialized() &&
//                TransferBall.getInstance().isInitialized()) {
//            FtcDashboard.getInstance().getTelemetry().addData("Status", "Initialized");
//        }
//    }
//
//    /*
//     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
//     */
//    @Override
//    public void init_loop() {
//        Odometry.getInstance().setPosition(0, 0);
//        Odometry.getInstance().setYaw(0);
//    }
//
//    /*
//     * Code to run ONCE when the driver hits START
//     */
//    @Override
//    public void start() {
//        Odometry.getInstance().setPosition(0, 0);
//        Odometry.getInstance().setYaw(0);
//        TransferBall.getInstance().closeDoor();
//        runtime.reset();
//    }
//
//    /*
//     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
//     */
//    @Override
//    public void loop() {
//        Odometry.getInstance().odometryTick();
//        FtcDashboard.getInstance().getTelemetry().addData("Distance", OdometerPinpoint.getInstance().getDistanceToTarget(0, 0));
//        FtcDashboard.getInstance().getTelemetry().addData("Velocity Gun:", GunControl.getInstance().getVelocity());
//        FieldView.renderRobot(OdometerPinpoint.getInstance().getX() + 21, OdometerPinpoint.getInstance().getY() - 60, OdometerPinpoint.getInstance().getYaw() - 45);
//        FtcDashboard.getInstance().getTelemetry().addData("SpeedX", OdometerPinpoint.getInstance().getSpeedX());
//        FtcDashboard.getInstance().getTelemetry().addData("SpeedY", OdometerPinpoint.getInstance().getSpeedY());
//        FtcDashboard.getInstance().getTelemetry().addData("Velocity Brush:",Brush.getInstance().getVelocityBrush());
//        FtcDashboard.getInstance().getTelemetry().addData("Yaw robot:", OdometerPinpoint.getInstance().getYaw());
//        FtcDashboard.getInstance().getTelemetry().addData("Velocity Flow", TransferBall.getInstance().getVelocityFlow());
//        FtcDashboard.getInstance().getTelemetry().addData("Trigger", gamepad1.right_trigger);
//        Vehicles.getInstance().setPosPID(vehicles.pX, vehicles.isX, vehicles.dX, vehicles.pY, vehicles.isY, vehicles.dY, vehicles.pYaw, vehicles.iYaw, vehicles.dYaw);
//        Vehicles.getInstance().setSpeedPID(vehicles.psX, 0, vehicles.dsX, vehicles.psY, 0, vehicles.dsY, vehicles.psYaw, vehicles.isYaw, vehicles.dsYaw);
//
//        FtcDashboard.getInstance().getTelemetry().update();
//        double forward = gamepad1.left_stick_y;
//        double horizontal = -gamepad1.left_stick_x;
//        double turn = -gamepad1.right_stick_x;
//        if (Math.abs(forward) < 0.15) forward = 0;
//        if (Math.abs(horizontal) < 0.15) horizontal = 0;
//        if (Math.abs(turn) < 0.15) turn = 0;
//
//
//        if (gamepad1.dpad_right) {
//            Vehicles.getInstance().setPosPID(vehicles.pX, 0, vehicles.dX, vehicles.pY, 0, vehicles.dY, vehicles.pYaw, vehicles.iYaw, vehicles.dYaw);
//            Vehicles.getInstance().goTo(80, 90, -140);
//        } else {
//            Vehicles.getInstance().setSpeed(forward, horizontal, turn);
//        }
//
//
//
//
//    }
//
//    @Override
//    public void stop() {
//        Vehicles.getInstance().moveToDirection(0, 0, 0);
//        GunControl.getInstance().stopShot();
//        Brush.getInstance().stopBrush();
//    }
//}
