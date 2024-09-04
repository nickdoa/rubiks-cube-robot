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

@TeleOp(name="RubiksCubeRobot", group="Linear Opmode")
public class RubiksCubeRobot extends LinearOpMode {

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

    private static final int TOP = 0;
    private static final int BOTTOM = 1;
    private static final int FRONT = 2;
    private static final int BACK = 3;
    private static final int LEFT = 4;
    private static final int RIGHT = 5;

    private int hue = 0;
    private String currentColor = "";

    private char[][] cube = new char[6][9]; // the rubik's cube matrix
    private char[][] copy = new char[6][9];  // copy of the cube
    private char[] faceColor = { 'R', 'O', 'W', 'Y', 'G', 'B' };  // colors of each face
    private String[] move = new String[100];  // stores the sequence of moves
    private int moveNum = 0;  // Counter for the number of moves
    private int start = 0;  // Starting index for printing moves// the cube matrix

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
    private void turnSide(int side, int turn) {
        int[] adoptA = { 2, 5, 8, 1, 4, 7, 0, 3, 6 };
        int[][][] adoptB = {
            { { LEFT, 6, 3, 0 }, { BACK, 6, 3, 0 }, { RIGHT, 6, 3, 0 }, { FRONT, 6, 3, 0 } },   // top
            { { LEFT, 2, 5, 8 }, { FRONT, 2, 5, 8 }, { RIGHT, 2, 5, 8 }, { BACK, 2, 5, 8 } },   // bottom
            { { LEFT, 8, 7, 6 }, { TOP, 2, 5, 8 }, { RIGHT, 0, 1, 2 }, { BOTTOM, 6, 3, 0 } },   // front
            { { RIGHT, 8, 7, 6 }, { TOP, 6, 3, 0 }, { LEFT, 0, 1, 2 }, { BOTTOM, 2, 5, 8 } },   // back
            { { FRONT, 8, 7, 6 }, { TOP, 8, 7, 6 }, { BACK, 0, 1, 2 }, { BOTTOM, 8, 7, 6 } },   // right
            { { BACK, 8, 7, 6 }, { TOP, 0, 1, 2 }, { FRONT, 0, 1, 2 }, { BOTTOM, 0, 1, 2 } }    // left
        };

        char[][] copy2 = new char[6][9];
        int faceO, blockO, faceN, blockN;

        for (int i = 0; i < turn; i++) {
            // Create a copy of the cube
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 9; k++) {
                    copy2[j][k] = cube[j][k];
                }
            }

            // Rotate the primary face
            for (int j = 0; j < 9; j++) {
                cube[side][j] = copy2[side][adoptA[j]];
            }

            // Rotate the secondary faces
            for (int k = 0; k < 4; k++) {
                for (int p = 1; p < 4; p++) {
                    faceN = adoptB[side][k][0];
                    blockN = adoptB[side][k][p];

                    if (k == 3) {
                        faceO = adoptB[side][0][0];
                        blockO = adoptB[side][0][p];
                    } else {
                        faceO = adoptB[side][k + 1][0];
                        blockO = adoptB[side][k + 1][p];
                    }

                    cube[faceO][blockO] = copy2[faceN][blockN];
                }
            }
        }

        // Log the move
        if (turn != 0) {
            switch (turn) {
                case 1:
                    move[moveNum] = String.valueOf(faceColor[side]);
                    break;
                case 2:
                    move[moveNum] = "2" + faceColor[side];
                    break;
                case 3:
                    move[moveNum] = "'" + faceColor[side] + "'";
                    break;
            }
            moveNum++;
        }
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
    // update telemetry
    private void printMoves(String amount) {
        int i = 0;
        if (amount.equals("ALL")) {
            i = 0;
        } else if (amount.equals("STEP")) {
            i = start;
        }

        for (int j = i; j < moveNum; j++) {
            telemetry.addData("Move", move[j]);
        }
        telemetry.update();
    }
}


