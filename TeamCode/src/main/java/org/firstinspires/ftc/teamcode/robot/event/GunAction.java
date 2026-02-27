package org.firstinspires.ftc.teamcode.robot.event;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

public class GunAction implements RobotAction {
    public boolean rotate = true;
    boolean followTarget = false;
    boolean shouldShoot = true;
    double x, y, yaw;

    public GunAction() {}

    public void setTarget(double x, double y, double yaw) {
        followTarget = true;
        double[] gunPos = Robot.getGunPos(x, y, yaw);
        this.x = gunPos[0];
        this.y = gunPos[1];
        this.yaw = yaw;
    }

    public void setTarget(double[] pos) {
        setTarget(pos[0], pos[1], pos[2]);
    }

    public void unsetTarget() {
        followTarget = false;
    }

    public void setShouldShoot(boolean should) {
        shouldShoot = should;
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
        if (shouldShoot) Robot.setGunVelocity(WebConfig.gunVel);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
