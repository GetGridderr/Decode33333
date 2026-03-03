package org.firstinspires.ftc.teamcode.main.test.modules.gun;//package org.firstinspires.ftc.teamcode.main.test.modules.gun;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
//import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
//import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
//import org.firstinspires.ftc.teamcode.main.movement.Odometry;
//
//@TeleOp(name="RotateTest", group="Test")
//@Config
//public class RotateTest extends OpMode {
//    public static double xGoal = -62;
//    public static double yGoal = -100;
//    public static double power;
//    @Override
//    public void init() {
//        OdometerPinpoint.getInstance().initialize(hardwareMap);
//        Odometry.getInstance().setPosition(0, 0);
//        Odometry.getInstance().setYaw(0);
//        GunControl.getInstance().initialize(hardwareMap);
//        OdometerPinpoint.getInstance().reset();
//    }
//
//    @Override
//    public void loop() {
//        FtcDashboard.getInstance().getTelemetry().addData("Degree robot", OdometerPinpoint.getInstance().getYaw());
//        FtcDashboard.getInstance().getTelemetry().addData("X robot", OdometerPinpoint.getInstance().getX());
//        FtcDashboard.getInstance().getTelemetry().addData("Y robot", OdometerPinpoint.getInstance().getY());
//        FtcDashboard.getInstance().getTelemetry().update();
//        GunControl.getInstance().setTowerDegree(xGoal, yGoal, OdometerPinpoint.getInstance().getX(), OdometerPinpoint.getInstance().getY(), OdometerPinpoint.getInstance().getYaw());
//    }
//}
