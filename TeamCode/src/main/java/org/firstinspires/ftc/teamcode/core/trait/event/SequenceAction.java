package org.firstinspires.ftc.teamcode.core.trait.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SequenceAction implements RobotAction {
    List<RobotAction> actions;
    int currentIndex = 0;

    public SequenceAction(List<RobotAction> actions) {
        this.actions = actions;
    }

    public SequenceAction() {
        this(new ArrayList<>());
    }

    public SequenceAction(RobotAction... actions) {
        this(Arrays.asList(actions));
    }

    public void addAction(RobotAction a) {
        actions.add(a);
    }

    @Override
    public void update() {
        if(currentIndex < actions.size()) {
            RobotAction current = actions.get(currentIndex);
            current.update();
            if (current.isFinished()) currentIndex++;
        }
    }

    @Override
    public boolean isFinished() {
        return currentIndex >= actions.size();
    }
}
