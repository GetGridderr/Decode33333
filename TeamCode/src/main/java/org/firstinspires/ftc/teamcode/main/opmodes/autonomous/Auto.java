package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.main.config.Team;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;


@Autonomous(name="Autonomus", group="Dev")
public class Auto extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    private Team colorTeam = Team.UNKNOW;

    @Override
    public void runOpMode() {
        Vehicles.getInstance().initialize(hardwareMap);
        waitForStart();

        if (opModeIsActive()) {
            runtime.reset();
            while (runtime.milliseconds() < 700) {
                Vehicles.getInstance().moveToDirection(-0.5, 0, 0);
            }

        }

    }
}
