package org.firstinspires.ftc.teamcode.robot.event;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.core.trait.event.TimedAction;


public class TimedMoveAction implements RobotAction {
    RobotAction action;

    public TimedMoveAction(double x, double y, double yaw, double duration) {
        MoveActionBasic move = new MoveActionBasic(x, y, yaw);
        action = new TimedAction(move, duration);
    }

    public TimedMoveAction(double[] pos, double duration) {
        this(pos[0], pos[1], pos[2], duration);
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
