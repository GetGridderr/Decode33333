package org.firstinspires.ftc.teamcode.core.trait.event;

public class SingleAction implements RobotAction {
    private final Runnable action;
    private boolean executed;

    public SingleAction(Runnable action) {
        this.action = action;
        executed = false;
    }

    @Override
    public void update() {
        if (executed) return;

        action.run();
        executed = true;
    }

    @Override
    public boolean isFinished() {
        return executed;
    }
}
