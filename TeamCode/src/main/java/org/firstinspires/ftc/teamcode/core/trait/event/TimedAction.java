package org.firstinspires.ftc.teamcode.core.trait.event;


public class TimedAction implements RobotAction {
    RobotAction action;
    WaitAction timer;

    public TimedAction(RobotAction action, double duration) {
        timer = new WaitAction(duration);
        this.action = action;
    }

    @Override
    public void update() {
        timer.update();
        action.update();
    }

    @Override
    public boolean isFinished() {
        return timer.isFinished();
    }
}
