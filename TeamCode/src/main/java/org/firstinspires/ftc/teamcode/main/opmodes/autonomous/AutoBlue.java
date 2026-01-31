package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.positionAutoBlue;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.config.AutoState;
import org.firstinspires.ftc.teamcode.main.modules.gun.DataShots;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;

@Autonomous(name="AutoBlue", group="Auto")
public class AutoBlue extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private AutoState state = AutoState.INIT;

    @Override
    public void runOpMode() {
        waitForStart();
        initialize();
        while (opModeIsActive()) {
            doCorrectState();
            updateSystem();
        }
    }

    private void initialize() {
        Vehicles.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().initialize(hardwareMap);
        GunControl.getInstance().initialize(hardwareMap);
        Vehicles.getInstance().setPosPID(vehicles.pX, 0, vehicles.dX, vehicles.pY, 0, vehicles.dY, vehicles.pYaw, vehicles.iYaw, vehicles.dYaw);
        telemetry.addData("Status", "Initialized");
        runtime.reset();
    }

    public void doCorrectState() {

        switch (state) {
            case INIT:
                state = AutoState.MOVE_TO_SHOOTING_ZERO;
                runtime.reset();
                break;
            case MOVE_TO_SHOOTING_ZERO:
//                if (!Vehicles.getInstance().goTo(
//                        positionAutoBlue.posGun.x - 30,
//                        positionAutoBlue.posGun.y - 30,
//                        positionAutoBlue.posGun.yaw)) {
//                    DataShots dataShots = GunControl.getInstance().shotToDistance(OdometerPinpoint.getInstance().getDistanceToTarget(gunConfig.posGoalX, gunConfig.posGoalY));
//                    gunConfig.velocity = dataShots.speed;
//                    gunConfig.bananPosition = dataShots.angle;
//                    gunConfig.spdMul = dataShots.k;
//                    GunControl.getInstance().startShot();
//                    GunControl.getInstance().setBananServo();
//                    TransferBall.getInstance().startBrush();
//                    TransferBall.getInstance().startFlow();
//                }
                while (Vehicles.getInstance().goTo(
                        positionAutoBlue.posGun.x,
                        positionAutoBlue.posGun.y,
                        positionAutoBlue.posGun.yaw)) {
                    Vehicles.getInstance().goTo(
                            positionAutoBlue.posGun.x,
                            positionAutoBlue.posGun.y,
                            positionAutoBlue.posGun.yaw);
                }
                state = AutoState.MOVE_TO_SHOOTING_ZERO;
                runtime.reset();
                break;
            case SHOOTING_ZERO:
                TransferBall.getInstance().startBrush();
                GunControl.getInstance().startShot();
                TransferBall.getInstance().startFlow();
                if (runtime.milliseconds() >= 1500) {
                    state = AutoState.MOVE_TO_FIRST_EAT;
                    TransferBall.getInstance().stopBrush();
                    TransferBall.getInstance().stopFlow();
                    GunControl.getInstance().stopShot();
                }
                break;
            case MOVE_TO_FIRST_EAT:
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posFirstEat.x,
                        positionAutoBlue.posFirstEat.y,
                        positionAutoBlue.posFirstEat.yaw);
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posFirstEat.x,
                        positionAutoBlue.posFirstEat.y,
                        positionAutoBlue.posFirstEat.yaw)) {
                    state = AutoState.FINISH_FIRST_EAT;
                }
                break;
            case FINISH_FIRST_EAT:
                TransferBall.getInstance().startBrush();
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posFirstFinishEat.x,
                        positionAutoBlue.posFirstFinishEat.y,
                        positionAutoBlue.posFirstFinishEat.yaw);
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posFirstFinishEat.x,
                        positionAutoBlue.posFirstFinishEat.y,
                        positionAutoBlue.posFirstFinishEat.yaw)) {
                    state = AutoState.MOVE_TO_SHOOTING_FIRST;
                }
                break;
            case MOVE_TO_SHOOTING_FIRST:
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posGun.x,
                        positionAutoBlue.posGun.y,
                        positionAutoBlue.posGun.yaw);
                GunControl.getInstance().startShot();
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posGun.x,
                        positionAutoBlue.posGun.y,
                        positionAutoBlue.posGun.yaw)) {
                    state = AutoState.SHOOTING_FIRST;
                    runtime.reset();
                }
                break;
            case SHOOTING_FIRST:
                GunControl.getInstance().startShot();
                TransferBall.getInstance().startBrush();
                TransferBall.getInstance().startFlow();
                if (runtime.milliseconds() >= 1500) {
                    GunControl.getInstance().stopShot();
                    TransferBall.getInstance().stopFlow();
                    state = AutoState.MOVE_TO_SECOND_EAT;
                }
                break;
            case MOVE_TO_SECOND_EAT:
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posTwoEat.x,
                        positionAutoBlue.posTwoEat.y,
                        positionAutoBlue.posTwoEat.yaw);
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posTwoEat.x,
                        positionAutoBlue.posTwoEat.y,
                        positionAutoBlue.posTwoEat.yaw)) {
                    state = AutoState.FINISH_SECOND_EAT;
                }
                break;
            case FINISH_SECOND_EAT:
                TransferBall.getInstance().startBrush();
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posFinishTwoEat.x,
                        positionAutoBlue.posFinishTwoEat.y,
                        positionAutoBlue.posFinishTwoEat.yaw);
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posFinishTwoEat.x,
                        positionAutoBlue.posFinishTwoEat.y,
                        positionAutoBlue.posFinishTwoEat.yaw)) {
                    state = AutoState.MOVE_TO_SHOOTING_SECOND;
                }
                break;
            case MOVE_TO_SHOOTING_SECOND:
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posGun.x,
                        positionAutoBlue.posGun.y,
                        positionAutoBlue.posGun.yaw);
                GunControl.getInstance().startShot();
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posGun.x,
                        positionAutoBlue.posGun.y,
                        positionAutoBlue.posGun.yaw)) {
                    state = AutoState.SHOOTING_SECOND;
                    runtime.reset();
                }
                break;
            case SHOOTING_SECOND:
                GunControl.getInstance().startShot();
                TransferBall.getInstance().startFlow();
                TransferBall.getInstance().startBrush();
                if (runtime.milliseconds() >= 1500) {
                    GunControl.getInstance().stopShot();
                    TransferBall.getInstance().stopFlow();
                    state = AutoState.MOVE_TO_THIRD_EAT;
                }
                break;
            case MOVE_TO_THIRD_EAT:
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posThreeEat.x,
                        positionAutoBlue.posThreeEat.y,
                        positionAutoBlue.posThreeEat.yaw);
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posThreeEat.x,
                        positionAutoBlue.posThreeEat.y,
                        positionAutoBlue.posThreeEat.yaw)) {
                    state = AutoState.FINISH_THIRD_EAT;
                }
                break;
            case FINISH_THIRD_EAT:
                TransferBall.getInstance().startBrush();
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posFinishTreeEat.x,
                        positionAutoBlue.posFinishTreeEat.y,
                        positionAutoBlue.posFinishTreeEat.yaw);
                if (!Vehicles.getInstance().goTo(
                    positionAutoBlue.posFinishTreeEat.x,
                    positionAutoBlue.posFinishTreeEat.y,
                    positionAutoBlue.posFinishTreeEat.yaw)) {
                    state = AutoState.MOVE_TO_SHOOTING_THIRD;
                }
                break;
            case MOVE_TO_SHOOTING_THIRD:
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posGun.x,
                        positionAutoBlue.posGun.y,
                        positionAutoBlue.posGun.yaw);
                GunControl.getInstance().startShot();
                if (!Vehicles.getInstance().goTo(
                        positionAutoBlue.posGun.x,
                        positionAutoBlue.posGun.y,
                        positionAutoBlue.posGun.yaw)) {
                    state = AutoState.SHOOTING_THIRD;
                    runtime.reset();
                }
                break;
            case SHOOTING_THIRD:
                GunControl.getInstance().startShot();
                TransferBall.getInstance().startFlow();
                TransferBall.getInstance().startBrush();
                if (runtime.milliseconds() >= 2500) {
                    state = AutoState.LEAVE_FROM_ZONE;
                }
                break;
            case LEAVE_FROM_ZONE:
                Vehicles.getInstance().goTo(
                        positionAutoBlue.posFirstEat.x,
                        positionAutoBlue.posFirstEat.y,
                        positionAutoBlue.posFirstEat.yaw);
                if (Vehicles.getInstance().goTo(
                        positionAutoBlue.posFirstEat.x,
                        positionAutoBlue.posFirstEat.y,
                        positionAutoBlue.posFirstEat.yaw)) {
                    state = AutoState.COMPLETE;
                    runtime.reset();
                }
                break;
            case COMPLETE:
                GunControl.getInstance().stopShot();
                TransferBall.getInstance().stopFlow();
                TransferBall.getInstance().stopBrush();
                Vehicles.getInstance().moveToDirection(0, 0, 0);
                break;
        }
    }

    public void updateSystem() {
        Odometry.getInstance().odometryTick();
        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX() + positionAutoBlue.xSwap, Vehicles.getInstance().getPositionOdometerY() + positionAutoBlue.ySwap, OdometerPinpoint.getInstance().getYaw() + positionAutoBlue.yawSwap);
        FtcDashboard.getInstance().getTelemetry().addData("X Pos:", OdometerPinpoint.getInstance().getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y Pos:", OdometerPinpoint.getInstance().getY());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw:", OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().addData("YawErr", Vehicles.getInstance().yawErr);
        FtcDashboard.getInstance().getTelemetry().update();
        GunControl.getInstance().setTowerDegree(OdometerPinpoint.getInstance().getYaw() - 50, gunConfig.offsetGun, gunConfig.distanceToTarget);
    }
}
