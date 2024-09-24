package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@TeleOp(name="RubiksCubeScannerTest", group="Linear Opmode")
public class RubiksCubeScannerTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    ColorSensor colorSensor;
    private DcMotor baseMotor = null;  // rotates the cube base
    private Servo servo = null;   // color sensor on a servo to scan cube faces

    private enum RobotState {
        INIT,
        SCAN,
        IDLE
    }

    private RobotState currentState = RobotState.INIT;

    // additional variables
    private int hue = 0;
    private String currentColor = "";
    private ArrayList<String> colorArray = new ArrayList<>(); // stores colors scanned from a face

    @Override
    public void runOpMode() {
        // initialize hardware
        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");
        baseMotor = hardwareMap.get(DcMotor.class, "baseMotor");
        servo = hardwareMap.get(Servo.class, "colorServo");

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            switch (currentState) {
                case INIT:
                    telemetry.addData("State", "INIT");
                    // reset motors to prepare for scanning
                    resetMotors();
                    currentState = RobotState.SCAN;
                    break;

                case SCAN:
                    telemetry.addData("State", "SCAN");
                    // scan the current face using color sensor and log into array
                    scanAndLogFaceColors();
                    currentState = RobotState.IDLE;  // stop after scanning one face
                    break;

                case IDLE:
                    telemetry.addData("State", "IDLE");
                    telemetry.addData("Scanned Colors", colorArray.toString());
                    if (gamepad1.a) {  // restart scanning with button press
                        currentState = RobotState.SCAN;
                    }
                    break;
            }

            // update telemetry data
            telemetry.update();
        }
    }

    // function to scan the current face, log colors into an array, and control the servo and motor for scanning
    private void scanAndLogFaceColors() {
        baseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        baseMotor.setTargetPosition(526);  // adjust for 360-degree rotation
        baseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        servo.setPosition(0.8);  // move servo to scanning position
        sleep(1000);

        // perform scanning actions
        for (int i = 0; i < 6; i++) {  // assuming scanning 6 sections
            scanColorAndLog();  // scans a color and adds it to the array

            // toggle servo position for different angles of scan
            if (servo.getPosition() == 1) {
                servo.setPosition(0.7);
            } else {
                servo.setPosition(1);
            }
            sleep(750);
        }

        // rotate the motor for the face scan
        baseMotor.setPower(0.4);
        while (opModeIsActive()) {
            if (baseMotor.getCurrentPosition() >= baseMotor.getTargetPosition()) {
                baseMotor.setPower(0);
                break;
            }
        }
    }

    // function to scan the color, log it, and convert it to hsv
    private void scanColorAndLog() {
        float[] hsvValues = new float[3];
        android.graphics.Color.RGBToHSV(
                (int) (colorSensor.red() * 255),
                (int) (colorSensor.green() * 255),
                (int) (colorSensor.blue() * 255),
                hsvValues
        );

        hue = (int) hsvValues[0];
        currentColor = getColor();  // get the color based on the hue
        colorArray.add(currentColor);  // log color into the array
        telemetry.addData("Scanned Color", currentColor);
    }

    // reset motors to starting positions
    private void resetMotors() {
        baseMotor.setPower(0);
        servo.setPosition(0);
    }

    // function to determine the color based on hue
    private String getColor() {
        if (hue >= 25 && hue <= 40) {
            return "red";
        } else if (hue >= 200 && hue <= 225) {
            return "blue";
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
