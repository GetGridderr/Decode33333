package org.firstinspires.ftc.teamcode.core.util;

public final class Util {
    public static final double SECONDS_PER_NANOSECOND = 1.E-9;
    public static final double RAD_PER_DEG = Math.PI / 180.0;
    public static final double DEG_PER_RAD = 180.0 / Math.PI;
    public static final double INCHES_PER_CM = 1.0 / 2.54;


    public static double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    public static double getShortestPathToAngle(double currentAngle, double targetAngle) {
        double difference = targetAngle - currentAngle;
        return normalizeAngle(difference);
    }

    public static double[] rotateVector(double x, double y, double rot) {
        final double cos = Math.cos(rot * RAD_PER_DEG);
        final double sin = Math.sin(rot * RAD_PER_DEG);
        return new double[]{cos * x + sin * y, cos * y - sin * x};
    }

    public static double clamp(double x, double min, double max) {
        x = Math.min(x, max);
        x = Math.max(x, min);
        return x;
    }

    public static double interp(double xMax, double xMin, double x, double yMax, double yMin) {
        double t = (x - xMin) / (xMax - xMin);
        t = clamp(t, 0, 1);
        return yMin + (yMax - yMin) * t;
    }
}
