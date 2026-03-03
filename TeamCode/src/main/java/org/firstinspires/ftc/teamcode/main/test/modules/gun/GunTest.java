package org.firstinspires.ftc.teamcode.main.test.modules.gun;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;

@TeleOp(name="GunTest", group="Test")
@Config
public class GunTest extends OpMode {
    public Motor left = new Motor("gun_motor_left");
    public static double xGoal = 140;
    public static double yGoal = 145;

    @Override
    public void init() {
        OdometerPinpoint.getInstance().initialize(hardwareMap);
        left.initialize(hardwareMap);
        GunControl.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().initialize(hardwareMap);
        OdometerPinpoint.getInstance().reset();
    }

    @Override
    public void loop() {
//        DataShots dataShots = GunControl.getInstance().shotToDistance(OdometerPinpoint.getInstance().getDistanceToTarget(0, 0));
//        gunConfig.velocity = 0;
//        gunConfig.bananPosition = gunConfig.bananPosition;
        GunControl.getInstance().startShot();
//        GunControl.getInstance().startShot();
//        GunControl.getInstance().setBananServo();
//        GunControl.getInstance().setTowerDegree(xGoal, yGoal, OdometerPinpoint.getInstance().getX(), OdometerPinpoint.getInstance().getY(), OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().addData("X", OdometerPinpoint.getInstance().getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y", OdometerPinpoint.getInstance().getY());
        FtcDashboard.getInstance().getTelemetry().addData("distance", OdometerPinpoint.getInstance().getDistanceToTarget(xGoal, yGoal));
        FtcDashboard.getInstance().getTelemetry().addData("Speed", GunControl.getInstance().getSpeedGun());
        FtcDashboard.getInstance().getTelemetry().update();
//        TransferBall.getInstance().startFlow();
//        TransferBall.getInstance().startBrush();
    }
}
