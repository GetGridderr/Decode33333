package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.positionAutoRed;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;

import com.acmerobotics.dashboard.FtcDashboard;
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

@Autonomous(name="AutonomusBlueTower", group="Dev")
public class AutonomusBlue extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();

    public static int countBall = 0;


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
                    while (Vehicles.getInstance().goTo(40, -51, 0) && opModeIsActive()) {
                        Vehicles.getInstance().goTo(40, -51, 0);
                        GunControl.getInstance().startShot();
                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
                    }
                    while (countBall < 3 && opModeIsActive()) {
                        TransferBall.getInstance().startFlow();
                        TransferBall.getInstance().stopBrush();
                        GunControl.getInstance().startShot();
                        runtime.reset();
                        if (GunControl.getInstance().getFlowGun() > 3) {
                            countBall++;
                            while (runtime.milliseconds() < 200) {}
                        }
                    }
                    TransferBall.getInstance().stopBrush();
                    TransferBall.getInstance().stopFlow();
                    GunControl.getInstance().stopShot();
                    runtime.reset();
                    while (Vehicles.getInstance().goTo(120, -55, 90) && opModeIsActive()) {
                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
                        Vehicles.getInstance().goTo(120, -55, 90);
                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
                    }
                    runtime.reset();
//                    while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                        TransferBall.getInstance().startBrush();
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(-20, -65,-90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 500 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 55, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(24, -90, -90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 1000 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 55, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(-13, -90, -90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
////                    runtime.reset();
////
////                    TransferBall.getInstance().startBrush();
////                    while (runtime.milliseconds() < 2000 && opModeIsActive()) {
////                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
////                        Vehicles.getInstance().goTo(35, -51, 0);
////                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
////                        GunControl.getInstance().startShot();
////                    }
////                    runtime.reset();
////                    while (runtime.milliseconds() < 2000 && opModeIsActive()) {
////                        Vehicles.getInstance().goTo(35, -51, 0);
////                        GunControl.getInstance().startShot();
////                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
////                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
////                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                        Vehicles.getInstance().goTo(35, -51, 0);
//                        GunControl.getInstance().startShot();
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2500 && opModeIsActive()) {
//                        Vehicles.getInstance().goTo(35, -51, 0);
//                        TransferBall.getInstance().startBrush();
//                        GunControl.getInstance().startShot();
//                        TransferBall.getInstance().startFlow();
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    TransferBall.getInstance().stopFlow();
//                    GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() + 55, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                    FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    GunControl.getInstance().stopShot();
//
//
//                    runtime.reset();
//                    while (runtime.milliseconds() < 1500 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(71, -130, -90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2200 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        TransferBall.getInstance().startBrush();
//                        Vehicles.getInstance().goTo(-45, -130, -90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(35, -51, 0);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                        GunControl.getInstance().startShot();
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2200 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        GunControl.getInstance().startShot();
//                        TransferBall.getInstance().startFlow();
//                        Vehicles.getInstance().goTo(35, -51, 0);
//                        TransferBall.getInstance().startBrush();
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    TransferBall.getInstance().stopFlow();
//                    GunControl.getInstance().stopShot();
//
//
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2100 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(58, -185, -90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 1200 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        TransferBall.getInstance().startBrush();
//                        Vehicles.getInstance().goTo(-50, -185, -90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//
//                    TransferBall.getInstance().startBrush();
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2100 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(35, -51, 0);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                        GunControl.getInstance().startShot();
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        GunControl.getInstance().startShot();
//                        Vehicles.getInstance().goTo(35, -51, 0);
//                        TransferBall.getInstance().startFlow();
//                        TransferBall.getInstance().startBrush();
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }
//                    runtime.reset();
//                    while (runtime.milliseconds() < 2500 && opModeIsActive()) {
//                        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw()  - 45, gunConfig.offsetGun, gunConfig.distanceToTarget);
//                        Vehicles.getInstance().goTo(40, -62, -90);
//                        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAutoRed.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoRed.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoRed.yawSwap);
//                    }

                }
        }
}