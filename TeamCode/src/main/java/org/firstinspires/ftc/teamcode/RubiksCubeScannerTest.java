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
        SCAN_CENTER,
        SCAN_OUTER,
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
            telemetry.addData("Current State", currentState.toString());

            switch (currentState) {
                case INIT:
                    telemetry.addData("State", "INIT");
                    // reset motors and servos to starting positions
                    resetMotorsAndServos();
                    telemetry.update();

                    // transition to SCAN_CENTER when ready
                    currentState = RobotState.SCAN_CENTER;
                    break;

                case SCAN_CENTER:
                    telemetry.addData("State", "SCAN_CENTER");
                    telemetry.update();
                    // scan the center tile first without moving the base
                    scanCenterTile();

                    // after the center scan, move to scanning the outer tiles
                    currentState = RobotState.SCAN_OUTER;
                    break;

                case SCAN_OUTER:
                    telemetry.addData("State", "SCAN_OUTER");
                    telemetry.update();

                    // rotate base and scan the 8 outer tiles
                    scanOuterTiles();

                    // once done, transition to IDLE state
                    currentState = RobotState.IDLE;
                    break;

                case IDLE:
                    telemetry.addData("State", "IDLE");
                    telemetry.addData("Scanned Colors", colorArray.toString());
                    telemetry.update();

                    // allow restarting scanning with a button press
                    if (gamepad1.a) {
                        currentState = RobotState.SCAN_CENTER;
                    }
                    break;
            }

            telemetry.update();  // always keep telemetry updated
        }
    }

    // Function to scan the center tile
    private void scanCenterTile() {
        servo.setPosition(1);  // extend servo to center tile position
        sleep(1000);  // wait for servo to stabilize

        // Scan the center tile
        scanColorAndLog();
        telemetry.addData("Center Tile Scanned", currentColor);

        // Retract servo after scanning center tile
        servo.setPosition(0);
        sleep(1000);  // wait for servo to fully retract
    }

    // Function to scan the outer tiles with base rotation
    private void scanOuterTiles() {
        // Set motor to rotate base for outer tiles (360 degrees)
        baseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        baseMotor.setTargetPosition(526);  // adjust for 360-degree rotation
        baseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        baseMotor.setPower(0.3);

        // Allow base to rotate while scanning
        while (opModeIsActive() && baseMotor.isBusy()) {
            telemetry.addData("Base Rotating", baseMotor.getCurrentPosition());

            // move servo to different positions to scan each outer tile
            if (servo.getPosition() == 1) {
                servo.setPosition(0.6);
            } else {
                servo.setPosition(0.8);
            }

            // Scan outer tile while base rotates
            scanColorAndLog();
            sleep(250);  // wait between servo movements for stable scan

            telemetry.update();
        }

        baseMotor.setPower(0);  // stop motor after rotation
    }

    // Function to scan a color, log it, and convert it to hsv
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

    // Reset motors and servos to their default positions
    private void resetMotorsAndServos() {
        baseMotor.setPower(0);
        servo.setPosition(0);
    }

    // Function to determine the color based on hue
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
