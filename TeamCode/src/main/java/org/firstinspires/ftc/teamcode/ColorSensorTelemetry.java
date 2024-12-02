package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="ColorSensorTelemetry", group="Linear Opmode")
public class ColorSensorTelemetry extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    ColorSensor colorSensor;

    private int hue = 0;
    private String currentColor = "";

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            hue = (int) JavaUtil.rgbToHue(colorSensor.red(), colorSensor.green(), colorSensor.blue());
            currentColor = getColor();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Current Color ", currentColor);
            telemetry.addData("color", hue);
            telemetry.update();
        }
    }

    public String getColor() {
        if (hue >= 0 && hue <= 59) {
            return "red";
        } else if (hue >= 206 && hue <= 225) {
            return "blue";
        } else if (hue >= 145 && hue <= 165) {
            return "green";
        } else if (hue >= 100 && hue <= 110) {
            return "yellow";
        } else if (hue >= 185 && hue <= 195) {
            return "white";
        } else if (hue >= 60 && hue <= 90) {
            return "orange";
        } else {
            return "no color";
        }
    }
}