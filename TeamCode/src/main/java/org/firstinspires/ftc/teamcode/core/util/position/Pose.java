package org.firstinspires.ftc.teamcode.core.util.position;

public class Pose {
    public double x;
    public double y;
    public double yaw;
    private static final org.firstinspires.ftc.teamcode.core.util.position.Pose INSTANCE = new org.firstinspires.ftc.teamcode.core.util.position.Pose();

    public static org.firstinspires.ftc.teamcode.core.util.position.Pose getInstance() { return INSTANCE; }

    public Pose() {
        this.x = 0;
        this.y = 0;
        this.yaw = 0;
    }
    public Pose(double x, double y, double yaw) {
        this.x = x;
        this.y = y;
        this.yaw = yaw;
    }
}
