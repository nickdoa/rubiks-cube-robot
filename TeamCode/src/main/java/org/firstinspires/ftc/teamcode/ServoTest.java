package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.ArrayList;  // Import ArrayList

@TeleOp
public class ServoTest extends LinearOpMode {
    DcMotor motor;
    Servo servo;
    ColorSensor colorSensor;

    private int hue = 0;
    private String currentColor = "";
    private ArrayList<String> colorArray = new ArrayList<>();  // Create an array to store colors

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

        // Step 1: Extend servo to the middle of the cube
        servo.setPosition(0);  // Adjust as needed to extend sensor to the middle
        sleep(750);

        // Step 2: Move servo back to the edge
        servo.setPosition(0.8);  // Retract to the edge for scanning
        sleep(750);

        // Step 3: Rotate motor to scan corners and edges
        motor.setPower(0.3);
        while (opModeIsActive()) {
            // Retrieve the color sensor values
            float[] hsvValues = new float[3];
            android.graphics.Color.RGBToHSV(
                    (int) (colorSensor.red() * 255),
                    (int) (colorSensor.green() * 255),
                    (int) (colorSensor.blue() * 255),
                    hsvValues
            );

            // Update the hue value
            hue = (int) hsvValues[0];

            // Get the current color based on hue
            currentColor = getColor();

            // Add detected color to the array
            colorArray.add(currentColor);

            if (motor.getCurrentPosition() >= motor.getTargetPosition()) {
                motor.setPower(0);  // Stop the motor after one rotation
                break;  // Exit the loop after one rotation
            }

            // Alternating servo position to scan corners/edges during rotation
            if (servo.getPosition() == 1) {
                servo.setPosition(0.7);  // Adjust slightly for scanning corners
                sleep(300);
            } else {
                servo.setPosition(1);  // Go back to scan edges
                sleep(300);
            }

            telemetry.addData("Motor Pos", motor.getCurrentPosition());
            telemetry.addData("Servo Pos", servo.getPosition());
            telemetry.addData("Current Color", currentColor);
            telemetry.addData("Hue", hue);
            telemetry.update();
        }

        // Motor reset after the loop ends
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        servo.setPosition(1);
        sleep(250);

        // Display all colors stored in the array after the loop
        telemetry.addData("Motor", "Reset complete");
        telemetry.addData("Servo", "Reset complete");
        telemetry.addData("Colors Scanned", colorArray.toString());  // Display the array in telemetry
        telemetry.update();
    }

    // Method to determine the color based on the hue value
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
