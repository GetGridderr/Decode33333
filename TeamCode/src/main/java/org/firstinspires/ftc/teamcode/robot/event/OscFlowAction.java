package org.firstinspires.ftc.teamcode.robot.event;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

@Config
public class OscFlowAction implements RobotAction {
    ElapsedTime elapsedTime;
    boolean started = false;

    public OscFlowAction() {
        this.elapsedTime = new ElapsedTime();
        elapsedTime.reset();
    }

    @Override
    public void update() {
        double t = elapsedTime.seconds() % (WebConfig.flowBackward + WebConfig.flowForward);
        if(t < WebConfig.flowForward) {
            Robot.flowMotor.setPower(WebConfig.flowSpeedWithClosedDoor);
        } else {
            Robot.flowMotor.setPower(WebConfig.flowSpeedBack);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
