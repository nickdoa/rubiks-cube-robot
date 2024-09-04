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


@TeleOp(name="StateMachTestBed", group="Linear Opmode")
public class StateMachTestBed extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    ColorSensor colorSensor;
    private DcMotor oneDrive = null;
    private DcMotor twoDrive = null;
    private DcMotor threeDrive = null;
    private DcMotor fourDrive = null;

    private Servo oneServo = null;
    private Servo twoServo = null;
    private Servo threeServo = null;
    private Servo fourServo = null;

    private int hue = 0;
    private String currentColor = "";

    private char[][] cube = new char[6][9]; // the cube matrix

    @Override

    public void runOpMode () {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");

        oneDrive = hardwareMap.get(DcMotor.class, "one_Drive");
        twoDrive = hardwareMap.get(DcMotor.class, "two_Drive");
        threeDrive = hardwareMap.get(DcMotor.class, "three_Drive");
        fourDrive = hardwareMap.get(DcMotor.class, "four_Drive");

        oneServo = hardwareMap.get(Servo.class, "one_Servo");
        twoServo = hardwareMap.get(Servo.class, "two_Servo");
        threeServo = hardwareMap.get(Servo.class, "three_Servo");
        fourServo = hardwareMap.get(Servo.class, "four_Servo");

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            hue = (int) JavaUtil.rgbToHue(colorSensor.red(), colorSensor.green(), colorSensor.blue());
            currentColor = getColor();
            switch (currentColor) {
                case "red": {
                    threeDrive.setPower(.8);
                    break;
                } // end step 10

                case "blue": {
                    oneDrive.setPower(.8);
                    break;
                }

                case "green": {
                    twoDrive.setPower(.8);
                    break;
                }

                case "yellow": {
                    fourDrive.setPower(.8);
                    break;
                }

                case "white": {
                    oneServo.setPosition(1);
                    sleep(1000);
                    oneServo.setPosition(-1);
                    break;
                }

                case "orange": {
                    twoServo.setPosition(1);
                    sleep(1000);
                    twoServo.setPosition(-1);
                }

                default: {
                    threeDrive.setPower(0);
                    twoDrive.setPower(0);
                    fourDrive.setPower(0);
                    oneDrive.setPower(0);
                    oneServo.setPosition(0);
                    twoServo.setPosition(0);
                    break;
                }
            } // end switch
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Current Color ", currentColor);
            telemetry.addData("color", hue);
            telemetry.update();
        }
    }

    public String getColor () {
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
    // function to rotate a face of the cube
    private void turnSide(int face, int turn) {
        // rotate the base to position the correct face
        rotateBaseToFace(face);

        // turns using the arm
        for (int i = 0; i < turn; i++) {
            rotateFace();
        }

        // update the cube matrix
        updateCubeMatrix(face, turn);
    }

    private void rotateBaseToFace(int face) {
        // rotate the robot's base to align with the correct face
        // use the motors
    }

    private void rotateFace() {
        // logic to rotate the face using the Servo(s)
        // this would be the physical movement to rotate the cube's face
        oneServo.setPosition(0.5);  // example, adjust for setup
    }

    private void updateCubeMatrix(int face, int turn) {
        // logic to update the cube matrix after a face rotation
    }
}


