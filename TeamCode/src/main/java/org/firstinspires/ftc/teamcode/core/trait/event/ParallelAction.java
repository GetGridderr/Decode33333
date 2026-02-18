package org.firstinspires.ftc.teamcode.core.trait.event;

import java.util.Arrays;
import java.util.List;


public class ParallelAction implements RobotAction {
    public enum FinishMode {ALL, ANY}

    public FinishMode mode;
    List<RobotAction> actions;

    public ParallelAction(List<RobotAction> actions, FinishMode mode) {
        this.actions = actions;
        this.mode = mode;
    }

    public ParallelAction(List<RobotAction> actions) {
        this(actions, FinishMode.ALL);
    }

    public ParallelAction(RobotAction... actions) {
        this(Arrays.asList(actions));
    }

    @Override
    public void update() {
        for(RobotAction a : actions) a.update();
    }

    @Override
    public boolean isFinished() {
        if(mode == FinishMode.ALL) {
            for(RobotAction a : actions) if (!a.isFinished()) return false;
            return true;
        } else {
            for(RobotAction a : actions) if (a.isFinished()) return true;
            return false;
        }
    }

}
