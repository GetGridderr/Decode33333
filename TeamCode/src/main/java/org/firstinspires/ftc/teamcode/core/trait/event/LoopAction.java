package org.firstinspires.ftc.teamcode.core.trait.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoopAction implements RobotAction {
    List<RobotAction> actions;
    int currentIndex = 0;

    public LoopAction(List<RobotAction> actions) {
        this.actions = actions;
    }

    public LoopAction() {
        this(new ArrayList<>());
    }

    public LoopAction(RobotAction... actions) {
        this(Arrays.asList(actions));
    }

    public void addAction(RobotAction a) {
        actions.add(a);
    }

    @Override
    public void update() {
        while (currentIndex >= actions.size()) {
            currentIndex -= actions.size();
        }
        RobotAction current = actions.get(currentIndex);
        current.update();
        if (current.isFinished()) currentIndex++;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
