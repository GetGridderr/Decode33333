package org.firstinspires.ftc.teamcode.robot.event;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

public class FlowAction implements RobotAction {
    RobotAction openAction = new ShootingFlowAction();
    RobotAction closedTeleAction = new OscFlowAction();
    boolean autoMode = false;
    public boolean on = true;

    public FlowAction(boolean autoMode) {this.autoMode = autoMode;}

    public void startFlow() {
        this.on = true;
    }

    public void stopFlow() {
        this.on = false;
    }

    @Override
    public void update() {
        if (on) {
            if (Robot.doorOpen) {
                openAction.update();
            } else {
                if (autoMode) Robot.flowMotor.setPower(WebConfig.flowForward);
                else closedTeleAction.update();
            }
        } else {
            Robot.stopFlow();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
