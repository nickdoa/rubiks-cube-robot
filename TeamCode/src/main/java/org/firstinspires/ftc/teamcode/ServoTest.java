package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.ArrayList;

@TeleOp
public class ServoTest extends LinearOpMode {
    DcMotor motor;
    Servo servo;
    ColorSensor colorSensor;

    private int hue = 0;
    private String currentColor = "";
    private ArrayList<String> colorArray = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.get(Servo.class, "one_Servo");
        motor = hardwareMap.get(DcMotor.class, "one_Drive");
        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(526);  // Adjust for 360-degree rotation
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        servo.setPosition(0.8);
        sleep(1000);

        servo.setPosition(0);
        sleep(750);

        servo.setPosition(0.8);
        sleep(750);

        motor.setPower(0.4);
        while (opModeIsActive()) {

            float[] hsvValues = new float[3];
            android.graphics.Color.RGBToHSV(
                    (int) (colorSensor.red() * 255),
                    (int) (colorSensor.green() * 255),
                    (int) (colorSensor.blue() * 255),
                    hsvValues
            );

            hue = (int) hsvValues[0];

            currentColor = getColor();

            colorArray.add(currentColor);

            if (motor.getCurrentPosition() >= motor.getTargetPosition()) {
                motor.setPower(0);
                break;
            }

            if (servo.getPosition() == 1) {
                servo.setPosition(0.7);
                sleep(300);
            } else {
                servo.setPosition(1);
                sleep(300);
            }

            telemetry.addData("Motor Pos", motor.getCurrentPosition());
            telemetry.addData("Servo Pos", servo.getPosition());
            telemetry.addData("Current Color", currentColor);
            telemetry.addData("Hue", hue);
            telemetry.update();
        }

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        servo.setPosition(1);
        sleep(250);

        telemetry.addData("Motor", "Reset complete");
        telemetry.addData("Servo", "Reset complete");
        telemetry.addData("Colors Scanned", colorArray.toString());
        telemetry.update();
    }

    public String getColor() {
        if (hue >= 25 && hue <= 40) {
            return "red";
        } else if (hue >= 200 && hue <= 225) {
            return "blue";
        } else if (hue >= 130 && hue <= 135) {
            return "no color";
        } else if (hue >= 120 && hue <= 140) {
            return "green";
        } else if (hue >= 90 && hue <= 110) {
            return "yellow";
        } else if (hue >= 170 && hue <= 180) {
            return "white";
        } else if (hue >= 45 && hue <= 55) {
            return "orange";
        } else {
            return "no color";
        }
    }
}
