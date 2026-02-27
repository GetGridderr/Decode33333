/**
 * @author Timofey Istomin
 */

package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.core.util.Util;


public final class FieldRenderer {
    public static double robotWidth = 37.4;
    public static double robotLength = 37.1;

    public static double halfWidth = robotWidth * 0.5;
    public static double halfLength = robotLength * 0.5;


    private static void rotatePoints(double[] xPoints, double[] yPoints, double angle) {
        for (int i = 0; i < xPoints.length; i++) {
            double x = xPoints[i];
            double y = yPoints[i];
            double[] p = Util.rotateVector(x, y, angle);
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
        packet.fieldOverlay().setScale(Util.INCHES_PER_CM, Util.INCHES_PER_CM);

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
        rotatePoints(xPoints, yPoints, 90);  // x, y = y, -x

        packet.fieldOverlay().setFill("blue");
        packet.fieldOverlay().setStrokeWidth(2);
        packet.fieldOverlay().fillPolygon(xPoints, yPoints);
        packet.fieldOverlay().strokeLine(xPoints[0], yPoints[0], xPoints[3], yPoints[3]);
        packet.fieldOverlay().setFill("white");
        packet.fieldOverlay().fillCircle(Robot.gunY, -Robot.gunX, 9.5);
        packet.fieldOverlay().fillCircle(WebConfig.GoalY, WebConfig.GoalX, 5);
        packet.fieldOverlay().fillCircle(WebConfig.GoalY, -WebConfig.GoalX, 5);

        FtcDashboard.getInstance().sendTelemetryPacket(packet);
        FtcDashboard.getInstance().getTelemetry().addData("X", Robot.getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y", Robot.getY());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw", Robot.getYaw());
        FtcDashboard.getInstance().getTelemetry().update();
    }

    public static void renderRobot() {
        renderRobot(
                Robot.getX(),
                Robot.getY(),
                Robot.getYaw()
        );
    }
}
