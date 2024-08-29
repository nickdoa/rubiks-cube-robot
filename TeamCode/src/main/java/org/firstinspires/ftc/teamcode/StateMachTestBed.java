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

    /*
    private int currentStep = 10;
    private int runningStep = -1;
    private double stepStartTime = 0;
    */

    private int hue = 0;
    private String currentColor = "";

    private String[][] cube = {
            {"R","R","R","R","R","R","R","R","R"},
            {"O","O","O","O","O","O","O","O","O"},
            {"W","W","W","W","W","W","W","W","W"},
            {"Y","Y","Y","Y","Y","Y","Y","Y","Y"},
            {"G","G","G","G","G","G","G","G","G"},
            {"B","B","B","B","B","B","B","B","B"},


    };

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");

        oneDrive  = hardwareMap.get(DcMotor.class, "one_Drive");
        twoDrive = hardwareMap.get(DcMotor.class, "two_Drive");
        threeDrive  = hardwareMap.get(DcMotor.class, "three_Drive");
        fourDrive = hardwareMap.get(DcMotor.class, "four_Drive");

        oneServo  = hardwareMap.get(Servo.class, "one_Servo");
        twoServo = hardwareMap.get(Servo.class, "two_Servo");
        threeServo  = hardwareMap.get(Servo.class, "three_Servo");
        fourServo = hardwareMap.get(Servo.class, "four_Servo");

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            hue = (int)JavaUtil.rgbToHue(colorSensor.red(), colorSensor.green(), colorSensor.blue());
            currentColor = getColor();
            switch (currentColor)
            {
                case "red":
                {
                    threeDrive.setPower(.8);
                    break;
                } // end step 10

                case "blue":
                {
                    oneDrive.setPower(.8);
                    break;
                }

                case "green":
                {
                    twoDrive.setPower(.8);
                    break;
                }

                case "yellow":
                {
                    fourDrive.setPower(.8);
                    break;
                }

                case "white":
                {
                    oneServo.setPosition(1);
                    sleep(1000);
                    oneServo.setPosition(-1);
                    break;
                }

                case "orange":
                {
                    twoServo.setPosition(1);
                    sleep(1000);
                    twoServo.setPosition(-1);
                }

                default :
                {
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

    public String getColor() {
        if (hue >= 25 && hue <= 40) {
            return "red";
        }
        else if (hue >= 200 && hue <= 225) {
            return "blue";
        }
        else if (hue >= 130 && hue <= 135) {
            return "no color";
        }
        else if (hue >= 120 && hue <= 140) {
            return "green";
        }
        else if (hue >= 90 && hue <= 110) {
            return "yellow";
        }
        else if (hue >= 170 && hue <= 180) {
            return "white";
        }
        else if (hue >= 45 && hue <= 55) {
            return "orange";
        }
        else {
            return "no color";
        }
    }

    /*
    private void motorPowers(float motor1,float motor2, float motor3, float motor4){
        oneDrive.setPower();
    }

     */
}
