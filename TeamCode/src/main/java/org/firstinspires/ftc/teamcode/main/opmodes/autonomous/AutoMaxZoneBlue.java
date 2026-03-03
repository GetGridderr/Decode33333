package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.positionAutoRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
//import org.firstinspires.ftc.teamcode.main.opencv.AprilTag;


@Autonomous(name="AutoMaxBlue", group="Dev")
public class AutoMaxZoneBlue extends LinearOpMode {
    public static double pXY = 0.02;
    public static double dXY = 0.04;
    public static double pYaw = 0.018;
    public static double iYaw = 0.0;

    public static double dYaw = 0.026;
    public static double offset = 10;
    public static double distanceToTarget = 25;
    private final ElapsedTime runtime = new ElapsedTime();


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
            Vehicles.getInstance().setPosPID(pXY, 0, dXY, pXY, 0, dXY, pYaw, iYaw, dYaw);
            while (runtime.milliseconds() < 2000 && opModeIsActive()) {
                gunConfig.velocity = -1500;
                GunControl.getInstance().startShot();
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 5, gunConfig.offsetGun, gunConfig.distanceToTarget);
                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            }
            runtime.reset();
            while (runtime.milliseconds() < 4000 && opModeIsActive()) {
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 5, offset, distanceToTarget);
                gunConfig.velocity = -1500;
                TransferBall.getInstance().startFlowAuto();
                TransferBall.getInstance().startBrush();
                GunControl.getInstance().startShot();
            }

            runtime.reset();
            while (runtime.milliseconds() < 1200 && opModeIsActive()) {
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 5, gunConfig.offsetGun, gunConfig.distanceToTarget);
                Vehicles.getInstance().goTo(22, 140, -107);
                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - 5, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            }
            runtime.reset();
            while (runtime.milliseconds() < 2000 && opModeIsActive()) {
                TransferBall.getInstance().startBrush();
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 5, gunConfig.offsetGun, gunConfig.distanceToTarget);
                Vehicles.getInstance().goTo(-55, 140, -107);
                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX()- 5, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            }
            runtime.reset();
            while (runtime.milliseconds() < 1200 && opModeIsActive()) {
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 5, gunConfig.offsetGun, gunConfig.distanceToTarget);
                Vehicles.getInstance().goTo(0, 40, 0);
                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - 5, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            }
            runtime.reset();
            while (runtime.milliseconds() < 4000 && opModeIsActive()) {
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 5, offset, distanceToTarget);
                gunConfig.velocity = -1500;
                TransferBall.getInstance().startFlowAuto();
                TransferBall.getInstance().startBrush();
                GunControl.getInstance().startShot();
            }
            while (runtime.milliseconds() < 1200 && opModeIsActive()) {
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 5, gunConfig.offsetGun, gunConfig.distanceToTarget);
                Vehicles.getInstance().goTo(22, 121, -107);
                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            }
        }
    }
}
