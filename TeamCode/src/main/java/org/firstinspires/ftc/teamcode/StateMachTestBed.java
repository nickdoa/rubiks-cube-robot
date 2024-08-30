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

        char[][] cube = new char[6][9];
        char[][] copy = new char[6][9];
        char[] face_color = { 'R', 'O', 'W', 'Y', 'G', 'B' };
        char[][] pair = { { 'R', 'G' }, { 'G', 'O' }, { 'B', 'R' }, { 'O', 'B' } };
        String[] move = new String[100];
        int move_num = 0;
        int start = 0;

        void turnSide(int side, int turn) {
            // defines the block number (of the old cube) that will be in the place of the corresponding index after one right turn
            // this is the same assignment for all sides
            // for example: the new block[0] is the old block[2]
            int[] adoptA = {2, 5, 8, 1, 4, 7, 0, 3, 6}; // adoption assignments for the main face
            // adoption assignments for the 4 sides affected by a turn
            // each level is for each side. Each level has 4 sub arrays with the name of the face and the blocks affected by the turn
            int[][][] adoptB = {
                    {{left, 6, 3, 0}, {back, 6, 3, 0}, {right, 6, 3, 0}, {front, 6, 3, 0}}, // top
                    {{left, 2, 5, 8}, {front, 2, 5, 8}, {right, 2, 5, 8}, {back, 2, 5, 8}}, // bottom
                    {{left, 8, 7, 6}, {top, 2, 5, 8}, {right, 0, 1, 2}, {bottom, 6, 3, 0}}, // front
                    {{right, 8, 7, 6}, {top, 6, 3, 0}, {left, 0, 1, 2}, {bottom, 2, 5, 8}}, // back
                    {{front, 8, 7, 6}, {top, 8, 7, 6}, {back, 0, 1, 2}, {bottom, 8, 7, 6}}, // right
                    {{back, 8, 7, 6}, {top, 0, 1, 2}, {front, 0, 1, 2}, {bottom, 0, 1, 2}}  // left
            };

            char[][] copy2 = new char[6][9]; // copy of the cube
            int faceO, blockO, faceN, blockN; // old face and block as well as new face and block
            // array of the abbreviations of each move
            // directly correlate to face indexes (top, bottom, etc...)
            String[] move_name = {"U", "D", "F", "B", "R", "L"};

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
        }
    }
