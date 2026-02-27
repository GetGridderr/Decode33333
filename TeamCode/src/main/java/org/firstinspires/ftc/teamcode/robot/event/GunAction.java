package org.firstinspires.ftc.teamcode.robot.event;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.core.util.Util;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

@Config
public class GunAction implements RobotAction {
    public boolean rotate = true;
    boolean followTarget = false;
    boolean shouldShoot = true;
    double x, y, yaw;

    public static double shortAngle = 0.5;
    public static double shortSpeed = 1100;
    public static double longAngle = 0;
    public static double longSpeed = 800;

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

    public double[] getAngleSpeed() {
        double[] gunCoordsShort = Robot.getGunPos(Robot.RED_SHOOT_SHORT[0],
                Robot.RED_SHOOT_SHORT[1],
                Robot.RED_SHOOT_SHORT[2]);
        double[] gunCoordsLong = Robot.getGunPos(Robot.RED_SHOOT_LONG[0],
                Robot.RED_SHOOT_LONG[1],
                Robot.RED_SHOOT_LONG[2]);
        double distShort = Robot.distanceToGoal(gunCoordsShort[0], gunCoordsShort[1]);
        double distLong = Robot.distanceToGoal(gunCoordsLong[0], gunCoordsLong[1]);
        double dist;
        if(followTarget) {
            dist = Robot.distanceToGoal(x, y);
        } else {
            dist = Robot.distanceToGoal();
        }
        FtcDashboard.getInstance().getTelemetry().addData("dist", dist);
        FtcDashboard.getInstance().getTelemetry().addData("angle", Util.interp(distLong, distShort, dist, longAngle, shortAngle));
        FtcDashboard.getInstance().getTelemetry().addData("spd", Util.interp(distLong, distShort, dist, longSpeed, shortSpeed));

        FtcDashboard.getInstance().getTelemetry().addData("ld", distLong);
        FtcDashboard.getInstance().getTelemetry().addData("sd", distShort);
        FtcDashboard.getInstance().getTelemetry().addData("la", longAngle);
        FtcDashboard.getInstance().getTelemetry().addData("sa", shortAngle);
        FtcDashboard.getInstance().getTelemetry().update();
        return new double[] {
                Util.interp(distLong, distShort, dist, longAngle, shortAngle),
                Util.interp(distLong, distShort, dist, longSpeed, shortSpeed)
        };
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
        double[] angleSpeed = getAngleSpeed();
        Robot.setShootingAngle(angleSpeed[0]);
        if (shouldShoot) Robot.setGunVelocity(angleSpeed[1]);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
