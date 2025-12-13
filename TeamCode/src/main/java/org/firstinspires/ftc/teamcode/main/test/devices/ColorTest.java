package org.firstinspires.ftc.teamcode.main.test.devices;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.sensor.color.SensorColor;

@TeleOp(name="ColorTest", group="Test")
@Config
public class ColorTest extends OpMode {
    private SensorColor sensorColor;
    public static String colorName = "color_sensor_right";
    @Override
    public void init() {
        AdafruitI2cColorSensor hfColor =
                hardwareMap.get(AdafruitI2cColorSensor.class, colorName);
        sensorColor = new SensorColor(colorName, hfColor);
        sensorColor.initialize(hardwareMap);
    }

    @Override
    public void loop() {
        FtcDashboard.getInstance().getTelemetry().addData("Color Red:", sensorColor.getRed());
        FtcDashboard.getInstance().getTelemetry().addData("Color Blue:", sensorColor.getBlue());
        FtcDashboard.getInstance().getTelemetry().addData("Color Green:", sensorColor.getGreen());
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
