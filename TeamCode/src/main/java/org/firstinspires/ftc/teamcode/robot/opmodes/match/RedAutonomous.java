package org.firstinspires.ftc.teamcode.robot.opmodes.match;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.trait.event.ParallelAction;
import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.core.trait.event.SequenceAction;
import org.firstinspires.ftc.teamcode.core.trait.event.TimedAction;
import org.firstinspires.ftc.teamcode.core.trait.event.WaitAction;
import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.event.GunAction;
import org.firstinspires.ftc.teamcode.robot.event.MoveAction;
import org.firstinspires.ftc.teamcode.robot.event.TimedMoveAction;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.core.trait.event.SingleAction;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

import java.util.Arrays;


/**
 * Autonomous-period program logic of the robot
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(
        name = "Red Autonomous",
        group = "Dev"
)
public class RedAutonomous extends OpMode {
    public RobotAction actions;

    protected void setTeamSign() {
        Robot.teamColor = Robot.TeamColor.RED;
    }

    private void initActions() {
        GunAction gunAction = new GunAction();
        RobotAction movementSeq = new SequenceAction(
                new SingleAction(() -> {
                    gunAction.rotate = true;
                    gunAction.setTarget(Robot.RED_DISTANCE_FROM_GATES);
                }),
                new WaitAction(1),
                new MoveAction(Robot.RED_DISTANCE_FROM_GATES),
                new SingleAction(gunAction::unsetTarget),
                new SingleAction(Robot::openGunDoor),
                new TimedMoveAction(Robot.RED_DISTANCE_FROM_GATES, WebConfig.pauseShoot),
                new SingleAction(Robot::closeGunDoor),

                new SingleAction(() -> {gunAction.rotate = false;}),
                new MoveAction(Robot.RED_BALLS1_PRE_POS),
                new TimedMoveAction(Robot.RED_BALLS1_GOT_POS, WebConfig.pauseEat),
                new MoveAction(Robot.RED_BALLS1_PRE_POS),
                new SingleAction(() -> {
                    gunAction.rotate = true;
                    gunAction.setTarget(Robot.RED_DISTANCE_FROM_GATES);
                }),
                new MoveAction(Robot.RED_DISTANCE_FROM_GATES),
                new SingleAction(gunAction::unsetTarget),
                new SingleAction(Robot::openGunDoor),
                new TimedMoveAction(Robot.RED_DISTANCE_FROM_GATES, WebConfig.pauseShoot),
                new SingleAction(Robot::closeGunDoor),
                new SingleAction(() -> {gunAction.rotate = false;}),

                new SingleAction(() -> {gunAction.rotate = false;}),
                new MoveAction(Robot.RED_BALLS2_PRE_POS),
                new TimedMoveAction(Robot.RED_BALLS2_GOT_POS, WebConfig.pauseEat),
                new MoveAction(Robot.RED_BALLS2_AFTER_POS),
                new MoveAction(Robot.RED_BALLS2_BACK_POS),
                new SingleAction(() -> {
                    gunAction.rotate = true;
                    gunAction.setTarget(Robot.RED_DISTANCE_FROM_GATES);
                }),
                new MoveAction(Robot.RED_DISTANCE_FROM_GATES),
                new SingleAction(gunAction::unsetTarget),
                new SingleAction(Robot::openGunDoor),
                new TimedMoveAction(Robot.RED_DISTANCE_FROM_GATES, WebConfig.pauseShoot),
                new SingleAction(Robot::closeGunDoor),
                new SingleAction(() -> {gunAction.rotate = false;}),

                new SingleAction(() -> {gunAction.rotate = false;}),
                new MoveAction(Robot.RED_BALLS3_PRE_POS),
                new TimedMoveAction(Robot.RED_BALLS3_GOT_POS, WebConfig.pauseEat),
                new SingleAction(() -> {
                    gunAction.rotate = true;
                    gunAction.setTarget(Robot.RED_DISTANCE_FROM_GATES);
                }),
                new MoveAction(Robot.RED_DISTANCE_FROM_GATES),
                new SingleAction(gunAction::unsetTarget),
                new SingleAction(Robot::openGunDoor),
                new TimedMoveAction(Robot.RED_DISTANCE_FROM_GATES, WebConfig.pauseShoot),
                new SingleAction(Robot::closeGunDoor),
                new SingleAction(() -> {gunAction.rotate = false;})
        );
        RobotAction parallelMain = new ParallelAction(Arrays.asList(
                gunAction, movementSeq), ParallelAction.FinishMode.ANY);
        actions = new SequenceAction(
                new SingleAction(Robot::closeGunDoor),
                new SingleAction(Robot::startFlow),
                parallelMain,
                new SingleAction(Robot::stopFlow),
                new SingleAction(() -> Robot.gunMotor.setPower(0)),
                new SingleAction(() -> Robot.setPowers(0, 0, 0, false))
        );
    }

    @Override
    public void init() {
        setTeamSign();
        initActions();
        Robot.initialize(hardwareMap);
    }

    @Override
    public void init_loop() {
        Robot.setInitPos();
        FieldRenderer.renderRobot();
    }

    @Override
    public void loop() {
        actions.update();

        FieldRenderer.renderRobot();
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
