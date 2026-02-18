package org.firstinspires.ftc.teamcode.robot.event;

import org.firstinspires.ftc.teamcode.core.trait.event.ParallelAction;
import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.core.trait.event.WaitAction;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

import java.util.Arrays;

public class MoveAction implements RobotAction {
    RobotAction action;

    public MoveAction(double x, double y, double yaw) {
        MoveActionBasic move = new MoveActionBasic(x, y, yaw);
        action = new ParallelAction(Arrays.asList(move, new WaitAction(WebConfig.pauseMoveLimit)),
                ParallelAction.FinishMode.ANY);
    }

    public MoveAction(double[] pos) {
        this(pos[0], pos[1], pos[2]);
    }

    @Override
    public void update() {
        action.update();
    }

    @Override
    public boolean isFinished() {
        return action.isFinished();
    }
}
