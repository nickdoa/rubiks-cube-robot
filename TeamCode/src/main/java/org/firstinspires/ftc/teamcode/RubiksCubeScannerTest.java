package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

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
    private int tilesScanned = 0;  // counter for tiles scanned

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
                    telemetry.addData("State", "SCAN_CENTER (skipped)");
                    telemetry.update();
                    // Skip scanning the center tile for now

                    // after skipping, move to scanning the outer tiles
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

    // Function to scan the outer tiles with base rotation
    private void scanOuterTiles() {
        // Set motor to rotate base for outer tiles (360 degrees)
        baseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        baseMotor.setTargetPosition(526);  // adjust for 360-degree rotation
        baseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        baseMotor.setPower(0.2);

        // Allow base to rotate while scanning
        while (opModeIsActive() && baseMotor.isBusy() && tilesScanned < 8) {  // Ensure only 8 colors are scanned
            telemetry.addData("Base Rotating", baseMotor.getCurrentPosition());

            // Scan color while base rotates
            sleep(500);
            scanColorAndLog();
            tilesScanned++;  // Increment the count of scanned tiles
              // Wait longer between scans for stable color readings (adjusted to 750ms)
            telemetry.update();
        }

        baseMotor.setPower(0);  // stop motor after rotation
    }

    // Function to scan a color, log it, and convert it to hsv
    private void scanColorAndLog() {
        hue = (int) JavaUtil.rgbToHue(colorSensor.red(), colorSensor.green(), colorSensor.blue());
        currentColor = getColor();  // get the color based on the hue
        colorArray.add(currentColor);  // log color into the array
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Current Color ", currentColor);
        telemetry.addData("Color ", hue);
    }

    // Reset motors and servos to their default positions
    private void resetMotorsAndServos() {
        baseMotor.setPower(0);
        tilesScanned = 0;  // reset the tile counter
    }

    // Function to determine the color based on hue
    public String getColor() {
        if (hue >= 0 && hue <= 69) {
            return "red";
        } else if (hue >= 206 && hue <= 225) {
            return "blue";
        } else if (hue >= 145 && hue <= 165) {
            return "green";
        } else if (hue >= 100 && hue <= 110) {
            return "yellow";
        } else if (hue >= 185 && hue <= 195) {
            return "white";
        } else if (hue >= 70 && hue <= 90) {
            return "orange";
        } else {
            return "no color";
        }
    }
}
