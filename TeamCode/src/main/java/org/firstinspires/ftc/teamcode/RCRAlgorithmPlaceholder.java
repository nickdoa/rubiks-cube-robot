package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@TeleOp(name="RCRAlgorithm,", group="Linear Opmode")
public class RCRAlgorithmPlaceholder extends LinearOpMode {

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

    private static final int top = 0;
    private static final int bottom = 1;
    private static final int front = 2;
    private static final int back = 3;
    private static final int right = 4;
    private static final int left = 5;
    private static final int all = -2;

    private String[] move_name = {"U", "D", "F", "B", "R", "L"};

    private int hue = 0;
    private String currentColor = "";

    private char[][] cube = new char[6][9]; // the rubik's cube matrix
    private char[][] copy = new char[6][9];  // copy of the cube
    private char[] faceColor = { 'R', 'O', 'W', 'Y', 'G', 'B' };  // colors of each face
    private String[] move = new String[100];  // stores the sequence of moves
    private int moveNum = 0;  // counter for the number of moves
    private int start = 0;  // starting index for printing moves

    private ArrayList<String> moves = new ArrayList<>(); // List of moves
    private int move_num = 0;

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

    /*
    function to rotate a face of the cube
    adoption assignment of affected blocks for all four sides after a turn:
    - each level corresponds to a side.
    - each level contains four sub-arrays which represent the face and the specific blocks impacted by the turn.
    */

