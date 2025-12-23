package org.firstinspires.ftc.teamcode.core.telemetry;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.sizeRobotConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.device.single.Gyro;
import org.firstinspires.ftc.teamcode.main.movement.Odometry;

import java.util.ArrayList;
import java.util.List;

public final class FieldView {
    private static final List<double[]> trajectoryHistory = new ArrayList<>();
    private final int maxTrajectoryPoints = 500;
    public final static double inchesPerCm = 1.0 / 2.54;

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
                +sizeRobotConfig.halfWidth,  // front right
                +sizeRobotConfig.halfWidth,  // back right
                -sizeRobotConfig.halfWidth,  // back left
                -sizeRobotConfig.halfWidth};  // front left
        double[] yPoints = new double[]{
                +sizeRobotConfig.halfLength,  // front right
                -sizeRobotConfig.halfLength,  // back right
                -sizeRobotConfig.halfLength,  // back left
                +sizeRobotConfig.halfLength};  // front left
        rotatePoints(xPoints, yPoints, robotYaw);
        shiftPoints(xPoints, yPoints, robotX, robotY);
        rotatePoints(xPoints, yPoints, 90);

        packet.fieldOverlay().setFill("blue");
        packet.fieldOverlay().fillPolygon(xPoints, yPoints);
        packet.fieldOverlay().strokeLine(xPoints[0] / 2.54, yPoints[0] / 2.54, xPoints[3] / 2.54, yPoints[3] / 2.54);
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