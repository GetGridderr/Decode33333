package org.firstinspires.ftc.teamcode.main.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.main.movement.Vehicles;


@Autonomous(name="Autonomus", group="Dev")
public class Auto extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        Vehicles.getInstance().initialize(hardwareMap);
        waitForStart();

        if (opModeIsActive()) {
            Vehicles.getInstance().goTo(50, 20, 30);
        }

    }
}