    private void turnSide(int side, int turn) {
        int[] adoptA = {2, 5, 8, 1, 4, 7, 0, 3, 6};  // cube rotation mapping
        int[][][] adoptB = {
                {{left, 6, 3, 0}, {back, 6, 3, 0}, {right, 6, 3, 0}, {front, 6, 3, 0}},  // top
                {{left, 2, 5, 8}, {front, 2, 5, 8}, {right, 2, 5, 8}, {back, 2, 5, 8}},  // bottom
                {{left, 8, 7, 6}, {top, 2, 5, 8}, {right, 0, 1, 2}, {bottom, 6, 3, 0}},  // front
                {{right, 8, 7, 6}, {top, 6, 3, 0}, {left, 0, 1, 2}, {bottom, 2, 5, 8}},  // back
                {{front, 8, 7, 6}, {top, 8, 7, 6}, {back, 0, 1, 2}, {bottom, 8, 7, 6}},  // right
                {{back, 8, 7, 6}, {top, 0, 1, 2}, {front, 0, 1, 2}, {bottom, 0, 1, 2}}   // left
        };

        char[][] copy2 = new char[6][9];

        // copy the cube's state
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = cube[i][j];
            }
        }

        // perform the rotation for the given number of turns
        for (int i = 0; i < turn; i++) {
            // rotate the primary face
            for (int j = 0; j < 9; j++) {
                cube[side][j] = copy[side][adoptA[j]];
            }

            // rotate the affected adjacent faces
            for (int k = 0; k < 4; k++) {
                for (int p = 1; p < 4; p++) {
                    int faceN = adoptB[side][k][0];
                    int blockN = adoptB[side][k][p];
                    int faceO = (k == 3) ? adoptB[side][0][0] : adoptB[side][k + 1][0]; //define the old face
                    int blockO = (k == 3) ? adoptB[side][0][p] : adoptB[side][k + 1][p]; //define the old block

                    cube[faceO][blockO] = copy[faceN][blockN]; // replace the old face with the new face
                }
            }
        }

        // record the move
        if (turn > 0) {
            String moveCommand = (turn == 1) ? move_name[side] :
                    (turn == 2) ? "2" + move_name[side] :
                            "'" + move_name[side] + "'";
            moves.add(moveCommand);
            move_num++;
        }

        // log the move
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

    // prints a matrix of the input face of the cube
    public void printCube(int face) {
        if (face == all) {
            String[] name = { "TOP", "BOTTOM", "FRONT", "BACK", "RIGHT", "LEFT" };  // the names of each face of the cube
            for (int i = 0; i < 6; i = i + 3) {
                telemetry.addData(name[i] + "\t\t", name[i + 1] + "\t\t" + name[i + 2]);  // prints the name of the face
                telemetry.update();
                for (int j = 0; j < 3; j++) {
                    telemetry.addData(cube[i][j] + "\t", cube[i][j + 3] + "\t" + cube[i][j + 6] + "\t\t" +
                            cube[i + 1][j] + "\t" + cube[i + 1][j + 3] + "\t" + cube[i + 1][j + 6] + "\t\t" +
                            cube[i + 2][j] + "\t" + cube[i + 2][j + 3] + "\t" + cube[i + 2][j + 6]);
                    telemetry.update();
                }
                telemetry.addLine();
                telemetry.update();
            }
        } else {
            // prints an individual face
            for (int i = 0; i < 3; i++) {
                telemetry.addData(cube[face][i] + "\t", cube[face][i + 3] + "\t" + cube[face][i + 6]);
                telemetry.update();
            }
        }
    }

    // makes a copy of the current cube config
    public void copyCube() {
        // creates a copy of the cube
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = cube[i][j];
            }
        }
    }

    /*
    // simplifies the moves by combining like moves that are next to each other
    public void simplifyMoves() {
        ArrayList<String> move_new = new ArrayList<>();
        String carry;
        int count, skip;

        // simplifies the moves, runs 5 times
        for (int repeat = 0; repeat < 5; repeat++) {
            count = 0;
            skip = 0;

            // checks the moves directly next and simplifies
            for (int i = 0; i < move_num - 1; i++) {
                if (skip == 0) {
                    // if this move and the next are the same and single character
                    if (move.get(i + 1).equals(move.get(i)) && move.get(i).length() == 1) {
                        move_new.add("2" + move.get(i));
                        count++;
                        skip = 1;
                    }
                    // if this move and the next are the same and 3 characters
                    else if (move.get(i + 1).equals(move.get(i)) && move.get(i).length() == 3) {
                        switch (move.get(i)) {
                            case "'U'": move_new.add("2U"); break;
                            case "'D'": move_new.add("2D"); break;
                            case "'F'": move_new.add("2F"); break;
                            case "'B'": move_new.add("2B"); break;
                            case "'R'": move_new.add("2R"); break;
                            case "'L'": move_new.add("2L"); break;
                        }
                        count++;
                        skip = 1;
                    }
                    // if this move and the next are the same and double character
                    else if (move.get(i + 1).equals(move.get(i)) && move.get(i).length() == 2) {
                        skip = 1;
                    }
                    // if the next move is double this move
                    else if (move.get(i + 1).equals("2" + move.get(i))) {
                        move_new.add("'" + move.get(i) + "'");
                        count++;
                        skip = 1;
                    }
                    // if the next move is half this move
                    else if (move.get(i).equals("2" + move.get(i + 1))) {
                        move_new.add("'" + move.get(i + 1) + "'");
                        count++;
                        skip = 1;
                    }
                    // if the next move is in the opposite direction of this move they cancel
                    else if (move.get(i + 1).equals("'" + move.get(i) + "'") || move.get(i).equals("'" + move.get(i + 1) + "'")) {
                        skip = 1;
                    }
                    // specific cases: 2U + 'U' (defined explicitly)
                    else if ((move.get(i).equals("2U") && move.get(i + 1).equals("'U'")) || (move.get(i + 1).equals("2U") && move.get(i).equals("'U'"))) {
                        move_new.add("U");
                        skip = 1;
                        count++;
                    } else if ((move.get(i).equals("2D") && move.get(i + 1).equals("'D'")) || (move.get(i + 1).equals("2D") && move.get(i).equals("'D'"))) {
                        move_new.add("D");
                        skip = 1;
                        count++;
                    } else if ((move.get(i).equals("2F") && move.get(i + 1).equals("'F'")) || (move.get(i + 1).equals("2F") && move.get(i).equals("'F'"))) {
                        move_new.add("F");
                        skip = 1;
                        count++;
                    } else if ((move.get(i).equals("2B") && move.get(i + 1).equals("'B'")) || (move.get(i + 1).equals("2B") && move.get(i).equals("'B'"))) {
                        move_new.add("B");
                        skip = 1;
                        count++;
                    } else if ((move.get(i).equals("2R") && move.get(i + 1).equals("'R'")) || (move.get(i + 1).equals("2R") && move.get(i).equals("'R'"))) {
                        move_new.add("R");
                        skip = 1;
                        count++;
                    } else if ((move.get(i).equals("2L") && move.get(i + 1).equals("'L'")) || (move.get(i + 1).equals("2L") && move.get(i).equals("'L'"))) {
                        move_new.add("L");
                        skip = 1;
                        count++;
                    } else {
                        move_new.add(move.get(i));
                        count++;
                    }
                } else if (skip == 1) {
                    skip = 0;  // reset skip
                }
            }

            // gets the last move
            if (skip == 0) {
                move_new.add(move.get(move_num - 1));
                count++;
            }

            // saves new moves to the move array
            move.clear();
            move.addAll(move_new);
            move_num = count;
        }

        // checks if the next move doesn't affect this move, switch their placement
        for (int i = 0; i < move_num - 2; i++) {
            String currentMove = move.get(i);
            String nextMove = move.get(i + 1);
            String nextNextMove = move.get(i + 2);
            if (currentMove.equals("U") || currentMove.equals("'U'") || currentMove.equals("2U")) {
                if (nextMove.equals("D") || nextMove.equals("'D'") || nextMove.equals("2D")) {
                    if (nextNextMove.equals("U") || nextNextMove.equals("'U'") || nextNextMove.equals("2U")) {
                        carry = nextMove;
                        move.set(i + 1, nextNextMove);
                        move.set(i + 2, carry);
                    }
                }
            }
            // similar checks for D, F, B, R, L...
        }
    }

     */

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


