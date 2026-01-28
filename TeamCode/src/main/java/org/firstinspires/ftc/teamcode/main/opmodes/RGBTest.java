package org.firstinspires.ftc.teamcode.main.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.device.RGBChannel;

// coding by Timofei

@Config
@TeleOp(name="RGBTest", group="Calibrate")
public class RGBTest extends OpMode
{
    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
    private final RGBChannel gChan = new RGBChannel("servo0", RGBChannel.SignalPin.MINUS);
    private final RGBChannel bChan = new RGBChannel("servo1", RGBChannel.SignalPin.MINUS);
    private final RGBChannel rChan = new RGBChannel("servo2", RGBChannel.SignalPin.MINUS);
    public static double r = 0, g = 0, b = 0;

    // Rainbow animation params
    public static boolean enableRainbow = false;
    public static double rainbowSpeed = 1.0;  // loops per second
    public static double saturation = 1.0;
    public static double value = 1.0;

    /*
     * Функция преобразования HSV в RGB
     * H (Hue) - оттенок: 0-360 градусов
     * S (Saturation) - насыщенность: 0-1
     * V (Value) - яркость: 0-1
     * Возвращает массив [R, G, B] в диапазоне 0-1
     */
    private double[] hsvToRgb(double h, double s, double v) {
        double[] rgb = new double[3];

        h = h % 360.0;
        if (h < 0) h += 360.0;
        s = Math.max(0, Math.min(1, s));
        v = Math.max(0, Math.min(1, v));

        double c = v * s;
        double x = c * (1 - Math.abs((h / 60.0) % 2 - 1));
        double m = v - c;

        if (h >= 0 && h < 60) {
            rgb[0] = c; rgb[1] = x; rgb[2] = 0;
        } else if (h >= 60 && h < 120) {
            rgb[0] = x; rgb[1] = c; rgb[2] = 0;
        } else if (h >= 120 && h < 180) {
            rgb[0] = 0; rgb[1] = c; rgb[2] = x;
        } else if (h >= 180 && h < 240) {
            rgb[0] = 0; rgb[1] = x; rgb[2] = c;
        } else if (h >= 240 && h < 300) {
            rgb[0] = x; rgb[1] = 0; rgb[2] = c;
        } else { // h >= 300 && h < 360
            rgb[0] = c; rgb[1] = 0; rgb[2] = x;
        }

        rgb[0] += m;
        rgb[1] += m;
        rgb[2] += m;

        return rgb;
    }

    private double[] getRainbowColor() {
        double hue = (runtime.seconds() * 360.0 * rainbowSpeed) % 360.0;
        return hsvToRgb(hue, saturation, value);
    }

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Controls", "Enable/disable rainbow via Dashboard");
        rChan.initialize(hardwareMap);
        gChan.initialize(hardwareMap);
        bChan.initialize(hardwareMap);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        if (enableRainbow) {
            double[] rainbowColor = getRainbowColor();

            r = rainbowColor[0];
            g = rainbowColor[1];
            b = rainbowColor[2];

            telemetry.addData("Mode", "RAINBOW ANIMATION");
            telemetry.addData("Hue (calculated)", (runtime.seconds() * 360.0 * rainbowSpeed) % 360.0);
        } else {
            telemetry.addData("Mode", "MANUAL (via Dashboard)");
        }

        rChan.setPower(r);
        gChan.setPower(g);
        bChan.setPower(b);

        telemetry.addData("Red", "%.3f", r);
        telemetry.addData("Green", "%.3f", g);
        telemetry.addData("Blue", "%.3f", b);
        telemetry.addData("Rainbow Enabled", enableRainbow);
        telemetry.addData("Rainbow Speed", "%.2f cycles/sec", rainbowSpeed);
        telemetry.addData("Time", "%.1f sec", runtime.seconds());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
