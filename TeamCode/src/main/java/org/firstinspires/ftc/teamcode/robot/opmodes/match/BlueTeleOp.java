package org.firstinspires.ftc.teamcode.robot.opmodes.match;


import org.firstinspires.ftc.teamcode.robot.Robot;

/**
 * TeleOp-period program logic of the robot
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(
        name = "Blue TeleOp",
        group = "Dev"
)
public class BlueTeleOp extends RedTeleOp {
    @Override
    protected void setTeamSign() {
        Robot.teamColor = Robot.TeamColor.BLUE;
    }
}
