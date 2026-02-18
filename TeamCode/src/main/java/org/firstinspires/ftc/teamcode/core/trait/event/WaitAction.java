package org.firstinspires.ftc.teamcode.core.trait.event;

import com.qualcomm.robotcore.util.ElapsedTime;


public class WaitAction implements RobotAction {
    double duration;
    ElapsedTime elapsedTime;
    boolean started = false;

    public WaitAction(double duration) {
        this.duration = duration;
        this.elapsedTime = new ElapsedTime();
    }

    @Override
    public void update() {
        if(!started) {
            elapsedTime.reset();
            started = true;
        }
    }

    @Override
    public boolean isFinished() {
        if(!started) return false;
        return elapsedTime.seconds() >= duration;
    }
}
