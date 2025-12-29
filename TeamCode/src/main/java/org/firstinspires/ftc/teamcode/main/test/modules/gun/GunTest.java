package org.firstinspires.ftc.teamcode.main.test.modules.gun;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;

@TeleOp(name="GunTest", group="Test")
@Config
public class GunTest extends OpMode {
    public static double degreeGunTower = 0;
    public static double offset = 10;
    public static double R = 20;
    public Motor left = new Motor("gun_motor_left");
    public static double power;
    @Override
    public void init() {
        OdometerPinpoint.getInstance().initialize(hardwareMap);
        left.initialize(hardwareMap);
//        Odometry.getInstance().setPosition(0, 0);
//        Odometry.getInstance().setYaw(0);
        GunControl.getInstance().initialize(hardwareMap);
    }

    @Override
    public void loop() {
//        left.setPower(power);
        FtcDashboard.getInstance().getTelemetry().addData("Degree", OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().update();
//        GunControl.getInstance().setAngleDegree(OdometerPinpoint.getInstance().getYaw());
//        left.setPower(0.5);
//        GunControl.getInstance().startShot();
        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw(), offset, R);
//        GunControl.getInstance().setTowerDegree(power);
//        GunControl.getInstance().testPower();
        //GunControl.getInstance().aimToAprilTag();
        //GunControl.getInstance().setTowerDegree(degreeGunTower);
    }
}
