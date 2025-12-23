package org.firstinspires.ftc.teamcode.main.test.modules.gun;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;

@TeleOp(name="GunTest", group="Test")
@Config
public class GunTest extends OpMode {
    public static double degreeGunTower = 0;
    public Motor left = new Motor("gun_motor_left");
    public static double power;
    @Override
    public void init() {

        GunControl.getInstance().initialize(hardwareMap);
    }

    @Override
    public void loop() {
//        left.setPower(power);
        FtcDashboard.getInstance().getTelemetry().addData("Velocity gun", GunControl.getInstance().getSpeedGun());
        GunControl.getInstance().startShot();
        FtcDashboard.getInstance().getTelemetry().update();
//        GunControl.getInstance().testPower();
        //GunControl.getInstance().aimToAprilTag();
        //GunControl.getInstance().setTowerDegree(degreeGunTower);
    }
}
