package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;

@TeleOp(name="RubiksCubeRobot", group="Linear Opmode")
public class RubiksCubeRobot extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    ColorSensor colorSensor;
    private DcMotor baseMotor = null;  // rotates the cube base
    private DcMotor flipMotor = null;  // flips the cube
    private Servo servo = null;   // color sensor on a servo to scan cube faces

    // enum for states
    private enum RobotState {
        INIT,
        SCAN,
        ANALYZE,
        SOLVE,
        ROTATE_FACE,
        FLIP_CUBE,
        IDLE
    }

    private RobotState currentState = RobotState.INIT;

    // additional variables
    private int hue = 0;
    private String currentColor = "";
    private ArrayList<String> colorArray = new ArrayList<>(); // stores colors scanned from a face
    private ArrayList<String> moves = new ArrayList<>(); // list of moves to solve the cube

    @Override
    public void runOpMode() {
        // initialize hardware
        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");
        baseMotor = hardwareMap.get(DcMotor.class, "baseMotor");
        flipMotor = hardwareMap.get(DcMotor.class, "flipMotor");
        servo = hardwareMap.get(Servo.class, "colorServo");

        // set initial state
        currentState = RobotState.INIT;

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
                    if (isCubeScanned()) {
                        currentState = RobotState.ANALYZE;
                    }
                    break;

                case ANALYZE:
                    telemetry.addData("State", "ANALYZE");
                    // analyze the scanned colors and prepare solve steps
                    analyzeCube();
                    currentState = RobotState.SOLVE;
                    break;

                case SOLVE:
                    telemetry.addData("State", "SOLVE");
                    // run the solving algorithm (placeholder for now)
                    if (moves.isEmpty()) {
                        telemetry.addData("Action", "No moves, solving completed.");
                        currentState = RobotState.IDLE;
                    } else {
                        currentState = RobotState.ROTATE_FACE;  // move to rotating the face
                    }
                    break;

                case ROTATE_FACE:
                    telemetry.addData("State", "ROTATE_FACE");
                    // rotate the cube face for solving
                    rotateFace();
                    currentState = RobotState.SOLVE;  // return to solving after rotating
                    break;

                case FLIP_CUBE:
                    telemetry.addData("State", "FLIP_CUBE");
                    // flip the cube to scan or reorient
                    flipCube();
                    currentState = RobotState.SCAN;  // scan next face after flipping
                    break;

                case IDLE:
                    telemetry.addData("State", "IDLE");
                    // wait for user input to restart scanning
                    if (gamepad1.a) {
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

    // function to analyze the scanned colors and prepare solve steps
    private void analyzeCube() {
        // placeholder for the logic that will analyze the cube's scanned faces
        telemetry.addData("Action", "Analyzing cube...");
        // add logic here to analyze and generate solving moves
        moves.add("rotate left");  // example move
        moves.add("rotate right"); // example move
    }

    // function to determine if the cube is fully scanned
    private boolean isCubeScanned() {
        // logic to check if all faces are scanned
        return colorArray.size() >= 6;  // assuming 6 faces scanned
    }

    // function to rotate a specific face of the cube
    private void rotateFace() {
        baseMotor.setPower(0.8);
        sleep(500);  // simulate face rotation
        baseMotor.setPower(0);
        moves.remove(0);  // remove the move after it's executed
    }

    // function to flip the cube to access other faces
    private void flipCube() {
        flipMotor.setPower(0.8);
        sleep(1000);  // simulate flipping motion
        flipMotor.setPower(0);
    }

    // reset motors to starting positions
    private void resetMotors() {
        baseMotor.setPower(0);
        flipMotor.setPower(0);
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
