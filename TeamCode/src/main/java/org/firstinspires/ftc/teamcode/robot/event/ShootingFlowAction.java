package org.firstinspires.ftc.teamcode.robot.event;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

@Config
public class ShootingFlowAction implements RobotAction {

    public ShootingFlowAction() {}

    public double getSpeed() {
        double[] gunCoordsShort = Robot.getGunPos(Robot.RED_SHOOT_SHORT[0],
                Robot.RED_SHOOT_SHORT[1],
                Robot.RED_SHOOT_SHORT[2]);
        double[] gunCoordsLong = Robot.getGunPos(Robot.RED_SHOOT_LONG[0],
                Robot.RED_SHOOT_LONG[1],
                Robot.RED_SHOOT_LONG[2]);
        double distShort = Robot.distanceToGoal(gunCoordsShort[0], gunCoordsShort[1]);
        double distLong = Robot.distanceToGoal(gunCoordsLong[0], gunCoordsLong[1]);
        double dist = Robot.distanceToGoal();
        if(Math.abs(dist - distLong) > Math.abs(dist - distShort)) return 1;
        else return WebConfig.flowSpeedLong;
    }

    @Override
    public void update() {
        Robot.flowMotor.setPower(getSpeed());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
