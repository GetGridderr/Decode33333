package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;//package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;
//
//import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.autoTime;
//import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.positionAuto;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
//import org.firstinspires.ftc.teamcode.core.device.sensor.SensorVoltage;
//import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
//import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
//import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
//import org.firstinspires.ftc.teamcode.main.movement.Odometry;
//import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
//import org.firstinspires.ftc.teamcode.main.opencv.AprilTag;
//
//@Autonomous(name="Auto", group="Dev")
//public class Auto extends LinearOpMode {
//
//    public static double pXY = 0.02;
//    public static double dXY = 0.04;
//    public static double pYaw = 0.018;
//    public static double iYaw = 0.0;
//    public static double dYaw = 0.026;
//    public static double offset = 10;
//    public static double distanceToTarget = 25;
//    private final ElapsedTime runtime = new ElapsedTime();
//
//    public static int count = 0;
//
//    public void initialize() {
//        Vehicles.getInstance().initialize(hardwareMap);
//        TransferBall.getInstance().initialize(hardwareMap);
//        GunControl.getInstance().initialize(hardwareMap);
//        if (Vehicles.getInstance().isInitialized()) {
//            telemetry.addData("Status", "Initialized");
//        }
//    }
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        initialize();
//
//        waitForStart();
//        runtime.reset();
//
//        while (opModeIsActive()) {
//            Odometry.getInstance().odometryTick();
//            FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            FtcDashboard.getInstance().getTelemetry().addData("X Pos:", OdometerPinpoint.getInstance().getX());
//            FtcDashboard.getInstance().getTelemetry().addData("Y Pos:", OdometerPinpoint.getInstance().getY());
//            FtcDashboard.getInstance().getTelemetry().addData("Yaw:", OdometerPinpoint.getInstance().getYaw());
//            FtcDashboard.getInstance().getTelemetry().addData("YawErr", Vehicles.getInstance().yawErr);
//            FtcDashboard.getInstance().getTelemetry().update();
//
//
//            Vehicles.getInstance().setPosPID(pXY, 0, dXY, pXY, 0, dXY, pYaw, iYaw, dYaw);
////        Vehicles.getInstance().goTo(vehicles.targetX, vehicles.targetY, vehicles.targetYaw);
//            FtcDashboard.getInstance().getTelemetry().addData("Satrt PID", true);
//            FtcDashboard.getInstance().getTelemetry().update();
//            while (!Vehicles.getInstance().goTo(positionAuto.posGun[0], positionAuto.posGun[1], positionAuto.posGun[2]) && opModeIsActive()) {
//                Vehicles.getInstance().goTo(positionAuto.posGun[0], positionAuto.posGun[1], positionAuto.posGun[2]);
//                GunControl.getInstance().startShot();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                TransferBall.getInstance().startBrush();
//                GunControl.getInstance().startShot();
//                TransferBall.getInstance().startFlow();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            TransferBall.getInstance().stopBrush();
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().stopShot();
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(positionAuto.posFirstEat[0],
//                    positionAuto.posFirstEat[1],
//                    positionAuto.posFirstEat[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posFirstEat[0], positionAuto.posFirstEat[1] + 5, positionAuto.posFirstEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posFinishEat[0],
//                    positionAuto.posFinishEat[1],
//                    positionAuto.posFinishEat[2]) && opModeIsActive()) {
//                TransferBall.getInstance().startBrush();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posFinishEat[0], positionAuto.posFinishEat[1], positionAuto.posFinishEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posDrop[0], positionAuto.posDrop[1], positionAuto.posDrop[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            runtime.reset();
//
//            TransferBall.getInstance().startBrush();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posGun[0],
//                    positionAuto.posGun[1],
//                    positionAuto.posGun[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posGun[0], positionAuto.posGun[1], positionAuto.posGun[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//                GunControl.getInstance().startShot();
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                GunControl.getInstance().startShot();
//                TransferBall.getInstance().startBrush();
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//                TransferBall.getInstance().startFlow();
//            }
//            runtime.reset();
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//            FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            GunControl.getInstance().stopShot();
//
//
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posTwoEat[0],
//                    positionAuto.posTwoEat[1],
//                    positionAuto.posTwoEat[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posTwoEat[0], positionAuto.posTwoEat[1], positionAuto.posTwoEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posFinishTwoEat[0],
//                    positionAuto.posFinishTwoEat[1],
//                    positionAuto.posFinishTwoEat[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                TransferBall.getInstance().startBrush();
//                Vehicles.getInstance().goTo(positionAuto.posFinishTwoEat[0], positionAuto.posFinishTwoEat[1], positionAuto.posFinishTwoEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posTwoEat[0],
//                    positionAuto.posTwoEat[1],
//                    positionAuto.posTwoEat[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posTwoEat[0], positionAuto.posTwoEat[1], positionAuto.posTwoEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posGun[0],
//                    positionAuto.posGun[1],
//                    positionAuto.posGun[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posGun[0], positionAuto.posGun[1], positionAuto.posGun[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//                GunControl.getInstance().startShot();
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 2000 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                GunControl.getInstance().startShot();
//                TransferBall.getInstance().startFlow();
//                TransferBall.getInstance().startBrush();
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().stopShot();
//
//
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posThreeEat[0],
//                    positionAuto.posThreeEat[1],
//                    positionAuto.posThreeEat[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posThreeEat[0], positionAuto.posThreeEat[1], positionAuto.posThreeEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//            runtime.reset();
//            while (runtime.milliseconds() < 1000 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                TransferBall.getInstance().startBrush();
//                Vehicles.getInstance().goTo(positionAuto.posFinishTreeEat[0], positionAuto.posFinishTreeEat[1], positionAuto.posFinishTreeEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//
//            TransferBall.getInstance().startBrush();
//            runtime.reset();
//            while (!Vehicles.getInstance().goTo(
//                    positionAuto.posGun[0],
//                    positionAuto.posGun[1],
//                    positionAuto.posGun[2]) && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(positionAuto.posGun[0], positionAuto.posGun[1], positionAuto.posGun[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//                GunControl.getInstance().startShot();
//            }
//            runtime.reset();
//            TransferBall.getInstance().stopFlow();
//            GunControl.getInstance().stopShot();
//            runtime.reset();
//            while (runtime.milliseconds() < 1000 && opModeIsActive()) {
//                GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, offset, distanceToTarget);
//                Vehicles.getInstance().goTo(-positionAuto.posFirstEat[0], positionAuto.posFirstEat[1] - 5, -positionAuto.posFirstEat[2]);
//                FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() - positionAuto.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAuto.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAuto.yawSwap);
//            }
//
//        }
//    }
//}