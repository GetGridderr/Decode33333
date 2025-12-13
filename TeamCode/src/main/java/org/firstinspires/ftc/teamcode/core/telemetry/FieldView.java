package org.firstinspires.ftc.teamcode.core.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.device.single.Gyro;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;

import java.util.ArrayList;
import java.util.List;


// coding by Timofei
// Untested
public final class FieldView {
    private static final List<double[]> trajectoryHistory = new ArrayList<>();
    private final int maxTrajectoryPoints = 500;
    public final static double inchesPerCm = 1.0 / 2.54;

    public static double robotWidth = 45.0;
    public static double robotLength = 45.0;

    public static double halfWidth = robotWidth * 0.5;
    public static double halfLength = robotLength * 0.5;

    private static void rotatePoints(double[] xPoints, double[] yPoints, double angle) {
        for (int i = 0; i < xPoints.length; i++) {
            double x = xPoints[i];
            double y = yPoints[i];
            double[] currentPosition = {x, y};
            trajectoryHistory.add(currentPosition);
            double[] p = Odometry.rotateVector(x, y, angle);
            xPoints[i] = p[0];
            yPoints[i] = p[1];
        }
    }

    private static void shiftPoints(double[] xPoints, double[] yPoints, double shiftX, double shiftY) {
        for (int j = 0; j < xPoints.length; j++) {
            xPoints[j] += shiftX;
            yPoints[j] += shiftY;
        }
    }

    public static void renderRobot(double robotX, double robotY, double robotYaw) {
        TelemetryPacket packet = new TelemetryPacket();
        packet.fieldOverlay().setScale(inchesPerCm, inchesPerCm);

        double[] currentPosition = {robotX, robotY};
        trajectoryHistory.add(currentPosition);

        double[] xPoints = new double[]{
                +halfWidth,  // front right
                +halfWidth,  // back right
                -halfWidth,  // back left
                -halfWidth};  // front left
        double[] yPoints = new double[]{
                +halfLength,  // front right
                -halfLength,  // back right
                -halfLength,  // back left
                +halfLength};  // front left
        rotatePoints(xPoints, yPoints, robotYaw);
        shiftPoints(xPoints, yPoints, robotX, robotY);
        rotatePoints(xPoints, yPoints, 90);

        packet.fieldOverlay().setFill("blue");
        packet.fieldOverlay().fillPolygon(xPoints, yPoints);
        packet.fieldOverlay().strokeLine(xPoints[0], yPoints[0], xPoints[3], yPoints[3]);
//        drawTrajectory(packet);


        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    public static void renderRobot() {
        renderRobot(
                OdometerPinpoint.getInstance().getX(),
                OdometerPinpoint.getInstance().getY(),
                OdometerPinpoint.getInstance().getYaw()
        );
    }

    private static void drawTrajectory(TelemetryPacket packet) {

        if (trajectoryHistory.size() < 2) return;


        for (int i = 0; i < trajectoryHistory.size() - 1; i++) {
            FtcDashboard.getInstance().getTelemetry().addData("Trajectory", trajectoryHistory.get(i));
            double[] point1 = trajectoryHistory.get(i);
            double[] point2 = trajectoryHistory.get(i + 1);

            packet.fieldOverlay().strokeLine(
                    point1[0], point1[1],
                    point2[0], point2[1]
            );
        }
    }
}