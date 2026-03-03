package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.pinpointConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.positionAuto;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.config.AutoState;
import org.firstinspires.ftc.teamcode.main.modules.gun.DataShots;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
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
        OdometerPinpoint.getInstance().setPosition(positionAuto.posStart);
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
                if (!Vehicles.getInstance().goTo(positionAuto.posGun)) {
                    DataShots dataShots = GunControl.getInstance().shotToDistance(OdometerPinpoint.getInstance().getDistanceToTarget(gunConfig.posGoalX, gunConfig.posGoalY));
                    gunConfig.velocity = dataShots.speed;
                    gunConfig.bananPosition = dataShots.angle;
                    gunConfig.kP = dataShots.k;
                    GunControl.getInstance().startShot();
                    GunControl.getInstance().setBananServo();
                    TransferBall.getInstance().startBrush();
                    TransferBall.getInstance().startFlow();
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
                if (!Vehicles.getInstance().goTo(positionAuto.posFirstEat)) {
                    state = AutoState.FINISH_FIRST_EAT;
                }
                break;
            case FINISH_FIRST_EAT:
                TransferBall.getInstance().startBrush();
                if (!Vehicles.getInstance().goTo(positionAuto.posFinishFirstEat)) {
                    state = AutoState.MOVE_TO_SHOOTING_FIRST;
                }
                break;
            case MOVE_TO_SHOOTING_FIRST:
                GunControl.getInstance().startShot();
                if (!Vehicles.getInstance().goTo(positionAuto.posGun)) {
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
                if (!Vehicles.getInstance().goTo(positionAuto.posTwoEat)) {
                    state = AutoState.FINISH_SECOND_EAT;
                }
                break;
            case FINISH_SECOND_EAT:
                TransferBall.getInstance().startBrush();
                if (!Vehicles.getInstance().goTo(positionAuto.posFinishTwoEat)) {
                    state = AutoState.MOVE_TO_SHOOTING_SECOND;
                }
                break;
            case MOVE_TO_SHOOTING_SECOND:
                GunControl.getInstance().startShot();
                if (!Vehicles.getInstance().goTo(positionAuto.posGun)) {
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
                if (!Vehicles.getInstance().goTo(positionAuto.posThreeEat)) {
                    state = AutoState.FINISH_THIRD_EAT;
                }
                break;
            case FINISH_THIRD_EAT:
                TransferBall.getInstance().startBrush();
                if (!Vehicles.getInstance().goTo(positionAuto.posFinishTreeEat)) {
                    state = AutoState.MOVE_TO_SHOOTING_THIRD;
                }
                break;
            case MOVE_TO_SHOOTING_THIRD:
                GunControl.getInstance().startShot();
                if (!Vehicles.getInstance().goTo(positionAuto.posGun)) {
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
                if (Vehicles.getInstance().goTo(positionAuto.posFirstEat)) {
                    state = AutoState.COMPLETE;
                    runtime.reset();
                }
                break;
            case COMPLETE:
                GunControl.getInstance().stopShot();
                TransferBall.getInstance().stopFlow();
                TransferBall.getInstance().stopBrush();
                pinpointConfig.xPositionStart = OdometerPinpoint.getInstance().getX();
                pinpointConfig.yPositionStart = OdometerPinpoint.getInstance().getY();
                pinpointConfig.headingStart = OdometerPinpoint.getInstance().getYaw();
                Vehicles.getInstance().moveToDirection(0, 0, 0);
                break;
        }
    }

    public void updateSystem() {
        Pose2D robotPos = new Pose2D(DistanceUnit.CM, OdometerPinpoint.getInstance().getX(),
                OdometerPinpoint.getInstance().getY(), AngleUnit.DEGREES, OdometerPinpoint.getInstance().getYaw());
        Pose2D goalPose = new Pose2D(DistanceUnit.CM, gunConfig.posGoalX, gunConfig.posGoalY, AngleUnit.DEGREES, 0);
        FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerX(), Vehicles.getInstance().getPositionOdometerY(), OdometerPinpoint.getInstance().getYaw());
        GunControl.getInstance().aimToTarget(goalPose, robotPos);
    }
}
