package org.firstinspires.ftc.teamcode.main.test.modules.gun;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;

@TeleOp(name="GunTest", group="Test")
@Config
public class GunTest extends OpMode {
    public static double degreeGunTower = 0;
    @Override
    public void init() {
        GunControl.getInstance().initialize(hardwareMap);
    }

    @Override
    public void loop() {
        GunControl.getInstance().startShot();
        GunControl.getInstance().aimToAprilTag();
        GunControl.getInstance().setTowerDegree(degreeGunTower);
    }
}
