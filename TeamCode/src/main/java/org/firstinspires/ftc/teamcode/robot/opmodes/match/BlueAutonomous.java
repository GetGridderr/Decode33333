package org.firstinspires.ftc.teamcode.robot.opmodes.match;


import org.firstinspires.ftc.teamcode.robot.Robot;

/**
 * Autonomous-period program logic of the robot
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(
        name = "Blue Autonomous",
        group = "Dev"
)
public class BlueAutonomous extends RedAutonomous {
    @Override
    protected void setTeamSign() {
        Robot.teamColor = Robot.TeamColor.BLUE;
    }
}
