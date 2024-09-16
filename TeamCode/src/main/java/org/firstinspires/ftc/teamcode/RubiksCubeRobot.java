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
    private Servo colorServo = null;   // color sensor on a servo to scan cube faces

    // enum for states
    private enum RobotState {
        INIT,
        SCAN,
        SOLVE,
        ROTATE_FACE,
        FLIP_CUBE,
        IDLE
    }

    private RobotState currentState = RobotState.INIT;

    // additional variables
    private int hue = 0;
    private String currentColor = "";
    private ArrayList<String> moves = new ArrayList<>(); // list of moves to solve the cube

    @Override
    public void runOpMode() {
        // initialize hardware
        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");
        baseMotor = hardwareMap.get(DcMotor.class, "baseMotor");
        flipMotor = hardwareMap.get(DcMotor.class, "flipMotor");
        colorServo = hardwareMap.get(Servo.class, "colorServo");

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
                    // scan the current face using color sensor
                    scanFace();
                    if (isCubeScanned()) {
                        currentState = RobotState.SOLVE;
                    }
                    break;

                case SOLVE:
                    telemetry.addData("State", "SOLVE");
                    // run the solving algorithm (placeholder for now)
                    solveCube();
                    currentState = RobotState.IDLE;  // go idle after solving
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

    // function to scan the current face using the color sensor
    private void scanFace() {
        hue = (int) com.qualcomm.robotcore.util.Range.scale(
                colorSensor.red(), colorSensor.green(), colorSensor.blue(), 0, 255);
        currentColor = getColor();
        telemetry.addData("Current Color", currentColor);
        telemetry.update();
    }

    // function to determine if the cube is fully scanned
    private boolean isCubeScanned() {
        // logic to check if all faces are scanned
        return moves.size() >= 6;  // assuming 6 faces
    }

    // placeholder for solving algorithm
    private void solveCube() {
        // solving logic goes here
        telemetry.addData("Action", "Solving...");
    }

    // function to rotate a specific face of the cube
    private void rotateFace() {
        baseMotor.setPower(0.8);
        sleep(500);  // simulate face rotation
        baseMotor.setPower(0);
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
