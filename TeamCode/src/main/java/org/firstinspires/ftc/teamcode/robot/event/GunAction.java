package org.firstinspires.ftc.teamcode.robot.event;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

public class GunAction implements RobotAction {
    public boolean rotate = true;
    boolean followTarget = false;
    double x, y, yaw;

    public GunAction() {}

    public void setTarget(double x, double y, double yaw) {
        followTarget = true;
        this.x = x;
        this.y = y;
        this.yaw = yaw;
    }

    public void setTarget(double[] pos) {
        setTarget(pos[0], pos[1], pos[2]);
    }

    public void unsetTarget() {
        followTarget = false;
    }

    @Override
    public void update() {
        if(rotate) {
            if(followTarget) {
                Robot.turnTower(Robot.angleToGoal(x, y, yaw));
            } else {
                Robot.turnTower(Robot.angleToGoal());
            }
        }
        Robot.setShootingAngle(WebConfig.gunAngle);
        Robot.setGunVelocity(WebConfig.gunVel);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
