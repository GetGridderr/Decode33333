package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.positionAutoRed;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;

import com.acmerobotics.dashboard.FtcDashboard;
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

@Autonomous(name="AutonomusRedTower", group="Dev")
public class AutonomusRed extends LinearOpMode {
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
            FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            FtcDashboard.getInstance().getTelemetry().addData("X Pos:", OdometerPinpoint.getInstance().getX());
            FtcDashboard.getInstance().getTelemetry().addData("Y Pos:", OdometerPinpoint.getInstance().getY());
            FtcDashboard.getInstance().getTelemetry().addData("Yaw:", OdometerPinpoint.getInstance().getYaw());
            FtcDashboard.getInstance().getTelemetry().addData("YawErr", Vehicles.getInstance().yawErr);
            FtcDashboard.getInstance().getTelemetry().update();


            Vehicles.getInstance().setPosPID(vehicles.pX, 0, vehicles.dX, vehicles.pY, 0, vehicles.dY, vehicles.pYaw, vehicles.iYaw, vehicles.dYaw);
//        Vehicles.getInstance().goTo(vehicles.targetX, vehicles.targetY, vehicles.targetYaw);
            FtcDashboard.getInstance().getTelemetry().addData("Satrt PID", true);
            FtcDashboard.getInstance().getTelemetry().update();
            while (runtime.milliseconds() < 1400 && opModeIsActive()) {
                Vehicles.getInstance().goTo(35, -51, 0);
                GunControl.getInstance().startShot();
                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
            }
            TransferBall.getInstance().vel = 1;
            runtime.reset();
            while (runtime.milliseconds() < 1500) {
                TransferBall.getInstance().startFlow();
                TransferBall.getInstance().stopBrush();
                GunControl.getInstance().startShot();
            }
            TransferBall.getInstance().stopFlow();
            GunControl.getInstance().stopShot();
            TransferBall.getInstance().closeDoor();
            TransferBall.getInstance().stopBrush();
            while (runtime.milliseconds() < 20000) {}

//            while (Vehicles.getInstance().goTo(50, -40, 90) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(50, -40, 90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            while (Vehicles.getInstance().goTo(120, 10,90) && opModeIsActive()) {
//                TransferBall.getInstance().startBrush();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(120, 10,90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 400) {
//
//            }
//
//
//            runtime.reset();
//            while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(-35, -51, 0);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                GunControl.getInstance().startShot();
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 1700 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                GunControl.getInstance().startShot();
//                TransferBall.getInstance().startFlow();
//                Vehicles.getInstance().moveToDirection(0, 0, 0);
//                TransferBall.getInstance().startBrush();
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().stopShot();
//
//
//
//            runtime.reset();
//            while (Vehicles.getInstance().goTo(0, -190, 90) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(0, -190, 90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//
//            runtime.reset();
//            while (Vehicles.getInstance().goTo(103, -190, 90) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                TransferBall.getInstance().startBrush();
//                Vehicles.getInstance().goTo(103, -190, 90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//
//            runtime.reset();
//            while (runtime.milliseconds() < 2200 && opModeIsActive()) {
//                Vehicles.getInstance().goTo(-35, -51, 0);
//                GunControl.getInstance().startShot();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 400) {
//
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 1700 && opModeIsActive()) {
//                TransferBall.getInstance().startBrush();
//                GunControl.getInstance().startShot();
//                Vehicles.getInstance().moveToDirection(0, 0, 0);
//                TransferBall.getInstance().startFlow();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            runtime.reset();
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//            FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            GunControl.getInstance().stopShot();
//
//
//            runtime.reset();
//            while (Vehicles.getInstance().goTo(0, -173, 90) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(0, -173, 90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 4200 && opModeIsActive()) {
//                TransferBall.getInstance().startBrush();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(120, -173,130);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 400) {
//                TransferBall.getInstance().startBrush();
//            }
//            runtime.reset();
//            while (Vehicles.getInstance().goTo(103, -190, 90) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                TransferBall.getInstance().startBrush();
//                Vehicles.getInstance().goTo(103, -190, 90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//
//            runtime.reset();
//            while (runtime.milliseconds() < 2700 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(-35, -51, 0);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                GunControl.getInstance().startShot();
//            }
//
//            runtime.reset();
//            while (runtime.milliseconds() < 1700 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                GunControl.getInstance().startShot();
//                TransferBall.getInstance().startFlow();
//                Vehicles.getInstance().moveToDirection(0, 0, 0);
//                TransferBall.getInstance().startBrush();
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().stopShot();
//
//
//
//
//
//
//            runtime.reset();
//            while (Vehicles.getInstance().goTo(10, -250, 90) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(10, -250, 90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            runtime.reset();
//            while (Vehicles.getInstance().goTo(97, -250, 90) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                TransferBall.getInstance().startBrush();
//                Vehicles.getInstance().goTo(97, -250, 90);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 400) {
//
//            }
////
//            TransferBall.getInstance().startBrush();
//            runtime.reset();
//            while (runtime.milliseconds() < 2200 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                Vehicles.getInstance().goTo(-35, -51, 0);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                GunControl.getInstance().startShot();
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 2500 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 60, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                GunControl.getInstance().startShot();
//                Vehicles.getInstance().moveToDirection(0, 0, 0);
//                TransferBall.getInstance().startFlow();
//                TransferBall.getInstance().startBrush();
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//            }
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().stopShot();
        }
    }
}