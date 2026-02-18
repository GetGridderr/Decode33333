package org.firstinspires.ftc.teamcode.robot.event;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.robot.Robot;


public class MoveActionBasic implements RobotAction {
    double x, y, yaw;
    boolean arrived = false;

    public MoveActionBasic(double x, double y, double yaw) {
        this.x = x * Robot.teamColor.getSign();
        this.y = y;
        this.yaw = yaw * Robot.teamColor.getSign();
    }

    public MoveActionBasic(double[] pos) {
        this(pos[0], pos[1], pos[2]);
    }

    public void update() {
        if(!Robot.goTo(x, y, yaw)) {
            this.arrived = true;
        }
    }

    @Override
    public boolean isFinished() {
        return this.arrived;
    }
}
