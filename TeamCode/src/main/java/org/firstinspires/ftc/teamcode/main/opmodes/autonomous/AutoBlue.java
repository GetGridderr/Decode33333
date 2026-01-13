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
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;

@Autonomous(name="AutoBlue", group="Auto")
public class AutoBlue extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private AutoState state = AutoState.INIT;

    @Override
    public void runOpMode() throws InterruptedException {
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
    }

    public void doCorrectState() {
        runtime.reset();

        switch (state) {
            case INIT:
                state = AutoState.MOVE_TO_SHOOTING_ZERO;
                runtime.reset();
                break;
            case MOVE_TO_SHOOTING_ZERO:
                Vehicles.getInstance().goTo(positionAutoBlue.posGun[0], positionAutoBlue.posGun[1], positionAutoBlue.posGun[2]);
                if (runtime.milliseconds() >= 2000) {
                    state = AutoState.SHOOTING_ZERO;
                    runtime.reset();
                }
                break;
            case SHOOTING_ZERO:
                TransferBall.getInstance().startBrush();
                GunControl.getInstance().startShot();
                TransferBall.getInstance().startFlow();
                if (runtime.milliseconds() >= 2500) {
                    state = AutoState.MOVE_TO_FIRST_EAT;
                    TransferBall.getInstance().stopBrush();
                    TransferBall.getInstance().stopFlow();
                    GunControl.getInstance().stopShot();
                    runtime.reset();
                }
                break;
            case MOVE_TO_FIRST_EAT:
                Vehicles.getInstance().goTo(positionAutoBlue.posFirstEat[0], positionAutoBlue.posFirstEat[1] + 5, positionAutoBlue.posFirstEat[2]);
                if (runtime.milliseconds() >= 1500) {
                    state = AutoState.FINISH_FIRST_EAT;
                    runtime.reset();
                }
                break;
            case FINISH_FIRST_EAT:
                TransferBall.getInstance().startBrush();
                Vehicles.getInstance().goTo(positionAutoBlue.posFirstFinishEat[0], positionAutoBlue.posFirstFinishEat[1], positionAutoBlue.posFirstFinishEat[2]);
                if (runtime.milliseconds() >= 1500) {
                    state = AutoState.DROP_RAMP;
                    runtime.reset();
                }
                break;
            case DROP_RAMP:
                Vehicles.getInstance().goTo(positionAutoBlue.posDrop[0], positionAutoBlue.posDrop[1], positionAutoBlue.posDrop[2]);
                if (runtime.milliseconds() >= 1500) {
                    state = AutoState.MOVE_TO_SHOOTING_FIRST;
                    runtime.reset();
                }
                break;
            case MOVE_TO_SHOOTING_FIRST:
                Vehicles.getInstance().goTo(positionAutoBlue.posGun[0], positionAutoBlue.posGun[1], positionAutoBlue.posGun[2]);
                GunControl.getInstance().startShot();
                if (runtime.milliseconds() >= 3000) {
                    state = AutoState.SHOOTING_FIRST;
                    runtime.reset();
                }
                break;
            case SHOOTING_FIRST:
                GunControl.getInstance().startShot();
                TransferBall.getInstance().startBrush();
                TransferBall.getInstance().startFlow();
                if (runtime.milliseconds() >= 2000) {
                    GunControl.getInstance().stopShot();
                    TransferBall.getInstance().stopFlow();
                    state = AutoState.MOVE_TO_SECOND_EAT;
                    runtime.reset();
                }
                break;
            case MOVE_TO_SECOND_EAT:
                Vehicles.getInstance().goTo(positionAutoBlue.posTwoEat[0], positionAutoBlue.posTwoEat[1], positionAutoBlue.posTwoEat[2]);
                if (runtime.milliseconds() >= 2000) {
                    state = AutoState.FINISH_SECOND_EAT;
                    runtime.reset();
                }
                break;
            case FINISH_SECOND_EAT:
                TransferBall.getInstance().startBrush();
                Vehicles.getInstance().goTo(positionAutoBlue.posFinishTwoEat[0], positionAutoBlue.posFinishTwoEat[1], positionAutoBlue.posFinishTwoEat[2]);
                if (runtime.milliseconds() >= 1500) {
                    state = AutoState.MOVE_TO_SHOOTING_SECOND;
                    runtime.reset();
                }
                break;
            case MOVE_TO_SHOOTING_SECOND:
                Vehicles.getInstance().goTo(positionAutoBlue.posGun[0], positionAutoBlue.posGun[1], positionAutoBlue.posGun[2]);
                GunControl.getInstance().startShot();
                if (runtime.milliseconds() >= 3000) {
                    state = AutoState.SHOOTING_SECOND;
                    runtime.reset();
                }
                break;
            case SHOOTING_SECOND:
                GunControl.getInstance().startShot();
                TransferBall.getInstance().startFlow();
                TransferBall.getInstance().startBrush();
                if (runtime.milliseconds() >= 2000) {
                    GunControl.getInstance().stopShot();
                    TransferBall.getInstance().stopFlow();
                    state = AutoState.MOVE_TO_THIRD_EAT;
                    runtime.reset();
                }
                break;
            case MOVE_TO_THIRD_EAT:
                Vehicles.getInstance().goTo(positionAutoBlue.posThreeEat[0], positionAutoBlue.posThreeEat[1], positionAutoBlue.posThreeEat[2]);
                if (runtime.milliseconds() >= 2500) {
                    state = AutoState.FINISH_THIRD_EAT;
                    runtime.reset();
                }
                break;
            case FINISH_THIRD_EAT:
                TransferBall.getInstance().startBrush();
                Vehicles.getInstance().goTo(positionAutoBlue.posFinishTreeEat[0], positionAutoBlue.posFinishTreeEat[1], positionAutoBlue.posFinishTreeEat[2]);
                if (runtime.milliseconds() >= 1500) {
                    state = AutoState.MOVE_TO_SHOOTING_THIRD;
                    runtime.reset();
                }
                break;
            case MOVE_TO_SHOOTING_THIRD:
                Vehicles.getInstance().goTo(positionAutoBlue.posGun[0], positionAutoBlue.posGun[1], positionAutoBlue.posGun[2]);
                GunControl.getInstance().startShot();
                if (runtime.milliseconds() >= 2500) {
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
                    runtime.reset();
                }
                break;
            case LEAVE_FROM_ZONE:
                Vehicles.getInstance().goTo(-positionAutoBlue.posFirstEat[0], positionAutoBlue.posFirstEat[1] - 5, -positionAutoBlue.posFirstEat[2]);
                if (runtime.milliseconds() >= 1000) {
                    state = AutoState.COMPLETE;
                    runtime.reset();
                }
                break;
            case COMPLETE:
                GunControl.getInstance().stopShot();
                TransferBall.getInstance().stopFlow();
                TransferBall.getInstance().stopBrush();
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
