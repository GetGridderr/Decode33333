package org.firstinspires.ftc.teamcode.robot.opmodes.match;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.util.exception.UnimplementedException;
import org.firstinspires.ftc.teamcode.robot.Robot;


/**
 * Autonomous-period program logic of the robot
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(
        name = "TeleOp",
        group = "Dev"
)
public final class TeleOp extends OpMode {
    @Override
    public void init() {
        Robot.initialize(hardwareMap);
    }

    @Override
    public void loop() {
        throw new UnimplementedException();
    }
}
